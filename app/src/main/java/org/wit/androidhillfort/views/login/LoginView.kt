package org.wit.androidhillfort.views.login

import android.os.Bundle
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast
import org.wit.androidhillfort.R
import org.wit.androidhillfort.views.BaseView

class LoginView : BaseView() {

  lateinit var presenter: LoginPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    init(toolbar, false)
    progressBar.visibility = ProgressBar.INVISIBLE

    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

    signUp.setOnClickListener {
      val email = email.text.toString()
      val password = password.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      } else {
        presenter.doSignUp(email, password)
      }
    }

    logIn.setOnClickListener {
      val email = email.text.toString()
      val password = password.text.toString()
      if (email == "" || password == "") {
        toast("Please provide email + password")
      } else {
        presenter.doLogin(email, password)
      }
    }
  }

  override fun showProgress() {
    progressBar.visibility = ProgressBar.VISIBLE
  }

  override fun hideProgress() {
    progressBar.visibility = ProgressBar.INVISIBLE
  }
}