package org.wit.androidhillfort.views.splashscreen

import android.os.Bundle
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_splash.*
import org.wit.androidhillfort.R
import org.wit.androidhillfort.views.BaseView

class SplashView : BaseView() {

  lateinit var presenter: SplashPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)
    init(toolbar, false)

    presenter = initPresenter(SplashPresenter(this)) as SplashPresenter

    presenter.doSplash()
  }
}