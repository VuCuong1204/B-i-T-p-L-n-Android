package baitaplon.babyphoto.screen.listimage

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.Data
import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.data.service.APIService
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Gravity
import android.view.Window
import android.widget.Button
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListImagePresenter(var mView: IListContract.IView) : IListContract.IPresenter {

    override fun addMultiImageToServer(
        files: MutableList<MultipartBody.Part>,
        idalbum: RequestBody,
        description: RequestBody,
        timeline: RequestBody,
    ) {
        mView.showLoading()
        val dataService = APIService.base()
        val callback = dataService.imageInsertMulti(files, idalbum, description, timeline)
        callback.enqueue(object : Callback<Data<String>> {
            override fun onResponse(
                call: Call<Data<String>>,
                response: Response<Data<String>>
            ) {
                if (response.body()!!.code == "code13") {
                    val data = DataResult<String>()
                    data.state = DataResult.State.SUCCESS
                    data.data = response.body()!!.msg
                    mView.onResult(data)
                    mView.onResult(data)
                    mView.hideLoading()
                } else {
                    val data = DataResult<String>()
                    data.state = DataResult.State.ERROR
                    data.data = response.body()!!.msg
                    mView.onResult(data)
                    mView.hideLoading()
                }
            }

            override fun onFailure(call: Call<Data<String>>, t: Throwable) {
                val data = DataResult<String>()
                data.state = DataResult.State.FAIL
                data.data = t.message
                mView.onResult(data)
                mView.hideLoading()
            }
        })
    }

    override fun addImageSingleToServer(
        singFile: MultipartBody.Part,
        idalbum: RequestBody,
        description: RequestBody,
        timeline: RequestBody
    ) {
        mView.showLoading()
        val dataService = APIService.base()
        val callback = dataService.imageInsertSingle(singFile, idalbum, description, timeline)
        callback.enqueue(object : Callback<Data<String>> {
            override fun onResponse(
                call: Call<Data<String>>,
                response: Response<Data<String>>
            ) {

                if (response.body()!!.code == "code13") {
                    val data = DataResult<String>()
                    data.state = DataResult.State.SUCCESS
                    data.data = response.body()!!.msg
                    mView.onResult(data)
                    mView.hideLoading()
                } else {
                    val data = DataResult<String>()
                    data.state = DataResult.State.ERROR
                    data.data = response.body()!!.msg
                    mView.onResult(data)
                    mView.hideLoading()
                }
            }

            override fun onFailure(call: Call<Data<String>>, t: Throwable) {
                val data = DataResult<String>()
                data.state = DataResult.State.FAIL
                data.data = t.message
                mView.onResult(data)
                mView.hideLoading()
            }
        })
    }

    override fun getImage(mThis: ListImageActivity) {
        var arrayImage: MutableList<String> = ArrayList()
        var arrayCb: MutableList<Boolean> = ArrayList()
        val projection = arrayOf(
            MediaStore.Images.ImageColumns._ID,
            MediaStore.Images.ImageColumns.DISPLAY_NAME,
            MediaStore.Images.ImageColumns.DATA,
        )

        val cursor = mThis.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, null
        )
        if (cursor!!.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val data: String =
                    cursor.getString(2)
                arrayImage.add(data)
                arrayCb.add(false)
                cursor.moveToNext()
            }
            cursor.close()
        }
        mView.getData(arrayImage, arrayCb)
    }

    override fun openBackDialog(mThis: ListImageActivity) {
        val dialog = Dialog(mThis)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_back_create_album_layout)
        val window: Window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes
        val btnCancel: Button = dialog.findViewById(R.id.btnDialogBacKCancel)
        val btnOK: Button = dialog.findViewById(R.id.btnDialogBacKOk)
        btnOK.setOnClickListener {
            mThis.finish()
            dialog.dismiss()
        }
        btnCancel.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }
}
