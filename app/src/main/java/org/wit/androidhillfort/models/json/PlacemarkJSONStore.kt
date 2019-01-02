package org.wit.androidhillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.androidhillfort.helpers.*
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.models.PlacemarkStore
import java.util.*

val JSON_FILE = "placemarks.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<PlacemarkModel>>() {}.type

fun generateRandomId(): Long {
  return Random().nextLong()
}

class PlacemarkJSONStore : PlacemarkStore, AnkoLogger {

  val context: Context
  var placemarks = mutableListOf<PlacemarkModel>()

  constructor (context: Context) {
    this.context = context
    if (exists(context, JSON_FILE)) {
      deserialize()
    }
  }

  suspend override fun findAll(): MutableList<PlacemarkModel> {
    return placemarks
  }

  suspend override fun findById(id:Long) : PlacemarkModel? {
    val foundPlacemark: PlacemarkModel? = placemarks.find { it.hId == id }
    return foundPlacemark
  }

  suspend override fun create(placemark: PlacemarkModel) {
    placemark.hId = generateRandomId()
    placemarks.add(placemark)
    serialize()
  }

  suspend override fun update(placemark: PlacemarkModel) {
    val placemarksList = findAll() as ArrayList<PlacemarkModel>
    var foundPlacemark: PlacemarkModel? = placemarksList.find { p -> p.hId == placemark.hId }
    if (foundPlacemark != null) {
      foundPlacemark.title = placemark.title
      foundPlacemark.description = placemark.description
      foundPlacemark.hImage = placemark.hImage
      foundPlacemark.location = placemark.location
    }
    serialize()
  }

  suspend override fun delete(placemark: PlacemarkModel) {
    placemarks.remove(placemark)
    serialize()
  }

  private fun serialize() {
    val jsonString = gsonBuilder.toJson(placemarks, listType)
    write(context, JSON_FILE, jsonString)
  }

  private fun deserialize() {
    val jsonString = read(context, JSON_FILE)
    placemarks = Gson().fromJson(jsonString, listType)
  }

  override fun clear() {
    placemarks.clear()
  }
}
