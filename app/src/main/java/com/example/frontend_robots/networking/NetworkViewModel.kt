package com.example.frontend_robots.networking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frontend_robots.repository.LoadingStatus
import org.apache.commons.lang3.StringUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkViewModel<S : Any> : ViewModel() {

    protected var msgResponse = StringUtils.EMPTY
    protected val loadingStatusMutable = MutableLiveData<LoadingStatus>()
    val loadingStatus : LiveData<LoadingStatus>
        get() = loadingStatusMutable

    fun hideLoading() {
        loadingStatusMutable.value = LoadingStatus.HIDE
    }

    fun getMessageResponse() = msgResponse

    protected fun executeCallback(call: Call<S>) {
        loadingStatusMutable.value = LoadingStatus.LOADING
        call.enqueue(object : Callback<S> {

            override fun onResponse(call: Call<S>, response: Response<S>) {
                if (response.isSuccessful) {
                    onSuccessful(response)
                    loadingStatusMutable.value = LoadingStatus.SUCCESS
                } else {
                    onFailure(response)
                    loadingStatusMutable.value = LoadingStatus.FAILURE
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                if (!call.isCanceled) {
                    msgResponse = t.message.toString()
                    loadingStatusMutable.value = LoadingStatus.ERROR
                }
            }
        })
    }

    protected abstract fun onSuccessful(response: Response<S>)
    protected abstract fun onFailure(response: Response<S>)
    abstract fun execute(call: Call<S>)

}