package com.example.frontend_robots.search

import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.frontend_robots.R
import java.io.ByteArrayOutputStream
import java.io.File


class SearchActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    //private val
    private var selectedByteArray: ByteArray? = null
    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        displaySearch()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == 0) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val photo = data.extras?.get("data")
                selectedPhotoUri = if (data.data != null) data.data else getImageUri(applicationContext, photo as Bitmap) }
                if (selectedPhotoUri != null) {
                    sendImage()
                }
            }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displaySearch() {
        openPopup()
    }

    private fun sendImage() {
        val bundle = Bundle()
        val fragment = SearchLoadingFragment.newInstance()
        bundle.putString("compressed_img", selectedPhotoUri.toString())
        fragment.arguments = bundle
        openFragment(fragment)
    }

    private fun openPopup(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Seleccione una opcion")

        val dialogView = layoutInflater.inflate(R.layout.image_options, null)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        val upload = dialogView.findViewById<Button>(R.id.upload)
        val takeImage = dialogView.findViewById<Button>(R.id.take_image)
        upload.setOnClickListener {
            setupPhotoButton()
        }
        takeImage.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private fun setupPhotoButton(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }


    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }
}