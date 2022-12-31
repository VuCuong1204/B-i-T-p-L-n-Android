package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.data.model.Account
import com.google.gson.Gson
import kotlin.random.Random

class RegisterPresenter(private var view: IRegisterContract.View):IRegisterContract.IPresenter {
    override fun checkName(name: String?, name2: String?) {
        val isName = Utils.isValidName(name, name2)
        val isNull = Utils.checkNull(name, name2)
        if (isName && !isNull) {
            view.onCheckName(RegisterState.SUCCESS, "name is valid")
        } else {
            view.onCheckName(RegisterState.NAME_NOT_VALID, "name is not valid")
        }
    }

    override fun nextScreen(state: RegisterState, firstName: String, lastName: String) {
        when (state) {
            RegisterState.SUCCESS -> {
                val account = Account(
                    "",
                    "",
                    firstName,
                    lastName,
                    Random.nextInt(10000)
                )
                view.onNextScreen(RegisterState.SUCCESS, "name is valid", Gson().toJson(account))
            }
            RegisterState.NAME_NOT_VALID -> {
                view.onNextScreen(RegisterState.NAME_NOT_VALID, "name is not valid", "")
            }
            else -> {}
        }
    }
}
