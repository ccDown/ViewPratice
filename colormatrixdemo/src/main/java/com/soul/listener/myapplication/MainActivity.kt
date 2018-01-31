package com.soul.listener.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {

    companion object {

        val RESULT_SELECT = 1
        val RESULT_SAVE = 2
    }

    private var afterBitmap: Bitmap? = null
    private var paint: Paint? = null
    private var canvas: Canvas? = null
    private var baseBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sb_red!!.setOnSeekBarChangeListener(seekBarChange)
        sb_green!!.setOnSeekBarChangeListener(seekBarChange)
        sb_blue!!.setOnSeekBarChangeListener(seekBarChange)
        sb_alpha!!.setOnSeekBarChangeListener(seekBarChange)

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



        btn_select.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            }
            val i = Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(i, RESULT_SELECT)
        }

        btn_save.setOnClickListener {
            val simpleDateFormat = SimpleDateFormat("yyyy_MM_dd_mm_ss")
            val fileName = simpleDateFormat.format(Date())
            val fileParent = File(Environment.getExternalStorageDirectory().absolutePath+"kuangege")
            if (!fileParent.exists()){
                fileParent.mkdir()
            }
            val tempFile = File(fileParent,fileName+".jpg")
            val bos = BufferedOutputStream(FileOutputStream(tempFile))
            (iv_show.drawable as BitmapDrawable).bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULT_SELECT -> {
                val slesctedImage = data!!.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = contentResolver.query(slesctedImage,
                        filePathColumn, null, null, null)
                cursor.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                val picturePath = cursor.getString(columnIndex)
                cursor.close()
                iv_show.background = Drawable.createFromPath(picturePath)
            }
            RESULT_SAVE -> {

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0 -> {
                if (grantResults!!.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
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
            Log.i("main", "R：G：B=$progressR：$progressG：$progressB")
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
}
