package com.example.frontend_robots.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.frontend_robots.domain.SearchResponse
import com.example.frontend_robots.networking.NetworkViewModel
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Response

class SearchViewModel : NetworkViewModel<SearchResponse>() {

    private val mutableSearchDataLoadedSuccessfully = MutableLiveData<Boolean>()

    val userDataLoadedSuccessfully: LiveData<Boolean>
        get() = mutableSearchDataLoadedSuccessfully

    var searchResponse : SearchResponse? = null

    override fun execute(call: Call<SearchResponse>) {
        mutableSearchDataLoadedSuccessfully.value = false
        Log.i(TAG, "Getting search data...")
        executeCallback(call)
    }

    override fun onSuccessful(response: Response<SearchResponse>) {
        Log.i(TAG, "Search data loaded successfully")
        searchResponse = response.body()
        Log.i("search data:", searchResponse.toString())
        mutableSearchDataLoadedSuccessfully.value = true
    }
    override fun onFailure(response: Response<SearchResponse>) {
        Log.d(TAG, "Failure: $response")
        this.msgResponse = response.message()
        mutableSearchDataLoadedSuccessfully.value = true

    }

    companion object {
        const val TAG = "SEARCH_DATA"
    }
}