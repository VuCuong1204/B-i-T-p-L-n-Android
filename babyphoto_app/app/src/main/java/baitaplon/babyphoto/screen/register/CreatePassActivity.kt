package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.Account
import baitaplon.babyphoto.screen.login.AccountLoginActivity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_create_pass.*

class CreatePassActivity() : AppCompatActivity(), ICreatePassContract.View {
    private var account: Account? = null
    private var presenter: CreatePassPresenter? = null
    private lateinit var stateCheckRePass: RegisterState
    private var enableCreatePass = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pass)
        val bundle = intent.extras?.let {
            it.apply {
                account = Gson().fromJson(get("account") as String, Account::class.java)
            }
        }
        presenter = CreatePassPresenter(this)


        ibRegisterBackCreatePass.setOnClickListener {
            finish()
        }
        //check pass
        tieRegisterPass.addTextChangedListener {
            presenter!!.checkPass(
                tieRegisterPass.text.toString().trim(),
                tieRegisterRePass.text.toString().trim()
            )
        }
        //check repass
        tieRegisterRePass.addTextChangedListener {
            presenter!!.checkPass(
                tieRegisterPass.text.toString().trim(),
                tieRegisterRePass.text.toString().trim()
            )
        }
        btnRegisterNextPass.setOnClickListener {

            if (enableCreatePass){
                presenter!!.submit(
                    stateCheckRePass,
                    tieRegisterPass.text.toString().trim(),
                    tieRegisterRePass.text.toString().trim(),
                    account
                )
                openDialog()
            }
        }
        clCreatePassMain.setOnClickListener {
            hideKeyboard(clCreatePassMain)
        }
    }


    override fun onInsertAccount(
        state: RegisterState,
        message: String,
        email: String,
    ) {
        val dialog = openDialog()
        when (state) {
            RegisterState.SUCCESS -> {
                dialog.dismiss()
                val intent = Intent(this, AccountLoginActivity::class.java)
                intent.putExtra("Email", email)
                startActivity(intent)
                Log.d("TAG", "onInsertAccount: ${email}")
            }
            RegisterState.PASS_NOT_MATCH -> {
                dialog.dismiss()
                Toast.makeText(this, "Insert failed", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {}
        }
    }

    override fun onCheckPass(state: RegisterState, message: String) {
        stateCheckRePass = state
        tvRegisterWarningPass.setTextColor(Color.parseColor("#DC143C"))
        btnRegisterNextPass.setBackgroundResource(R.drawable.selector_rec_gray_color_orange_selected)
        when (state) {
            RegisterState.SUCCESS -> {
                enableCreatePass = true
                tvRegisterWarningPass.setTextColor(Color.parseColor("#FFFFFF"))
                btnRegisterNextPass.setBackgroundResource(R.drawable.selector_rec_orange_color)
            }
            RegisterState.PASS_NOT_VALID -> {
                enableCreatePass = false
                tvRegisterWarningPass.text =
                    "Passwords are between 6 and 8 characters, do not use special characters"

            }
            RegisterState.PASS_NOT_MATCH -> {
                enableCreatePass = false
                tvRegisterWarningPass.text = "Confirm password doesn't match"
            }
            else -> {}
        }

    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun openDialog(): Dialog {
        var dialogLoadPass = Dialog(this)
        dialogLoadPass.setContentView(R.layout.dialog_loading_register_layout)
        dialogLoadPass.setCancelable(false)
        dialogLoadPass.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.btnRegisterNextPass.setOnClickListener {
            dialogLoadPass.dismiss()
        }
        dialogLoadPass.show()
        return dialogLoadPass
    }
}
