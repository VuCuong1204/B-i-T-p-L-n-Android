package baitaplon.babyphoto.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AlbumBaby(
    @SerializedName("idalbum")
    @Expose
    var idalbum: String,
    @SerializedName("idaccount")
    @Expose
    var idaccount: String,
    @SerializedName("urlimage")
    @Expose
    var urlimage: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("gender")
    @Expose
    var gender: String,
    @SerializedName("birthday")
    @Expose
    var birthday: String,
    @SerializedName("relation")
    @Expose
    var relation: String,
    @SerializedName("amountimage")
    @Expose
    var amountimage: String
)
