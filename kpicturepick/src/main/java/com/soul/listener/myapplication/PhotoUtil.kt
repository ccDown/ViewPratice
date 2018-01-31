package com.soul.listener.myapplication

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.support.v4.app.Fragment

/**
 * @description
 * @author kuan
 * Created on 2018/picture/29.
 */
public class PhotoUtil() {
    companion object {
        public val NONE = 0

        public val IMAGE_UNSPECIFIED = "image/*"//随意图片类型

        public val PHOTOGRAPH = 1//拍照

        public val PHOTOZOOM = 2//缩放
        public val PHOTORESOULT = 3//结果

        public val PICTURE_HEIGHT = 500
        public val PICTURE_WIDTH = 500
        public var imageName: String? = null

        /**
         * 从系统相册中选取照片上传
         */
        public fun selectPictureFromAlbum(activity: Activity): Unit {
            //调用系统相册
            val intent = Intent(Intent.ACTION_PICK,null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_UNSPECIFIED)
            //调用剪切功能
            activity.startActivityForResult(intent,PHOTOZOOM)
        }

        fun selectPictureFromAlbum(fragment: Fragment): Unit {
            //调用系统相册
            val intent = Intent(Intent.ACTION_PICK,null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_UNSPECIFIED)
            //调用剪切功能
            fragment.startActivityForResult(intent, PHOTOZOOM)
        }


    }



}