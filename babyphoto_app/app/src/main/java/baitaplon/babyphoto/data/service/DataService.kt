package baitaplon.babyphoto.data.service

import baitaplon.babyphoto.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DataService {
    //login
    @FormUrlEncoded
    @POST("account_check_login.php")
    fun login(@Field("email") email: String, @Field("password") password: String): Call<ResponseModel<List<String>>>

    //get account with id
    @FormUrlEncoded
    @POST("account_id.php")
    fun getAccountWithId(@Field("idaccount") idaccount: String): Call<ResponseModel<List<Account>>>

    //thêm mới user
    @FormUrlEncoded
    @POST("account_check_mail.php")
    fun checkEmail(
        @Field("email") email: String,
    ): Call<ResponseModel<List<String>>>

    //thêm mới user
    @FormUrlEncoded
    @POST("account_insert.php")
    fun insertAccount(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String,
    ): Call<String>

    //cập nhật người dùng
    @FormUrlEncoded
    @POST("account_update.php")
    fun updateAccount(
        @Field("email") email: String,
        @Field("firstname") firstName: String,
        @Field("lastname") lastName: String,
        @Field("idaccount") idaccount: Int
    ): Call<ResponseModel<Any>>

    //cập nhật password
    @FormUrlEncoded
    @POST("account_pw.php")
    fun updatePass(
        @Field("idaccount") idaccount: Int,
        @Field("password") password: String
    ): Call<ResponseModel<Any>>

    @FormUrlEncoded
    @POST("album.php")
    fun getAlbum(@Field("idaccount") idaccount: Int): Call<ResponseModel<List<AlbumBaby>>>

    @FormUrlEncoded
    @POST("album_id.php")
    fun getAlbumId(@Field("idalbum") idalbum: Int): Call<ResponseModel<List<AlbumBaby>>>


    //list image
    @FormUrlEncoded
    @POST("image.php")
    fun getImageId(@Field("idalbum") idalbum: Any?): Call<ResponseModel<List<Image>>>

    @Multipart
    @POST("album_insert.php")
    fun albumInsert(
        @Part file: MultipartBody.Part,
        @Part("idaccount") idaccount: RequestBody,
        @Part("name") name: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("birthday") birthday: RequestBody,
        @Part("relation") relation: RequestBody,
    ): Call<Data<String>>


    @Multipart
    @POST("image_insert_multi.php")
    fun imageInsertMulti(
        @Part files: MutableList<MultipartBody.Part>,
        @Part("idalbum") idalbum: RequestBody,
        @Part("description") description: RequestBody,
        @Part("timeline") timeline: RequestBody,
    ): Call<Data<String>>

    @Multipart
    @POST("image_insert_single.php")
    fun imageInsertSingle(
        @Part file: MultipartBody.Part,
        @Part("idalbum") idalbum: RequestBody,
        @Part("description") description: RequestBody,
        @Part("timeline") timeline: RequestBody,
    ): Call<Data<String>>

}
