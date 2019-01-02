package org.wit.androidhillfort.room

import android.content.Context
import androidx.room.Room
import org.jetbrains.anko.coroutines.experimental.bg
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.models.PlacemarkStore

class PlacemarkStoreRoom(val context: Context) : PlacemarkStore {

  var dao: PlacemarkDao

  init {
    val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
        .fallbackToDestructiveMigration()
        .build()
    dao = database.placemarkDao()
  }

  suspend override fun findAll(): List<PlacemarkModel> {
    val deferredPlacemarks = bg {
      dao.findAll()
    }
    val placemarks = deferredPlacemarks.await()
    return placemarks
  }

  suspend override fun findById(id: Long): PlacemarkModel? {
    val deferredPlacemark = bg {
      dao.findById(id)
    }
    val placemark = deferredPlacemark.await()
    return placemark
  }

  suspend override fun create(placemark: PlacemarkModel) {
    bg {
      dao.create(placemark)
    }
  }

  suspend override fun update(placemark: PlacemarkModel) {
    bg {
      dao.update(placemark)
    }
  }

  suspend override fun delete(placemark: PlacemarkModel) {
    bg {
      dao.deletePlacemark(placemark)
    }
  }

  override fun clear() {
  }
}