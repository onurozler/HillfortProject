package org.wit.androidhillfort.views.placemark


import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.*

class SearchPresenter(view: BaseView) : BasePresenter(view) {


    fun doSearch(sa: String) {
        view?.navigateTo2(VIEW.PLACEMARK, 0, "placemark_search",sa )

    }
}