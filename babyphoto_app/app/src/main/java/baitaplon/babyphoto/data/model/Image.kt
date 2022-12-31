package baitaplon.babyphoto.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("idimage")
    @Expose
    val idimage: Int,
    @SerializedName("idalbum")
    @Expose
    val idalbum: Int,
    @SerializedName("urlimage")
    @Expose
    val urlimage: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("timeline")
    @Expose
    val timeline: String
)
