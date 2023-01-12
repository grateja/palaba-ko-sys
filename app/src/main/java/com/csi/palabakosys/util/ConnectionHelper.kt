package com.csi.palabakosys.util

import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConnectionHelper
@Inject
constructor()
{
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    var onSuccess: ((String) -> Unit) ? = null
    var onError: ((String) -> Unit) ? = null

    fun activate(ipAddress: String, pulse: Int, token: String) {
        val request = Request.Builder()
            .url("http://$ipAddress/activate?pulse=$pulse&token=$token")
            .build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                onError?.invoke(e.message.toString())
                onError = null
            }

            override fun onResponse(call: Call, response: Response) {
                println("body")
                val body = response.body()?.string()
                println(body)
                onSuccess?.invoke(body.toString())
                onSuccess = null
            }
        })
    }
}