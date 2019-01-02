package org.wit.androidhillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.androidhillfort.models.json.PlacemarkJSONStore
import org.wit.androidhillfort.models.PlacemarkStore
import org.wit.androidhillfort.models.firebase.PlacemarkFireStore
import org.wit.androidhillfort.room.PlacemarkStoreRoom

class MainApp : Application(), AnkoLogger {

  lateinit var placemarks: PlacemarkStore

  override fun onCreate() {
    super.onCreate()
    //placemarks = PlacemarkJSONStore(applicationContext)
    //placemarks = PlacemarkStoreRoom(applicationContext)
    placemarks = PlacemarkFireStore(applicationContext)
    info("Placemark started")
  }
}