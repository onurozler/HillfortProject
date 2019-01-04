package org.wit.androidhillfort.models

interface PlacemarkStore {
  suspend fun findAll(): List<PlacemarkModel>
  suspend fun findAllofThem(): List<PlacemarkModel>
  suspend fun findAllFav(): List<PlacemarkModel>
  suspend fun findById(id:Long) : PlacemarkModel?
  suspend fun createAll(placemark: PlacemarkModel)
  suspend fun create(placemark: PlacemarkModel)
  suspend fun update(placemark: PlacemarkModel)
  suspend fun delete(placemark: PlacemarkModel)
  suspend fun findByName(name:String) : PlacemarkModel?
  fun clear()
}