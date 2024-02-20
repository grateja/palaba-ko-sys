package com.csi.palabakosys.di

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.csi.palabakosys.room.db.MainDatabase
import com.csi.palabakosys.util.Constants
//import com.mazenrashed.printooth.Printooth
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var database: MainDatabase
    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val mediaDir = File(filesDir, Constants.PICTURES_DIR)
        if(!mediaDir.exists()) {
            mediaDir.mkdirs()
        }
    }
}