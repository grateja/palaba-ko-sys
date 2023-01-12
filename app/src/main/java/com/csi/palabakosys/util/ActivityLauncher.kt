package com.csi.palabakosys.util

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ActivityLauncher(activity: AppCompatActivity) {
    var onOk: ((ActivityResult) -> Unit) ? = null
    var onCancel: (() -> Unit) ? = null
    private var resultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == AppCompatActivity.RESULT_OK) {
                println("id from activity launcher")
                println(result.data?.getStringExtra("id"))
                onOk?.invoke(result)
            } else if(result.resultCode == AppCompatActivity.RESULT_CANCELED) {
                onCancel?.invoke()
            }
        }

    fun launch(intent: Intent) {
        resultLauncher.launch(intent)
    }
}