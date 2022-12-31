package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.data.model.Account
import baitaplon.babyphoto.data.service.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreatePassPresenter(private var view: ICreatePassContract.View) :ICreatePassContract.IPresenter{
    private var account: Account? = null


    override fun checkPass(pass: String, rePass: String)  {
        val isValidPassCharacter =
            Utils.isValidPassCharacter(pass) && Utils.isValidPassCharacter(rePass)
        val isValidPassCount = Utils.isValidPassCount(pass) && Utils.isValidPassCount(rePass)
        val isMatchPass = Utils.isMatchPass(pass, rePass)

        if (!isValidPassCharacter)
            return view.onCheckPass(RegisterState.PASS_NOT_VALID, "pass is not valid")

        if (!isValidPassCount)
            return view.onCheckPass(RegisterState.PASS_NOT_VALID, "pass is not valid")

        if (!isMatchPass) return view.onCheckPass(
            RegisterState.PASS_NOT_MATCH,
            "repass is not match"
        )

        view.onCheckPass(RegisterState.SUCCESS, "pass is valid")
    }

    override fun submit(state: RegisterState, pass: String, rePass: String, accountP: Account?) {
        account = accountP
        when (state) {
            RegisterState.SUCCESS -> {
                account?.password = rePass
                APIService.base().insertAccount(
                    account!!.email,
                    account!!.password,
                    account!!.firstname,
                    account!!.lastname
                ).enqueue(
                    object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            view.onInsertAccount(
                                RegisterState.PASS_NOT_MATCH,
                                "pass is not match",
                                ""
                            )
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            view.onInsertAccount(
                                RegisterState.SUCCESS,
                                "pass is match",
                                account!!.email
                            )

                        }
                    }
                )
            }
            else -> {}
        }
    }
}
