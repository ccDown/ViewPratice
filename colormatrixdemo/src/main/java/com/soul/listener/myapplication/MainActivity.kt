package com.soul.listener.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {
    private var imageUri: Uri? = null

    companion object {

        val RESULT_SELECT = 1
        val RESULT_TAKEPHOTO = 2
        val RESULT_CROPPHOTO = 3
    }

    private var afterBitmap: Bitmap? = null
    private var paint: Paint? = null
    private var canvas: Canvas? = null
    private var baseBitmap: Bitmap? = null
    private var progressSave: Int? = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sb_red!!.setOnSeekBarChangeListener(seekBarChange)
        sb_green!!.setOnSeekBarChangeListener(seekBarChange)
        sb_blue!!.setOnSeekBarChangeListener(seekBarChange)
        sb_alpha!!.setOnSeekBarChangeListener(seekBarChange)
        sb_saveimage!!.setOnSeekBarChangeListener(seekBarChange)

        //初始化背景
        initPaint()

        btn_takephoto.setOnClickListener {
            //获取系统版本
            val currentapiVersion = android.os.Build.VERSION.SDK_INT
            //激活相机
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //判断存储卡是否可用 可用进行存储
            if (hasSdcard()) {
                val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_mm_ss")
                val fileName = simpleDateFormat.format(Date())
                val tempFile = File(Environment.getExternalStorageDirectory(), fileName + ".jpg")
                if (currentapiVersion < 24) {
                    //从文件中创建uri
                    imageUri = Uri.fromFile(tempFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                } else {
                    //兼容android7.0 使用共享文件的形式
                    val contentValues = ContentValues(1)
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.absolutePath)
                    //检查是否有存储权限，以免崩溃
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)

                        Toast.makeText(this, "请开启存储权限", Toast.LENGTH_SHORT).show()
                    }
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                }
            }
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            startActivityForResult(intent, RESULT_TAKEPHOTO);
        }

        btn_select.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
            val i = Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(i, RESULT_SELECT)
        }

        //保存图片
        btn_save.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_mm_ss")
            val fileName = simpleDateFormat.format(Date())
            val fileParent = File(Environment.getExternalStorageDirectory().absolutePath + "/kuangege/kuan")
            if (!fileParent.exists()) {
                fileParent.mkdirs()
            }
            val tempFile = File(fileParent, fileName + ".jpg")
            val bos = BufferedOutputStream(FileOutputStream(tempFile))
            //图片压缩保存
            (iv_show.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, progressSave!!, bos)
            bos.flush()
            bos.close()
        }

        //长按显示图片详细信息
        iv_show.setOnLongClickListener {
            Log.e("TAG","图片路径  ${imageUri?.toString()}")
            if (imageUri!=null) {
                val exifInterface = ExifInterface(imageUri.toString())
                Toast.makeText(this, "设备品牌${exifInterface.getAttribute(ExifInterface.TAG_MAKE)}  " +
                        "数字化时间${exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)}", Toast.LENGTH_SHORT).show()
                true
            }else{
                false
            }
        }
    }

    fun initPaint() {
        //开启图片缓存
//        iv_show.isDrawingCacheEnabled = true
        //获取图片缓存
//        baseBitmap = iv_show.getDrawingCache()
        //关闭图片缓存
//        iv_show.isDrawingCacheEnabled = false
        baseBitmap = (iv_show.drawable as BitmapDrawable).bitmap

        // 从资源文件中获取图片
//        baseBitmap = BitmapFactory.decodeResource(resources,
//                R.drawable.picture)
        // 获取一个与baseBitmap大小一致的可编辑的空图片
        afterBitmap = Bitmap.createBitmap(baseBitmap!!.width,
                baseBitmap!!.height, baseBitmap!!.config)
        canvas = Canvas(afterBitmap)
        paint = Paint()
        paint!!.isAntiAlias = true
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("TAG", "requestCode=${requestCode}  resultCode=${resultCode}   ")
        when (requestCode) {
            RESULT_SELECT -> {
                if (data != null) {
                    val slesctedImage = data.data
                    Log.e("TAG", "slesctedImage=${slesctedImage}  data!!.data=${data!!.data}")

                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(slesctedImage,
                            filePathColumn, null, null, null)
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()


                    val options = BitmapFactory.Options()
                    //只获取图片的大小信息 而不是将整张图片载入内存中 避免内存溢出
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(picturePath,options)
                    val height = options.outHeight
                    val width = options.outWidth
                    var inSampleSize = 2//默认像素压缩比例  压缩为原图的1/2
                    val minLen = Math.min(height,width)//原图的最小边长
                    if (minLen > 100){
                        //如果㡳图像的最小边长大于100dp()
                        val ratio = minLen /100.0f //计算像素压缩比例
                        inSampleSize = ratio.toInt()
                    }

                    options.inJustDecodeBounds  = false //计算好压缩比例后  加载原图
                    options.inSampleSize = inSampleSize//设置为刚才计算的压缩比例
                    val bm = BitmapFactory.decodeFile(picturePath,options)//解码文件
                    iv_show.scaleType = ImageView.ScaleType.FIT_CENTER
                    iv_show.setImageBitmap(bm)


//                    imageUri = Uri.parse(picturePath)
//                    afterBitmap = Bitmap.createBitmap((Drawable.createFromPath(picturePath) as BitmapDrawable).bitmap)
//                    iv_show.setImageBitmap(afterBitmap)
                    //此时界面图片发生变化  要进行画笔画布的初始化
                    initPaint()
                }
            }
            RESULT_TAKEPHOTO -> {
                Log.e("TAG", "RESULT_TAKEPHOTO      data!!.data=${data?.data}")

                val imageUri = imageUri
                val intent = Intent("com.android.camera.action.CROP")
                intent.setDataAndType(imageUri, "image/*")
                intent.putExtra("scale", true)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

                //启动裁剪程序
                startActivityForResult(intent, RESULT_CROPPHOTO)
            }
            RESULT_CROPPHOTO -> {
                if (resultCode==-1) {
                    Log.e("TAG", "RESULT_CROP_PHOTO")
                    val imageUri = imageUri
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    iv_show.setImageBitmap(bitmap)

                    this.imageUri = Uri.parse(getRealFilePath(this, imageUri!!))

                    //此时界面图片发生变化  要进行画笔画布的初始化
                    initPaint()
                }
            }
        }
    }

    /*
   * 判断sdcard是否被挂载
   */
    fun hasSdcard(): Boolean {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> if (grantResults!!.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
            else -> {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
        }
    }

    private val seekBarChange = object : OnSeekBarChangeListener {

        override fun onStopTrackingTouch(seekBar: SeekBar) {}

        override fun onStartTrackingTouch(seekBar: SeekBar) {}

        override fun onProgressChanged(seekBar: SeekBar, progress: Int,
                                       fromUser: Boolean) {
            // 获取每个SeekBar当前的值
            val progressR = sb_red!!.progress / 128f
            val progressG = sb_green!!.progress / 128f
            val progressB = sb_blue!!.progress / 128f
            val progressA = sb_alpha!!.progress / 128f
            progressSave = sb_saveimage!!.progress
            Log.i("main", "R：G：B:progressSave=$progressR：$progressG：$progressB:$progressSave")
            // 根据SeekBar定义RGBA的矩阵
            val src = floatArrayOf(
                    progressR, 0f, 0f, 0f, 0f,
                    0f, progressG, 0f, 0f, 0f,
                    0f, 0f, progressB, 0f, 0f,
                    0f, 0f, 0f, progressA, 0f
            )
            // 设置Paint的颜色
            paint!!.colorFilter = ColorMatrixColorFilter(src)
            // 通过指定了RGBA矩阵的Paint把原图画到空白图片上
            canvas!!.drawBitmap(baseBitmap!!, Matrix(), paint)
            iv_show!!.setImageBitmap(afterBitmap)
        }
    }

    public fun getRealFilePath(context: Context, uri: Uri) : String  {
        if ( null == uri ) return "";
        val scheme = uri.getScheme();
        var data = String();
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            val cursor = context.getContentResolver().query( uri, arrayOf(MediaStore.Images.ImageColumns.DATA ), null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    val index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
