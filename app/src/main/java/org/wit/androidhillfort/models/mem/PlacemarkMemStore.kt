package org.wit.androidhillfort.models.mem

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.models.PlacemarkStore

var lastId = 0L

internal fun getId(): Long {
  return lastId++
}

class PlacemarkMemStore : PlacemarkStore, AnkoLogger {

  val placemarks = ArrayList<PlacemarkModel>()

  suspend override fun findAll(): List<PlacemarkModel> {
    return placemarks
  }

  suspend override fun findById(id:Long) : PlacemarkModel? {
    val foundPlacemark: PlacemarkModel? = placemarks.find { it.hId == id }
    return foundPlacemark
  }

  suspend override fun create(placemark: PlacemarkModel) {
    placemark.hId = getId()
    placemarks.add(placemark)
    logAll()
  }

  suspend override fun update(placemark: PlacemarkModel) {
    var foundPlacemark: PlacemarkModel? = placemarks.find { p -> p.hId == placemark.hId }
    if (foundPlacemark != null) {
      foundPlacemark.title = placemark.title
      foundPlacemark.description = placemark.description
      foundPlacemark.hImage = placemark.hImage
      foundPlacemark.location = placemark.location
      logAll();
    }
  }

  suspend override fun delete(placemark: PlacemarkModel) {
    placemarks.remove(placemark)
  }
  
  fun logAll() {
    placemarks.forEach { info("${it}") }
  }

  override fun clear() {
    placemarks.clear()
  }
}