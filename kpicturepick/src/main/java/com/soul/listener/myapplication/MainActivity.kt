package com.soul.listener.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var imageUri :Uri ? = null

    companion object {
        val RESULT_LOAD_IMAGE = 3//选择图片
        val RESULT_TAKE_PHOTO = 4//拍照
        val RESULT_CROP_PHOTO = 5//切割图片
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }
            val i = Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(i, RESULT_LOAD_IMAGE)
        }

        takephoto.setOnClickListener {
            //获取系统版本
            val currentapiVersion = android.os.Build.VERSION.SDK_INT
            //激活相机
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //判断存储卡是否可用 可用进行存储
            if (hasSdcard()){
                val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_mm_ss")
                val fileName = simpleDateFormat.format(Date())
                val tempFile = File(Environment.getExternalStorageDirectory(),fileName+".jpg")
                if (currentapiVersion<24){
                    //从文件中创建uri
                    imageUri = Uri.fromFile(tempFile)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                }else{
                    //兼容android7.0 使用共享文件的形式
                    val contentValues = ContentValues(1)
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.absolutePath)
                    //检查是否有存储权限，以免崩溃
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)

                        Toast.makeText(this, "请开启存储权限", Toast.LENGTH_SHORT).show()
                    }
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                }
            }
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            startActivityForResult(intent, RESULT_TAKE_PHOTO);
        }

//        imageview.setOnGenericMotionListener(object : View.OnGenericMotionListener {
//            override fun onGenericMotion(v: View?, event: MotionEvent?): Boolean {
//                if (event!!.pointerCount<2)return false
//
//            }
//
//        })

        imageview.setOnClickListener {
            imageview.isDrawingCacheEnabled = true
            var bitmap = Bitmap.createBitmap(imageview.getDrawingCache())
            val paint = Paint()
            paint.isAntiAlias = true
            val colorMatrix = ColorMatrix(floatArrayOf(
                    1f, 0f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f, 500f,
                    0f, 0f, 1f, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
            ))

            val matrix = Matrix()
            matrix.setRotate(500f,30f,60f)

            paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
            val canvas = Canvas()

            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
            canvas.drawBitmap(bitmap,null, Rect(0,0,bitmap.width, 500 * bitmap!!.height / bitmap!!.width),paint)

            imageview.draw(canvas)
            imageview.isDrawingCacheEnabled = false


            val canva = Canvas()
            imageview.draw(canva)
            imageview.postInvalidate()
        }


    }

    /*
    * 判断sdcard是否被挂载
    */
    fun hasSdcard(): Boolean {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            0->{
                if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
            }
            else ->{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
            when(requestCode ){
                RESULT_LOAD_IMAGE  ->{
                    val slesctedImage = data!!.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(slesctedImage,
                            filePathColumn, null, null, null)
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    imageview.background = Drawable.createFromPath(picturePath)
                }
                RESULT_TAKE_PHOTO ->{
                    Log.e("TAG","RESULT_TAKE_PHOTO")
                    val imageUri = imageUri
                    val intent = Intent("com.android.camera.action.CROP")
                    intent.setDataAndType(imageUri,"image/*")
                    intent.putExtra("scale",true)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                    //启动裁剪程序
                    startActivityForResult(intent, RESULT_CROP_PHOTO)
                }
                RESULT_CROP_PHOTO ->{
                    Log.e("TAG","RESULT_CROP_PHOTO")
                    val imageUri = imageUri
                    val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri))
                    imageview.setImageBitmap(bitmap)
                }
                else ->{
                    Log.e("TAG","123")
                }
            }
    }
}
