package org.wit.androidhillfort.views.signup

import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.toast
import org.wit.androidhillfort.models.firebase.PlacemarkFireStore
import org.wit.androidhillfort.views.BasePresenter
import org.wit.androidhillfort.views.BaseView
import org.wit.androidhillfort.views.VIEW

class SignupPresenter(view: BaseView) : BasePresenter(view) {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun doSignUp(email: String, password: String) {
        view?.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
            if (task.isSuccessful) {
                view?.hideProgress()
                view?.navigateTo(VIEW.LIST)
            } else {
                view?.hideProgress()
                view?.toast("Sign Up Failed: ${task.exception?.message}")
            }
        }
    }
}
