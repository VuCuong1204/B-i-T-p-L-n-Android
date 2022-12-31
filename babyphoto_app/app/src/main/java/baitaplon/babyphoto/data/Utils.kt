package baitaplon.babyphoto.data

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import baitaplon.babyphoto.R
import java.util.regex.Pattern

object Utils {
    private val REGEX_EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    private val REGEX_NAME = "[a-zA-Z]+"

    fun isEmail(email: String): Boolean {
        return Pattern.matches(REGEX_EMAIL, email)
    }

    fun isValidName(name: String?, name2: String?): Boolean {
        return Pattern.matches("[a-zA-Z0-9]+", name) && Pattern.matches("[a-zA-Z0-9]+", name2)
    }

    fun checkNull(firstName: String?, lastName: String?): Boolean {
        return firstName == "" || lastName == ""
    }

    fun isValidPassCharacter(pass: String): Boolean {
        return Pattern.matches("[a-zA-Z0-9]+", pass)
    }

    fun isValidPassCount(pass: String): Boolean {
        return pass.length in (6..8)
    }

    fun isMatchPass(pass: String, rePass: String): Boolean {
        return Pattern.matches(pass, rePass)
    }

    fun isMatchEmail(email: String, email1: String): Boolean {
        return Pattern.matches(email, email1)
    }

    fun loading(context: Context): Dialog {
        var dialogLoadPass = Dialog(context)
        dialogLoadPass.setContentView(R.layout.dialog_loading_register_layout)
        dialogLoadPass.setCancelable(false)
        dialogLoadPass.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogLoadPass.show()
        return dialogLoadPass
    }
}
