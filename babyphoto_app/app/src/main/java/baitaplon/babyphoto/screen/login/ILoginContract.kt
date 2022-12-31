package baitaplon.babyphoto.screen.login

import android.app.Dialog

interface ILoginContract {
    interface View {
        fun onLogin(state: LoginState, message: String)
        fun onValidAccount(state: LoginState, message: String)
        fun onCheckMailNull(state: LoginState, message: String)
    }

    interface IPresenter {
        fun login(dialog: Dialog, email: String, password: String)
        fun getIdAccount(dialog: Dialog, email: String, password: String)
        fun getAccountWithID(dialog: Dialog, id: String)
        fun checkMailNull(email: String?)
        fun checkValidAccount(email: String, pass: String)
    }
}
