package baitaplon.babyphoto.screen.register


interface IRegisterContract {
    interface View{
        fun onCheckName(state: RegisterState, message: String)
        fun onNextScreen(state: RegisterState, message: String, account: String)
    }
    interface IPresenter{
        fun checkName(name: String?, name2: String?)
        fun nextScreen(state: RegisterState, firstName: String, lastName: String)
    }
}
