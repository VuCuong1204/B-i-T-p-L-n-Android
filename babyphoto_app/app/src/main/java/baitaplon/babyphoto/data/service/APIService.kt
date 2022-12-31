package baitaplon.babyphoto.data.service

class APIService {
    companion object {
          private val BASE_URL = "https://vuquoccuong.000webhostapp.com/ServerHelloBaby/"
        fun base(): DataService {
            return APIRetrofitClient.getClient(BASE_URL).create(DataService::class.java)
        }
    }
}
