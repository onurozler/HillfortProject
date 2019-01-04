package org.wit.androidhillfort.views.placemark

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

import org.wit.androidhillfort.R
import org.wit.androidhillfort.models.PlacemarkModel
import org.wit.androidhillfort.views.BaseView

class SearchView : BaseView(){

  lateinit var presenter: SearchPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_search)

    presenter = initPresenter (SearchPresenter(this)) as SearchPresenter



    search_hillfort_button.setOnClickListener { presenter.doSearch(search_hillforts.text.toString()) }

  }

}

