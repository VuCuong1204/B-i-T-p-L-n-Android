package baitaplon.babyphoto.data.service

import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object APIRetrofitClient {
    private lateinit var retrofit: Retrofit
    fun getClient(base_url: String): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(1000000, TimeUnit.MILLISECONDS)
            .writeTimeout(1000000, TimeUnit.MILLISECONDS)
            .connectTimeout(1000000, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .protocols(Arrays.asList(Protocol.HTTP_1_1))
            .build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit
    }
}
