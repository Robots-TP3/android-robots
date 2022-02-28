package com.example.frontend_robots.search

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
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
                selectedPhotoUri = data.data.also { data.extras?.get(MediaStore.EXTRA_OUTPUT) }
                //val bitmap = MediaStore.Images.Media.getBitmap(baseContext?.contentResolver, selectedPhotoUri)
                //setPhoto(bitmap)
                if (selectedPhotoUri != null) {
                    sendImage()
                }
            }
        }
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

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
        /*val m_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(Environment.getExternalStorageDirectory(), "image.jpg")
        val uri = FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            file
        )
        m_intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(m_intent, REQUEST_IMAGE_CAPTURE)*/

    }

    private fun setPhoto(bitmap: Bitmap){
        selectedByteArray = compressBitmap(bitmap, 30)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }


    private fun openFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }
}