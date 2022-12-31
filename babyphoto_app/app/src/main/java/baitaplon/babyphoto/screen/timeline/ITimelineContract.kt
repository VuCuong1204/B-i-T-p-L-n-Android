package baitaplon.babyphoto.screen.timeline

import baitaplon.babyphoto.data.model.Image

interface ITimelineContract {
    interface View {
        fun onGetImage(state: TimelineState, message: String, lImage: MutableList<Image>)
    }
    interface IPresenter {
        fun getImage(idalbum: String?)
    }
}
