package baitaplon.babyphoto.screen.home

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.model.AlbumBaby
import baitaplon.babyphoto.screen.timeline.TimelineActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), BabyHomeAdapter.onItemClickListenerr, IHomeContract.View {
    @SuppressLint("WrongViewCast", "NotifyDataSetChanged")
    private lateinit var presenter: HomePresenter
    private var mutableListBaby: MutableList<AlbumBaby> = mutableListOf()

    //callback result
    private var getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            mutableListBaby.clear()
            srlHome.isRefreshing = true
            presenter.getAlbum(Constant.account.idaccount)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        presenter = HomePresenter(this)

        val adapter =
            BabyHomeAdapter(this@HomeActivity, mutableListBaby, R.drawable.ic_add_home_24px)
        adapter.setOnItemClickListener(this)
        rcvHomeViewBaby.adapter = adapter
        val manager = GridLayoutManager(this@HomeActivity, 2, GridLayoutManager.VERTICAL, false)
        rcvHomeViewBaby.layoutManager = manager

        srlHome.isRefreshing = true
        presenter.getAlbum(Constant.account.idaccount)

        srlHome.setOnRefreshListener {
            mutableListBaby.clear()
            srlHome.isRefreshing = true
            presenter.getAlbum(Constant.account.idaccount)
        }
    }

    override fun onItemClick(position: Int) {
        var intent = Intent(this, TimelineActivity::class.java)
        intent.putExtra("idalbum", mutableListBaby[position].idalbum)
        intent.putExtra("nameAlbum", mutableListBaby[position].name)
        intent.putExtra("urlimage", mutableListBaby[position].urlimage)
        intent.putExtra("birthday", mutableListBaby[position].birthday)
        getResult.launch(intent)
    }

    override fun onGetAlbum(state: HomeState, message: String, lAbum: List<AlbumBaby>) {
        srlHome.isRefreshing = false
        when (state) {
            HomeState.SUCCESS -> {
                this.mutableListBaby.addAll(lAbum)
                rcvHomeViewBaby.adapter!!.notifyDataSetChanged()
            }
            HomeState.GET_ALBUM_FAIL -> {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBackPressed() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_back_create_album_layout)
        val window: Window = dialog.window ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttributes = window.attributes
        windowAttributes.gravity = Gravity.CENTER
        window.attributes = windowAttributes
        val btnCancel: Button = dialog.findViewById(R.id.btnDialogBacKCancel)
        val btnOK: Button = dialog.findViewById(R.id.btnDialogBacKOk)
        btnOK.setOnClickListener {
            this.finishAffinity()
        }
        btnCancel.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

}
