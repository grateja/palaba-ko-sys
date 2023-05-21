package com.csi.palabakosys.app

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

open class EndingActivity : AppCompatActivity() {
    private var doubleclick = false
    override fun onBackPressed() {
        if(doubleclick) {
//            super.onBackPressed()
            moveTaskToBack(true)
//            finish()
        }
        doubleclick = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleclick = false
        }, 2000)
    }
}