package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Utils
import baitaplon.babyphoto.data.model.Account
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_enter_email.*

class EnterEmailActivity : AppCompatActivity(), IEnterEmailContract.View {
    private var presenter: EnterEmailPresenter? = null
    private var account: Account? = null
    private var stateCheckMail: RegisterState? = null
    private var enableEnterEmail = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_email)
        val bundle = intent.extras.let {
            it?.apply {
                account = Gson().fromJson(get("account") as String, Account::class.java)
            }
        }
        var tieRegisterEmail:TextInputEditText = findViewById(R.id.tieRegisterEmail)
        presenter = EnterEmailPresenter(this)

        ibRegisterBackEmail.setOnClickListener {
            finish()
        }
        btnRegisterNextEmail.setOnClickListener {
            if (enableEnterEmail){
                val dialog = Utils.loading(this)
                presenter!!.checkEmail(dialog, tieRegisterEmail.text.toString(), account)
            }
        }
        clEnterEmailMain.setOnClickListener {
            hideKeyboard(clEnterEmailMain)
        }
        tieRegisterEmail.addTextChangedListener {
            presenter!!.validatEmail(tieRegisterEmail.text.toString())
        }
    }

    override fun onCheckMail(state: RegisterState, message: String) {
        stateCheckMail = state
        tvRegisterWarningEmail.visibility = View.VISIBLE
        when (state) {
            RegisterState.SUCCESS -> {
                enableEnterEmail = true
                tvRegisterWarningEmail.visibility = View.INVISIBLE
                btnRegisterNextEmail.setBackgroundResource(R.drawable.selector_rec_orange_color)
            }
            RegisterState.EMAIL_EXIST -> {
                enableEnterEmail = false
                tvRegisterWarningEmail.text =
                    "This email is wrong or already exists, please enter a new email"
            }
            RegisterState.IS_NOT_EMAIL -> {
                enableEnterEmail = false
                tvRegisterWarningEmail.text = "This is not email"
            }
            RegisterState.EMAIL_EXIST_OR_NOT_EMAIL -> {
                enableEnterEmail = false
                btnRegisterNextEmail.setBackgroundResource(R.drawable.selector_rec_gray_color_orange_selected)
            }
            else -> {}
        }
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onNextScreen(state: RegisterState, message: String, account: String) {
        when (state) {
            RegisterState.SUCCESS -> {
                val intent = Intent(this, CreatePassActivity::class.java)
                intent.putExtra("account", account)
                startActivity(intent)
            }
            else -> {}
        }
    }
}
