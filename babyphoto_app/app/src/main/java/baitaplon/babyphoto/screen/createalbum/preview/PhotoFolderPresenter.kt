package baitaplon.babyphoto.screen.createalbum.preview

import baitaplon.babyphoto.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.provider.MediaStore
import android.view.Gravity
import android.view.Window
import android.widget.Button

class PhotoFolderPresenter(var mView: PhotoFolderActivity) : IPhotoContract.IPresenter {

    override fun openBackDialog(mThis: PhotoFolderActivity) {
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

    override fun getImage(mThis: PhotoFolderActivity) {
        val arrayImage: MutableList<String> = ArrayList()
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
            while (!cursor.isAfterLast()) {
                val data: String =
                    cursor.getString(2)
                arrayImage.add(data)
                cursor.moveToNext()
            }
            cursor.close()
        }
        mView.data(arrayImage)
    }


}
