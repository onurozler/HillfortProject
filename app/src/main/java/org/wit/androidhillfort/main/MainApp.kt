package org.wit.androidhillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.androidhillfort.models.PlacemarkStore
import org.wit.androidhillfort.models.firebase.PlacemarkFireStore

class MainApp : Application(), AnkoLogger {

  lateinit var placemarks: PlacemarkStore

  override fun onCreate() {
    super.onCreate()
    placemarks = PlacemarkFireStore(applicationContext)
    info("Placemark started")
  }
}