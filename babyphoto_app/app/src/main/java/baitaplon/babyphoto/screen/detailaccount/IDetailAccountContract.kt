package baitaplon.babyphoto.screen.detailaccount

import baitaplon.babyphoto.data.model.AccountUpdate

interface IDetailAccountContract {
    interface View {
        fun onUpdateAccount(state: DetailAccountState, message: String)
    }
    interface IPresenter {
        fun updateAccount(account: AccountUpdate)
    }
}
