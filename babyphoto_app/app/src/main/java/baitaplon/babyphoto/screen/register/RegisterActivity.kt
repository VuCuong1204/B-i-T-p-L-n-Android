package baitaplon.babyphoto.screen.register

import baitaplon.babyphoto.MainActivity
import baitaplon.babyphoto.R
import baitaplon.babyphoto.screen.register.MultiTextWatcher.TextWatcherWithInstance
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), IRegisterContract.View {
    private var presenterRegister: RegisterPresenter? = null
    private lateinit var stateCheckName: RegisterState
    private var enableRegister = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val edtRegisterFirstName: AppCompatEditText = findViewById(R.id.edtRegisterFirstName)
        val edtRegisterLastName: AppCompatEditText = findViewById(R.id.edtRegisterLastName)
        val btnRegisterNextFLName: AppCompatButton = findViewById(R.id.btnRegisterNextFLName)

        presenterRegister = RegisterPresenter(this)

        btnRegisterNextFLName.setOnClickListener {
            if (enableRegister){
                presenterRegister!!.nextScreen(
                    stateCheckName,
                    edtRegisterFirstName.text.toString(),
                    edtRegisterLastName.text.toString()
                )
            }
        }
        ibRegisterBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        clRegisterNameMain.setOnClickListener {
            hideKeyboard(clRegisterNameMain)
        }
        MultiTextWatcher()
            .registerEditText(edtRegisterFirstName)
            .registerEditText(edtRegisterLastName)
            .setCallback(callback = object : TextWatcherWithInstance {
                override fun beforeTextChanged(
                    editText: EditText?,
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    editText: EditText?,
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {

                }

                override fun afterTextChanged(editText: EditText?, editable: Editable?) {
                    presenterRegister!!.checkName(
                        edtRegisterFirstName.text.toString(),
                        edtRegisterLastName.text.toString()
                    )
                }

            })
    }

    override fun onCheckName(state: RegisterState, message: String) {
        stateCheckName = state
        when (state) {
            RegisterState.SUCCESS -> {
                enableRegister = true
                btnRegisterNextFLName.setBackgroundResource(R.drawable.selector_rec_orange_color)
                tvRegisterWarning.setTextColor(Color.parseColor("#66000000"))
            }
            RegisterState.NAME_NOT_VALID -> {
                enableRegister = false
                btnRegisterNextFLName.setBackgroundResource(R.drawable.selector_rec_gray_color_orange_selected)
                tvRegisterWarning.setTextColor(Color.parseColor("#FF4B4B"))
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
                enableRegister = true
                val intent = Intent(this, EnterEmailActivity::class.java)
                intent.putExtra("account", account)
                startActivity(intent)
            }
            RegisterState.NAME_NOT_VALID -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }
}

class MultiTextWatcher {
    private var callback: TextWatcherWithInstance? = null
    fun setCallback(callback: TextWatcherWithInstance?): MultiTextWatcher {
        this.callback = callback
        return this
    }

    fun registerEditText(editText: EditText): MultiTextWatcher {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                callback!!.beforeTextChanged(editText, s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                callback!!.onTextChanged(editText, s, start, before, count)
            }

            override fun afterTextChanged(editable: Editable) {
                callback!!.afterTextChanged(editText, editable)
            }
        })
        return this
    }

    interface TextWatcherWithInstance {
        fun beforeTextChanged(
            editText: EditText?,
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        )

        fun onTextChanged(
            editText: EditText?,
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        )

        fun afterTextChanged(editText: EditText?, editable: Editable?)
    }
}

