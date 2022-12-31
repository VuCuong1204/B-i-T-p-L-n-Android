package baitaplon.babyphoto.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("email")
    @Expose
    var email: String,
    @SerializedName("password")
    @Expose
    var password: String,
    @SerializedName("firstname")
    @Expose
    var firstname: String,
    @SerializedName("lastname")
    @Expose
    var lastname: String,
    @SerializedName("idaccount")
    @Expose
    var idaccount: Int
)
