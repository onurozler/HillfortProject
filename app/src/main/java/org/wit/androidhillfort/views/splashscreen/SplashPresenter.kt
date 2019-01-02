package org.wit.androidhillfort.views.splashscreen

import android.os.Handler
import org.wit.androidhillfort.views.BasePresenter
import org.wit.androidhillfort.views.BaseView
import org.wit.androidhillfort.views.VIEW


class SplashPresenter(view: BaseView) : BasePresenter(view) {


    fun doSplash()
    {
        Handler().postDelayed({
            view?.hideProgress()
            view?.navigateTo(VIEW.LOGIN)
        }, 3000)
    }

}