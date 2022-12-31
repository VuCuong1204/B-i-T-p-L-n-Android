package baitaplon.babyphoto.screen.createalbum

import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.data.model.IBaseView
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface ICreateContract {
    interface IView : IBaseView {
        fun onResult(data: DataResult<String>)
        fun getData(time: String)
    }

    interface IPresenter {

        fun createAlbum(
            singleFile: MultipartBody.Part,
            rqIdaccount: RequestBody, rqName: RequestBody,
            rqGender: RequestBody, rqBirthday: RequestBody,
            rqRelation: RequestBody
        )

        fun openBackDialog(mThis: CreateAlbumActivity)
        fun getBirthdayAlbum(mThis: CreateAlbumActivity)
    }
}
