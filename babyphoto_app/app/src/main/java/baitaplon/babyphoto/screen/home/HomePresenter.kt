package baitaplon.babyphoto.screen.home

import baitaplon.babyphoto.data.model.AlbumBaby
import baitaplon.babyphoto.data.model.ResponseModel
import baitaplon.babyphoto.data.service.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomePresenter(private val view: IHomeContract.View) : IHomeContract.IPresenter{
    private val apiService = APIService.base()

    override fun getAlbum(idaccount: Int?) {
        if (idaccount == null) return

        apiService.getAlbum(idaccount).enqueue(
            object : Callback<ResponseModel<List<AlbumBaby>>> {
                override fun onResponse(
                    call: Call<ResponseModel<List<AlbumBaby>>>,
                    response: Response<ResponseModel<List<AlbumBaby>>>
                ) {
                    val res = response.body() as ResponseModel<MutableList<AlbumBaby>>
                    view.onGetAlbum(HomeState.SUCCESS, "Get album success", res.data)
                }

                override fun onFailure(call: Call<ResponseModel<List<AlbumBaby>>>, t: Throwable) {
                    view.onGetAlbum(HomeState.GET_ALBUM_FAIL, "Get album error", ArrayList())
                }

            }
        )
    }
}
