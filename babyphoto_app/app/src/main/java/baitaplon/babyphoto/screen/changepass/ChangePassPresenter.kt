package baitaplon.babyphoto.screen.changepass

import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.data.model.ResponseModel
import baitaplon.babyphoto.data.service.APIService
import android.app.Dialog
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassPresenter(private var view: IChangePassContract.View) : IChangePassContract.IPresenter{
    override fun validPassword(oldPass: String, newPass: String, reNewPass: String) {
        view.onCheckNull(ChangePassState.PASS_NULL, "pass is null")
        if (oldPass != Constant.account.password) {
            return view.onCheckPass(ChangePassState.PASS_NOT_FOUND, "pass not found")
        }
        if (!Utils.isValidPassCharacter(newPass) || !Utils.isValidPassCount(newPass)) {
            return view.onCheckNewPass(ChangePassState.PASS_NOT_VALID, "new pass is not valid")
        }
        if (!Utils.isValidPassCharacter(reNewPass) || !Utils.isValidPassCount(reNewPass) || !Utils.isMatchPass
                (newPass, reNewPass)
        ) {
            return view.onCheckReNewPass(ChangePassState.PASS_NOT_MATCH, "re new pass is not match")
        }

        view.onCheckPass(ChangePassState.SUCCESS, "pass found")

        if (oldPass.isNotEmpty() && newPass.isNotEmpty() && reNewPass.isNotEmpty()) {
            return view.onCheckNull(ChangePassState.PASS_NOT_NULL, "pass is null")
        }
    }

    override fun submit(dialog: Dialog, idaccount: Int, pass: String) {
        APIService.base().updatePass(
            idaccount, pass
        ).enqueue(
            object : Callback<ResponseModel<Any>> {
                override fun onResponse(
                    call: Call<ResponseModel<Any>>,
                    response: Response<ResponseModel<Any>>
                ) {
                    Constant.account.password = pass
                    view.onSubmit(
                        ChangePassState.UPDATE_PASS_SUCCESS,
                        "Update password success",
                        dialog
                    )

                    Log.d("TAG", "onResponse: update pass success")
                }

                override fun onFailure(call: Call<ResponseModel<Any>>, t: Throwable) {
                    view.onSubmit(
                        ChangePassState.UPDATE_PASS_FAILED,
                        "Update password failed",
                        dialog
                    )
                    Log.d("TAG", "onFailure: update pass fail")
                }
            }
        )
    }

}
