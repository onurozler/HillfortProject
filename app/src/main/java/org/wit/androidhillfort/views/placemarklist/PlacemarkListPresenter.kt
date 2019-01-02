package org.wit.androidhillfort.views.placemarklist

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.BasePresenter
import org.wit.androidhillfort.views.BaseView
import org.wit.androidhillfort.views.VIEW

class PlacemarkListPresenter(view: BaseView) : BasePresenter(view) {

  fun doAddPlacemark() {
    view?.navigateTo(VIEW.PLACEMARK)
  }

  fun doEditPlacemark(placemark: PlacemarkModel) {
    view?.navigateTo(VIEW.PLACEMARK, 0, "placemark_edit", placemark)
  }

  fun doShowPlacemarksMap() {
    view?.navigateTo(VIEW.MAPS)
  }

  fun loadPlacemarks() {
    async(UI) {
      view?.showPlacemarks(app.placemarks.findAll())
    }
  }

  fun doLogout() {
    FirebaseAuth.getInstance().signOut()
    app.placemarks.clear()
    view?.navigateTo(VIEW.LOGIN)
  }
}