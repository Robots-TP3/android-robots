package com.example.frontend_robots.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.MediaColumns
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.frontend_robots.R
import com.example.frontend_robots.utils.BaseFragment
import com.example.getfluent.NetworkModule
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class SearchLoadingFragment: BaseFragment(R.layout.fragment_loading) {

    private val REQUEST_PERMISSION_CODE = 1

    private val searchLoadingViewModel by activityViewModels<SearchViewModel>()

    @SuppressLint("Recycle")
    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaColumns.DATA)
        val cursor: Cursor? =
            uri?.let { activity?.contentResolver?.query(it, projection, null, null, null) }
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaColumns.DATA)
        cursor?.moveToFirst()
        return columnIndex?.let { cursor.getString(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img = requireArguments().getString("compressed_img")
        val path = getPath(Uri.parse(img))
        Log.d("Search", "Obteniendo datos de la busqueda")
        requestPermissionsIfNecessary(Array<String>(1) {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
        val file = File(path!!)
        val requestFile = RequestBody.create(
            MediaType.parse("image/jpg"),
            file
        )
        val body = MultipartBody.Part.createFormData("image", "image", requestFile)
        searchLoadingViewModel.execute(NetworkModule.buildRetrofitClient().search(body))

        searchLoadingViewModel.userDataLoadedSuccessfully.observe(viewLifecycleOwner) { isLoaded ->
            if (isLoaded) {
                replaceFragment(SearchResultFragment.newInstance())
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission is not granted
                permissionsToRequest.add(permission)
            }
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionsToRequest.toArray(arrayOfNulls(0)),
                REQUEST_PERMISSION_CODE
            )
        }
    }


    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.search_container, fragment)
        transaction.commit()
    }

    companion object {
        fun newInstance(): SearchLoadingFragment = SearchLoadingFragment()
    }
}