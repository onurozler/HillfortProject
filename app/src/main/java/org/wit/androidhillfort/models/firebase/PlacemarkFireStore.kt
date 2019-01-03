package org.wit.androidhillfort.models.firebase

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.jetbrains.anko.AnkoLogger
import org.wit.androidhillfort.helpers.readImageFromPath
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.models.PlacemarkStore
import java.io.ByteArrayOutputStream
import java.io.File

class PlacemarkFireStore(val context: Context) : PlacemarkStore, AnkoLogger {


  val placemarks = ArrayList<PlacemarkModel>()
  val allPlacemarks = ArrayList<PlacemarkModel>()
  lateinit var userId: String
  lateinit var db: DatabaseReference
  lateinit var st: StorageReference

  suspend override fun findAll(): List<PlacemarkModel> {
    return placemarks
  }

  suspend override fun findById(id: Long): PlacemarkModel? {
    val foundPlacemark: PlacemarkModel? = placemarks.find { p -> p.hId == id }
    return foundPlacemark
  }

  override suspend fun createAll(placemark: PlacemarkModel) {
    val key = db.child("allhillforts").child("placemarks").push().key
    key?.let {
      placemark.fbId = key
      allPlacemarks.add(placemark)
      db.child("allhillforts").child("placemarks").child(key).setValue(placemark)
      updateImage(placemark)
    }
  }

  suspend override fun create(placemark: PlacemarkModel) {
    val key = db.child("users").child(userId).child("placemarks").push().key
    key?.let {
      placemark.fbId = key
      placemarks.add(placemark)
      db.child("users").child(userId).child("placemarks").child(key).setValue(placemark)
      updateImage(placemark)


    }
  }

  suspend override fun update(placemark: PlacemarkModel) {
    var foundPlacemark: PlacemarkModel? = placemarks.find { p -> p.fbId == placemark.fbId }
    if (foundPlacemark != null) {
      foundPlacemark.title = placemark.title
      foundPlacemark.description = placemark.description
      foundPlacemark.hImage = placemark.hImage
      foundPlacemark.location = placemark.location
    }

    db.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)
    if ((placemark.hImage.length) > 0 && (placemark.hImage[0] != 'h')) {
      updateImage(placemark)
    }
  }

  suspend override fun delete(placemark: PlacemarkModel) {
    db.child("users").child(userId).child("placemarks").child(placemark.fbId).removeValue()
    placemarks.remove(placemark)
  }

  override fun clear() {
    placemarks.clear()
  }

  fun updateImage(placemark: PlacemarkModel) {
    if (placemark.hImage != "") {
      val fileName = File(placemark.hImage)
      val imageName = fileName.getName()

      var imageRef = st.child(userId + '/' + imageName)
      val baos = ByteArrayOutputStream()
      val bitmap = readImageFromPath(context, placemark.hImage)

      bitmap?.let {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)
        uploadTask.addOnFailureListener {
          println(it.message)
        }.addOnSuccessListener { taskSnapshot ->
          taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
            placemark.hImage = it.toString()
            db.child("users").child(userId).child("placemarks").child(placemark.fbId).setValue(placemark)
          }
        }
      }
    }
  }

  fun fetchPlacemarks(placemarksReady: () -> Unit) {
    val valueEventListener = object : ValueEventListener {
      override fun onCancelled(dataSnapshot: DatabaseError) {
      }
      override fun onDataChange(dataSnapshot: DataSnapshot) {
        dataSnapshot!!.children.mapNotNullTo(placemarks) { it.getValue<PlacemarkModel>(PlacemarkModel::class.java) }
        placemarksReady()
      }
    }
    userId = FirebaseAuth.getInstance().currentUser!!.uid
    db = FirebaseDatabase.getInstance().reference
    st = FirebaseStorage.getInstance().reference
    placemarks.clear()
    db.child("users").child(userId).child("placemarks").addListenerForSingleValueEvent(valueEventListener)
  }
}