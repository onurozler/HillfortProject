package org.wit.androidhillfort.views.navigator

import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.content_placemark_map.*
import org.wit.androidhillfort.R
import org.wit.androidhillfort.views.BaseView

lateinit var presenter: NavigatorPresenter
lateinit var map : GoogleMap

class NavigatorView : BaseView(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigator)

        presenter = initPresenter (NavigatorPresenter(this)) as NavigatorPresenter

        mapView.onCreate(savedInstanceState);
    }
}