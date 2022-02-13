package com.example.frontend_robots

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend_robots.databinding.ActivityMainBinding
import com.example.frontend_robots.domain.SearchData
import com.example.frontend_robots.domain.SearchResponse
import com.example.getfluent.NetworkModule
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), Callback<SearchResponse> {
    private lateinit var binding: ActivityMainBinding
    val REQUEST_IMAGE_CAPTURE = 1
    private var selectedByteArray: ByteArray? = null
    private var selectedPhotoUri: Uri? = null
    private lateinit var searchResponse: SearchResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            openPopup()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(baseContext?.contentResolver, selectedPhotoUri)
            setPhoto(bitmap)
            if (selectedByteArray != null) {
                searchImage()
            }
        }
    }

    override fun onResponse(
        call: retrofit2.Call<SearchResponse>,
        response: Response<SearchResponse>
    ) {
        if (response.code() == 200) {
            Log.d("Session", "Got a 200!")
            searchResponse = response.body() as SearchResponse
        }
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
    }

    private fun setPhoto(bitmap: Bitmap){
        selectedByteArray = compressBitmap(bitmap, 30)
    }

    private fun compressBitmap(bitmap: Bitmap, quality: Int): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
        val byteArray = stream.toByteArray()
        return byteArray
    }

    private fun searchImage() {
        val call = NetworkModule.buildRetrofitClient().search(image =  SearchData(selectedByteArray.toString()))
        call.enqueue(this)
    }

    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
        Log.d("Error search:", call.request().body().toString())
    }

}