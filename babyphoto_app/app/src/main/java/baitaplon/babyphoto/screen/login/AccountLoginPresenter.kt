package baitaplon.babyphoto.screen.login

import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.data.model.Account
import baitaplon.babyphoto.data.model.ResponseModel
import baitaplon.babyphoto.data.service.APIService
import android.app.Dialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountLoginPresenter(private var view: ILoginContract.View) : ILoginContract.IPresenter {
    private val apiService = APIService.base()

    override fun login(dialog: Dialog, email: String, password: String) {
        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            view.onLogin(LoginState.EMAIL_OR_PASS_EMPTY, "email, pass is empty")
        } else {
            getIdAccount(dialog, email, password)
        }
    }

    override fun getIdAccount(dialog: Dialog, email: String, password: String) {
        dialog.show()
        apiService.login(email, password).enqueue(
            object : Callback<ResponseModel<List<String>>> {
                override fun onResponse(
                    call: Call<ResponseModel<List<String>>>,
                    response: Response<ResponseModel<List<String>>>
                ) {
                    if (response.body() != null && "code12" == response.body()?.code && response.body()!!.data
                            .isNotEmpty()
                    ) {
                        getAccountWithID(dialog, response.body()!!.data.get(0))
                        return
                    }
                    view.onLogin(LoginState.INVALID_EMAIL_AND_PASS, "get id failed")
                    dialog.dismiss()
                }

                override fun onFailure(call: Call<ResponseModel<List<String>>>, t: Throwable) {
                    view.onLogin(LoginState.INVALID_EMAIL_AND_PASS, "get id failed")
                    dialog.dismiss()
                }

            }
        )
    }
   //save account in object companient Contain
    override fun getAccountWithID(dialog: Dialog, id: String) {
        apiService.getAccountWithId(id).enqueue(
            object : Callback<ResponseModel<List<Account>>> {
                override fun onResponse(
                    call: Call<ResponseModel<List<Account>>>,
                    response: Response<ResponseModel<List<Account>>>
                ) {
                    dialog.dismiss()
                    if (response.body() != null && "code12" == response.body()?.code && response.body()!!.data
                            .isNotEmpty()
                    ) {
                        Constant.account = response.body()!!.data.get(0)
                        view.onLogin(LoginState.SUCCESS, "Login success")
                        return
                    }
                    view.onLogin(LoginState.INVALID_EMAIL_AND_PASS, "Login failed")
                }

                override fun onFailure(call: Call<ResponseModel<List<Account>>>, t: Throwable) {
                    view.onLogin(LoginState.INVALID_EMAIL_AND_PASS, "Login failed")
                    dialog.dismiss()
                }

            }
        )
    }

    override fun checkMailNull(email: String?) {
        if (email == null) {
            view.onCheckMailNull(LoginState.MAIL_NULL, "email is null")
        } else {
            view.onCheckMailNull(LoginState.MAIL_NOT_NULL, "email is not null")
        }
    }

    override fun checkValidAccount(email: String, pass: String) {
        val isValid2 = Utils.checkNull(email, pass)
        if (!isValid2) {
            view.onValidAccount(LoginState.SUCCESS, "email, pass is valid")
        } else {
            view.onValidAccount(
                LoginState.EMAIL_OR_PASS_EMPTY,
                "email, pass is not found or empty"
            )
        }
    }
}
