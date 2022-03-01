package com.example.frontend_robots.search

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.frontend_robots.R
import com.example.frontend_robots.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import java.io.ByteArrayOutputStream

class SearchFragment: BaseFragment(R.layout.fragment_search) {

    private var selectedPhotoUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_button.setOnClickListener {
            displaySearch()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_IMAGE_LOAD) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val photo = data.extras?.get("data")
                selectedPhotoUri = if (data.data != null) data.data else getImageUri(this.requireContext(), photo as Bitmap) }
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

    private fun displaySearch() {
        openPopup()
    }

    private fun sendImage() {
        val bundle = Bundle()
        val fragment = SearchLoadingFragment.newInstance()
        bundle.putString("compressed_img", selectedPhotoUri.toString())
        fragment.arguments = bundle
        replaceFragment(fragment)
    }

    private fun openPopup(){
        val builder = AlertDialog.Builder(this.requireContext())
        builder.setTitle("Seleccione una opcion")

        val dialogView = layoutInflater.inflate(R.layout.image_options, null)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        dialog.show()
        val upload = dialogView.findViewById<Button>(R.id.upload)
        val takeImage = dialogView.findViewById<Button>(R.id.take_image)
        upload.setOnClickListener {
            setupPhotoButton()
            dialog.dismiss()
        }
        takeImage.setOnClickListener {
            dispatchTakePictureIntent()
            dialog.dismiss()
        }
    }

    private fun setupPhotoButton(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(this.requireContext().packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }

    companion object {
        private const val REQUEST_IMAGE_LOAD = 0
        private const val REQUEST_IMAGE_CAPTURE = 1
        fun newInstance(): SearchFragment = SearchFragment()
    }
}