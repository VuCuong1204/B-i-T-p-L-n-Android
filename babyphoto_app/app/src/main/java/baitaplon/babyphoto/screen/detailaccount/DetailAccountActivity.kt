package baitaplon.babyphoto.screen.detailaccount


import baitaplon.babyphoto.MainActivity
import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.model.Account
import baitaplon.babyphoto.data.model.AccountUpdate
import baitaplon.babyphoto.screen.changepass.ChangePassActivity
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ContextUtils
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail_account.*


class DetailAccountActivity : AppCompatActivity(), IDetailAccountContract.View {
    private lateinit var presenter: DetailAccountPresenter
    private var account: AccountUpdate = AccountUpdate("", "", "", 0)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_account)
        presenter = DetailAccountPresenter(this)

        onGetAccount()

        detailAcccountMain.setOnClickListener {
            hideKeyboard(detailAcccountMain)
        }

        tvCoppyIDAccount.setOnClickListener {
            coppyClipboardManager()
        }
        ibAccountDetailBack.setOnClickListener {
            finish()
        }
        //edt
        ibAccountDetailFName.setOnClickListener {
            focusText(0)
        }
        ibAccountDetailLName.setOnClickListener {
            focusText(1)
        }
        ibAccountDetailEmail.setOnClickListener {
            focusText(2)
        }

        llAccountDetailSaveChange.setOnClickListener {
            account.email = edtViewEmailAccountDetail.text.toString()
            account.firstname = edtAccountDetailName.text.toString()
            account.lastname = edtAccountDetailNameLast.text.toString()
            account.idaccount = Constant.account.idaccount

            presenter.updateAccount(account)
        }
        llAccountDetailLogout.setOnClickListener {
            openDialog()
        }
        clProfileChangePass.setOnClickListener {
            var intent = Intent(this, ChangePassActivity::class.java)
            startActivity(intent)
        }
//        Snackbar.make(
//            findViewById(R.id.llAccountDetailLogout),
//            ai.ftech.babyphoto.R.string.add_baby,
//            Snackbar.LENGTH_SHORT
//        ).show()

    }

    fun focusText(indexTF: Int) {
        edtAccountDetailName.isFocusable = indexTF == 0
        edtAccountDetailName.isFocusableInTouchMode = indexTF == 0

        edtAccountDetailNameLast.isFocusable = indexTF == 1
        edtAccountDetailNameLast.isFocusableInTouchMode = indexTF == 1

        edtViewEmailAccountDetail.isFocusable = indexTF == 2
        edtViewEmailAccountDetail.isFocusableInTouchMode = indexTF == 2

        when (indexTF) {
            0 -> {
                edtAccountDetailName.setSelection(edtAccountDetailName.text.length)
                edtAccountDetailName.requestFocus()
                showSoftKeyboard(edtAccountDetailName)
            }
            1 -> {
                edtAccountDetailNameLast.setSelection(edtAccountDetailNameLast.text.length)
                edtAccountDetailNameLast.requestFocus()
                showSoftKeyboard(edtAccountDetailNameLast)
            }
            2 -> {
                edtViewEmailAccountDetail.setSelection(edtViewEmailAccountDetail.text.length)
                edtViewEmailAccountDetail.requestFocus()
                showSoftKeyboard(edtViewEmailAccountDetail)
            }
        }
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun onGetAccount() {
        tvViewIDAccountDetail.text = Constant.account.idaccount.toString()
        edtAccountDetailName.setText(Constant.account.firstname)
        edtAccountDetailNameLast.setText(Constant.account.lastname)
        edtViewEmailAccountDetail.setText(Constant.account.email)

    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onUpdateAccount(state: DetailAccountState, message: String) {
        showSnackbar(message)
        when (state) {
            DetailAccountState.SUCCESS -> {

            }
            DetailAccountState.UPDATE_ACCOUNT_FAIL -> {}
            else -> {}
        }
    }

    @SuppressLint("RestrictedApi")
    fun coppyClipboardManager() {
        val stringYouExtracted: String = tvViewIDAccountDetail.text.toString()
        val clipboard =
            ContextUtils.getActivity(this)!!
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", stringYouExtracted)

        clipboard.setPrimaryClip(clip)
        showSnackbar("ID copied")
        Toast.makeText(
            ContextUtils.getActivity(this),
            "Copy coupon code copied to clickboard!",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun showSnackbar(content: String) {
        val mSnackBar = Snackbar.make(this.detailAcccountMain, content, Snackbar.LENGTH_LONG)
            .setActionTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#FECE00"))
            .setTextColor(Color.parseColor("#FFFFFF"))

        val params = mSnackBar.view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        mSnackBar.view.layoutParams = params
        mSnackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
        mSnackBar.show()
    }

    private fun openDialog() {
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_logout_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var tvDialogCancel: TextView = dialog.findViewById(R.id.tvDialogCancel)
        var tvDialogLogout: TextView = dialog.findViewById(R.id.tvDialogLogout)
        tvDialogCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvDialogLogout.setOnClickListener {
            Constant.account = Account("", "", "", "", 0)

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

}
