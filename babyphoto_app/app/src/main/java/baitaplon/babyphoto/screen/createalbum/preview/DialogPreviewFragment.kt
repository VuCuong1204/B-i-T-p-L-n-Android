package baitaplon.babyphoto.screen.createalbum.preview


import baitaplon.babyphoto.R
import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DialogPreviewFragment : BottomSheetDialogFragment() {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var ivCancel: ImageView
    private lateinit var ivOk: ImageView
    private lateinit var ivBaby: ImageView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog =
            BottomSheetDialog(requireContext())

        bottomSheetDialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.preview_activity, null)
        bottomSheetDialog.setContentView(view)
        ivCancel = view.findViewById(R.id.ivPreviewCancel)
        ivOk = view.findViewById(R.id.ivPreviewOk)
        ivBaby = view.findViewById(R.id.ivPreviewBaby)

        val urlImage = arguments?.getString("urlImage")
        val status = arguments?.getBoolean("status")
        if (status!!) {
            ivOk.visibility = View.VISIBLE
        } else {
            ivOk.visibility = View.GONE
        }
        ivBaby.setImageBitmap(BitmapFactory.decodeFile(urlImage))
        setOnClick(urlImage)
        return bottomSheetDialog
    }

    private fun setOnClick(pathImage: String?) {
        ivCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        ivOk.setOnClickListener {
            iPreviewUri.getBitmap(pathImage!!)
            bottomSheetDialog.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IPreviewUri) {
            iPreviewUri = context
        }
    }

    private lateinit var iPreviewUri: IPreviewUri

    interface IPreviewUri {
        fun getBitmap(Uri: String)
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}
