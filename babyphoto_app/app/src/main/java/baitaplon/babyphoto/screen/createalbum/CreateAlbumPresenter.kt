package baitaplon.babyphoto.screen.createalbum

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.Data
import baitaplon.babyphoto.data.model.DataResult
import baitaplon.babyphoto.data.service.APIService
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class CreateAlbumPresenter(var mView: ICreateContract.IView) : ICreateContract.IPresenter {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun createAlbum(
        singleFile: MultipartBody.Part,
        rqIdaccount: RequestBody, rqName: RequestBody,
        rqGender: RequestBody, rqBirthday: RequestBody,
        rqRelation: RequestBody
    ) {
        mView.showLoading()
        val dataService = APIService.base()
        val callback = dataService.albumInsert(
            singleFile,
            rqIdaccount,
            rqName,
            rqGender,
            rqBirthday,
            rqRelation
        )

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

    override fun openBackDialog(mThis: CreateAlbumActivity) {
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

    override fun getBirthdayAlbum(mThis: CreateAlbumActivity) {
        val calendar = Calendar.getInstance()
        var time: String? = ""
        calendar.timeInMillis = System.currentTimeMillis()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog =
            DatePickerDialog(mThis, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?,
                    yearNew: Int,
                    monthNew: Int,
                    dayOfMonthNew: Int
                ) {
                    if (yearNew <= year) {
                        if (monthNew < month) {
                            time = "${dayOfMonthNew}/${monthNew + 1}/${yearNew}"
                            mView.getData(time!!)
                        } else {
                            if (monthNew == month) {
                                if (dayOfMonthNew <= dayOfMonth) {
                                    time = "${dayOfMonthNew}/${monthNew + 1}/${yearNew}"
                                    mView.getData(time!!)
                                } else {
                                    Toast.makeText(
                                        mThis.applicationContext,
                                        "Date of birth cannot be greater than current date",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                Toast.makeText(
                                    mThis.applicationContext,
                                    "Date of birth cannot be greater than current date",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            mThis.applicationContext,
                            "Date of birth cannot be greater than current date",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }, year, month, dayOfMonth)
        datePickerDialog.setOnCancelListener {
            if (time != "") {
                mView.getData(time!!)
            }
        }
        datePickerDialog.setOnDismissListener {
            if (time != "") {
                mView.getData(time!!)
            }
        }
        datePickerDialog.show()
    }


}

