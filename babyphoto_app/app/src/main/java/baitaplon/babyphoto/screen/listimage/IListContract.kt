package baitaplon.babyphoto.screen.listimage

import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.data.model.IBaseView
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IListContract {
    interface IView : IBaseView {
        fun onResult(data: DataResult<String>)
        fun getData(arrayImage: MutableList<String>, arrayCb: MutableList<Boolean>)
    }

    interface IPresenter {
        fun addMultiImageToServer(
            files: MutableList<MultipartBody.Part>,
            idalbum: RequestBody,
            description: RequestBody,
            timeline: RequestBody,
        )

        fun addImageSingleToServer(
            singFile: MultipartBody.Part,
            idalbum: RequestBody,
            description: RequestBody,
            timeline: RequestBody,
        )

        fun getImage(mThis: ListImageActivity)
        fun openBackDialog(mThis: ListImageActivity)
    }
}
