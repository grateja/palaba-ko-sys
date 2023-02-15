package com.csi.palabakosys.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.csi.palabakosys.datastates.RemoteActivationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MachineConnectionViewModel
@Inject
constructor() : ViewModel(

)
{
//    val dataState = MutableLiveData<RemoteActivationState>()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun connect(url: String?) {

        if(url == null) return

        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                viewModelScope.launch {
//                    dataState.value = RemoteActivationState.Error(e.message.toString())
                }
            }

            override fun onResponse(call: Call, response: Response) {
                println("body")
                val body = response.body()?.string()
                println(body)
                viewModelScope.launch {
//                    dataState.value = RemoteActivationState.Connected(body.toString())
                }
            }
        })
    }
}