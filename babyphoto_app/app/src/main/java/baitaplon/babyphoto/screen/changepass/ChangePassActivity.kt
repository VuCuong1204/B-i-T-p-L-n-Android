package baitaplon.babyphoto.screen.changepass

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Constant
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_change_pass.*

class ChangePassActivity : AppCompatActivity(), IChangePassContract.View {
    private lateinit var presenter: ChangePassPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pass)

        presenter = ChangePassPresenter(this)

        tieCurrentPass.addTextChangedListener {
            presenter.validPassword(
                tieCurrentPass.text.toString(),
                tieNewPass.text.toString(),
                tieReNewPass.text.toString(),
            )
        }
        tieNewPass!!.addTextChangedListener {
            presenter.validPassword(
                tieCurrentPass.text.toString(),
                tieNewPass.text.toString(),
                tieReNewPass.text.toString(),
            )
        }
        tieReNewPass!!.addTextChangedListener {
            presenter.validPassword(
                tieCurrentPass.text.toString(),
                tieNewPass.text.toString(),
                tieReNewPass.text.toString(),
            )
        }
        tvSaveChange.setOnClickListener {
            val dialog = openDialog()
            presenter.submit(dialog, Constant.account.idaccount, tieNewPass.text.toString())
        }
        ibChangePassBack.setOnClickListener {
            finish()
        }

    }

    override fun onGetAccountId(state: ChangePassState, message: String) {
        when (state) {
            ChangePassState.GET_PASS_FAILED -> {
                Toast.makeText(this, "Get Account failed", Toast.LENGTH_SHORT).show()
            }
            ChangePassState.SUCCESS -> {
                Toast.makeText(this, "Get Account success", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onCheckPass(state: ChangePassState, message: String) {
        when (state) {
            ChangePassState.PASS_NOT_FOUND -> {
                tvChangePassWarning.text = "You password was incorrect."
                tvChangePassWarning.setTextColor(Color.parseColor("#FF4B4B"))
            }
            ChangePassState.SUCCESS -> {
                tvChangePassWarning.text = ""
            }
            else -> {}
        }
    }

    override fun onCheckNewPass(state: ChangePassState, message: String) {
        when (state) {
            ChangePassState.PASS_NOT_VALID -> {
                tvChangePassWarning.text =
                    "Passwords are between 6 and 8 characters, do not use special characters"
                tvChangePassWarning.setTextColor(Color.parseColor("#FF4B4B"))
            }
            ChangePassState.PASS_VALID -> {
                tvChangePassWarning.text = ""
            }
            else -> {}
        }
    }

    override fun onCheckReNewPass(state: ChangePassState, message: String) {
        when (state) {
            ChangePassState.PASS_NOT_MATCH -> {
                tvChangePassWarning.text = "Confirm password doesn't match"
                tvChangePassWarning.setTextColor(Color.parseColor("#FF4B4B"))
            }
            ChangePassState.PASS_MATCH -> {
                tvChangePassWarning.text = ""
            }
            else -> {}
        }
    }

    override fun onCheckNull(state: ChangePassState, message: String) {
        when (state) {
            ChangePassState.PASS_NOT_NULL -> {
                tvSaveChange.text = "Save"
                tvSaveChange.visibility = View.VISIBLE
            }
            ChangePassState.PASS_NULL -> {
                tvSaveChange.text = ""
                tvSaveChange.visibility = View.INVISIBLE
            }
            else -> {}
        }
    }

    override fun onSubmit(state: ChangePassState, message: String, dialog: Dialog) {
        dialog.dismiss()
        when (state) {
            ChangePassState.UPDATE_PASS_SUCCESS -> {
                finish()
            }
            ChangePassState.UPDATE_PASS_FAILED -> {
                Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }

    }

    private fun openDialog(): Dialog {
        var dialogLoadPass = Dialog(this)
        dialogLoadPass.setContentView(R.layout.dialog_loading_register_layout)
        dialogLoadPass.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoadPass.show()
        return dialogLoadPass
    }

    //    fun backScreen(){
//        view.finish()
//    }
    fun showSnackbar() {
        val mSnackBar = Snackbar.make(
            this.ctChangePassMain,
            "Your new password has updated",
            Snackbar.LENGTH_LONG
        )
//        mSnackBar.setAction("close", View.OnClickListener {
//
//        })
            .setActionTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#FECE00"))
            .setTextColor(Color.parseColor("#FFFFFF"))

        val params = mSnackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        mSnackBar.view.layoutParams = params
        mSnackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        mSnackBar.show()
    }
}
