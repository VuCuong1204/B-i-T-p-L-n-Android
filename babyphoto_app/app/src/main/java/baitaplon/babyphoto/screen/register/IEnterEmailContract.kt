package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.data.model.Account
import android.app.Dialog

interface IEnterEmailContract {
    interface View{
        fun onCheckMail(state: RegisterState, message: String)
        fun onNextScreen(state: RegisterState, message: String, account: String)
    }
    interface IPresenter {
        fun checkEmail(dialog: Dialog, email: String, account: Account?)
        fun validatEmail(email: String)
    }
}
