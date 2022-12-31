package baitaplon.babyphoto.screen.login

import baitaplon.babyphoto.MainActivity
import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.screen.home.HomeActivity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_account_login.*

class AccountLoginActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener,
    ILoginContract.View {
    private var presenter: AccountLoginPresenter? = null
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var PREF_NAME = "prefs"
    private var KEY_REMEMBER = "remember"
    private var KEY_EMAIL = "email_pref"
    private var KEY_PASS = "pass_pref"
    private var email: String? = ""
    private var enableLogin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_login)
        val bundle: Bundle? = intent.extras
        email = bundle?.getString("Email")
        presenter = AccountLoginPresenter(this)
        presenter?.checkMailNull(email)
        clAccountLoginMain.setOnClickListener {
            hideKeyboard(clAccountLoginMain)
        }

        ibAccountLoginBackHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        tvAccountLoginTakePass.setOnClickListener {
            showBottomSheet()
        }

        acbAccountLogin.setOnClickListener {
            if (enableLogin) {
                val email = tieAccountLoginEmail.text.toString()
                val password = tieAccountLoginPass.text.toString()
                val dialog = Utils.loading(this)
                presenter?.login(dialog, email, password)
            }
        }

        presenter?.checkValidAccount(
            tieAccountLoginEmail.text.toString().trim(),
            tieAccountLoginPass.text.toString().trim(),
        )
        baitaplon.babyphoto.screen.register.MultiTextWatcher()
            .registerEditText(tieAccountLoginEmail)
            .registerEditText(tieAccountLoginPass)
            .setCallback(callback = object :
                baitaplon.babyphoto.screen.register.MultiTextWatcher.TextWatcherWithInstance {
                override fun beforeTextChanged(
                    editText: EditText?,
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    presenter?.checkValidAccount(
                        tieAccountLoginEmail.text.toString(),
                        tieAccountLoginPass.text.toString()
                    )
                }

                override fun onTextChanged(
                    editText: EditText?,
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    presenter?.checkValidAccount(
                        tieAccountLoginEmail.text.toString(),
                        tieAccountLoginPass.text.toString()
                    )
                }

                override fun afterTextChanged(editText: EditText?, editable: Editable?) {
                    presenter?.checkValidAccount(
                        tieAccountLoginEmail.text.toString(),
                        tieAccountLoginPass.text.toString()
                    )
                }

            })
    }


    private fun managePrefs() {
        if (cbAccountLoginRemember.isChecked) {
            editor?.putString(KEY_EMAIL, tieAccountLoginEmail.text.toString().trim())
            editor?.putString(KEY_PASS, tieAccountLoginPass.text.toString().trim())
            editor?.putBoolean(KEY_REMEMBER, true)
            editor?.apply()
        } else {
            editor?.putBoolean(KEY_REMEMBER, false)
            editor?.remove(KEY_EMAIL)
            editor?.remove(KEY_PASS)
            editor?.apply()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        managePrefs()
    }

    override fun onLogin(state: LoginState, message: String) {
        when (state) {
            LoginState.SUCCESS -> {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            LoginState.INVALID_EMAIL_AND_PASS -> {
                tvAccountLoginWarning.visibility = View.VISIBLE
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun onValidAccount(state: LoginState, message: String) {
        tvAccountLoginWarning.visibility = View.INVISIBLE
        when (state) {
            LoginState.SUCCESS -> {
                acbAccountLogin.setBackgroundResource(R.drawable.selector_rec_orange_color_correct_login)
                enableLogin = true
            }
            LoginState.EMAIL_OR_PASS_EMPTY -> {
                acbAccountLogin.setBackgroundResource(R.drawable.selector_rec_gray_incorrect_login)
                enableLogin = false
            }
            else -> {}
        }
    }

    override fun onCheckMailNull(state: LoginState, message: String) {
        when (state) {
            LoginState.MAIL_NULL -> {
                sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                editor = sharedPreferences?.edit()
                cbAccountLoginRemember.isChecked =
                    sharedPreferences!!.getBoolean(KEY_REMEMBER, false)//check is_checked
                tieAccountLoginEmail.setText(sharedPreferences?.getString(KEY_EMAIL, ""))
                tieAccountLoginPass.setText(sharedPreferences?.getString(KEY_PASS, ""))
                cbAccountLoginRemember.setOnCheckedChangeListener(this)
            }
            LoginState.MAIL_NOT_NULL -> {
                tieAccountLoginEmail.setText(email.toString())
                tieAccountLoginPass.setText("")
                sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                editor = sharedPreferences?.edit()
                cbAccountLoginRemember.isChecked =
                    sharedPreferences!!.getBoolean(KEY_REMEMBER, false)//check is_checked
                cbAccountLoginRemember.setOnCheckedChangeListener(this)
//                cbAccountLoginRemember.isChecked =
//                    sharedPreferences!!.getBoolean(KEY_REMEMBER, false)//check is_checked
            }
            else -> {}
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun showBottomSheet(): Dialog {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.password_recovery_bottomsheet_layout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.window?.setGravity(Gravity.BOTTOM)
        return dialog
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
