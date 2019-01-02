package org.wit.androidhillfort.room

import androidx.room.*
import org.wit.androidhillfort.models.PlacemarkModel

@Dao
interface PlacemarkDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun create(placemark: PlacemarkModel)

  @Query("SELECT * FROM PlacemarkModel")
  fun findAll(): List<PlacemarkModel>

  @Query("select * from PlacemarkModel where hId = :id")
  fun findById(id: Long): PlacemarkModel

  @Update
  fun update(placemark: PlacemarkModel)

  @Delete
  fun deletePlacemark(placemark: PlacemarkModel)
}