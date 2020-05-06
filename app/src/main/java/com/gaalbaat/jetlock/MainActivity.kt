package com.gaalbaat.jetlock

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var mPhotoBtn: Button
    private lateinit var mPhotoSaveBtn : Button
    private lateinit var mPhotoView: ImageView
    private var myCurrentImage : Uri ? = null
//    Permission arrayOf
//    val mPermission = arrayOf(
//        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        android.Manifest.permission.READ_EXTERNAL_STORAGE
//    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        UI Initialized
        mPhotoBtn = findViewById(R.id.myPhotoClick)
        mPhotoSaveBtn = findViewById(R.id.mySaveBtn)
        mPhotoView = findViewById(R.id.myImageView)

//        UI Click
        mPhotoBtn.setOnClickListener {
            permissionInit()
//            selectPhoto()
        }
        mPhotoSaveBtn.setOnClickListener {
//            permissionInit()
            savePhoto()
            // Save the image to gallery and get saved image Uri
            val uri = saveImage(R.drawable.copywriting,"Flower")
//            toast("saved : $uri")
            Toast.makeText(applicationContext, "Saved: $uri", Toast.LENGTH_SHORT).show();
            mPhotoView.setImageURI(uri)
        }
    }

    private fun saveImage(drawable: Any, title: String): Uri {
        // Get the image from drawable resource as drawable object
//        Here error giving.
        val drawable = ContextCompat.getDrawable(applicationContext, drawable as Int)

        // Get the bitmap from drawable object
        val bitmap = (drawable as BitmapDrawable).bitmap

        // Save image to gallery
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            title,
            "Image of $title"
        )

        // Parse the gallery image url to uri
        return Uri.parse(savedImageURL)

    }


    //      Permission Initialized
    private fun permissionInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {
//                    Permission Denied
                val permissions = arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissions(permissions, PERMISSION_CODE)

                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                    .show();
            } else {
//                    Permission Already granted
                selectPhoto()
//                savePhoto()

                Toast.makeText(
                    applicationContext,
                    "Permission Already granted",
                    Toast.LENGTH_SHORT
                ).show();
            }
        } else {
//                System is < Marshmallow
            selectPhoto()
//            savePhoto()
            Toast.makeText(applicationContext, "No need for permission", Toast.LENGTH_SHORT)
                .show();
        }
    }

    companion object {
        private const val PICK_CODE = 107
        private const val PERMISSION_CODE = 100
    }
//      select Photo -
    private fun selectPhoto() {
        val selectPhotoIntent = Intent(Intent.ACTION_PICK)
        selectPhotoIntent.type = "image/*"
        startActivityForResult(selectPhotoIntent, PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_CODE) {
            mPhotoView.setImageURI(data?.data)
             myCurrentImage = data?.data
            data?.data?.let { saveImage(it, "Hello") }
            Toast.makeText(applicationContext, "${data?.data}", Toast.LENGTH_SHORT).show();
        }
    }

    private fun savePhoto() {
//        val externalStorageState = Environment.getExternalStorageState()
//
//        if(externalStorageState == Environment.MEDIA_MOUNTED){
//            val storageDirectory = Environment.getExternalStorageDirectory().toString()
//            val mFile = File(storageDirectory,"Test_Image.jpg")
//            try{
//                val stream: OutputStream = FileOutputStream(mFile)
//                var mPhotoDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.background)
//
//                var mBitmap = (mPhotoDrawable as BitmapDrawable).bitmap
//                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//                myImageView.setImageURI(Uri.parse(mFile.absolutePath))
//                stream.flush()
//                stream.close()
//                Toast.makeText(applicationContext, "Photo Saved...", Toast.LENGTH_SHORT).show();
//            }catch (o : Exception){
//                o.printStackTrace()
//            }
//        }else{
//            Toast.makeText(applicationContext, "Unable to access storage", Toast.LENGTH_SHORT).show();
//        }




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectPhoto()
//                    savePhoto()
                } else {
                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT)
                        .show();
                }
            }
        }
    }

}
