package baitaplon.babyphoto.screen.changepass

import android.app.Dialog

interface IChangePassContract {
    interface View {
        fun onGetAccountId(state: ChangePassState, message: String)
        fun onCheckPass(state: ChangePassState, message: String)
        fun onCheckNewPass(state: ChangePassState, message: String)
        fun onCheckReNewPass(state: ChangePassState, message: String)
        fun onCheckNull(state: ChangePassState, message: String)
        fun onSubmit(state: ChangePassState, message: String, dialog: Dialog)

    }
    interface IPresenter {
        fun validPassword(oldPass: String, newPass: String, reNewPass: String)
        fun submit(dialog: Dialog, idaccount: Int, pass: String)
    }
}
