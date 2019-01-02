package org.wit.androidhillfort.views.login

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_signup.*
import org.jetbrains.anko.toast
import org.wit.androidhillfort.R
import org.wit.androidhillfort.views.BaseView
import org.wit.androidhillfort.views.signup.SignupPresenter

class SignupView : BaseView() {

  lateinit var presenter: SignupPresenter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_signup)
    init(toolbar, false)

    presenter = initPresenter(SignupPresenter(this)) as SignupPresenter


  signUp.setOnClickListener {
    val email = email.text.toString()
    val password = password.text.toString()
    val password2 = password2.text.toString()

    if (email == "" || password == "") {
      toast("Please provide email + password")
    }
    else if (password != password2){
      toast("Enter passwords correctly")
    }
    else {
      presenter.doSignUp(email, password)
    }
    }
  }


}