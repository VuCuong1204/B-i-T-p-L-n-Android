package baitaplon.babyphoto.screen.home

import baitaplon.babyphoto.data.model.AlbumBaby

interface IHomeContract {
    interface View {
        fun onGetAlbum(state: HomeState, message: String, lAbum: List<AlbumBaby>)
    }
    interface IPresenter {
        fun getAlbum(idaccount: Int?)
    }
}
