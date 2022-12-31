package baitaplon.babyphoto.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Album(
    @SerializedName("idalbum")
    @Expose
    var idalbum: Int,
    @SerializedName("idaccount")
    @Expose
    var idaccount: Int,
    @SerializedName("urlimage")
    @Expose
    var urlimage: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("gender")
    @Expose
    var gender: Int,
    @SerializedName("birthday")
    @Expose
    var birthday: String,
    @SerializedName("relation")
    @Expose
    var relation: String,
    @SerializedName("amountimage")
    @Expose
    var amountimage: Int
)
