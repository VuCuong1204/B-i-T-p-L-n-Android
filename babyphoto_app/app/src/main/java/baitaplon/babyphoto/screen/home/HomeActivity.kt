package baitaplon.babyphoto.screen.home

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.Constant
import baitaplon.babyphoto.data.model.AlbumBaby
import baitaplon.babyphoto.screen.detailaccount.DetailAccountActivity
import baitaplon.babyphoto.screen.timeline.TimelineActivity
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nv_home_header_layout.view.*

class HomeActivity : AppCompatActivity(), BabyHomeAdapter.onItemClickListenerr, IHomeContract.View {
    @SuppressLint("WrongViewCast", "NotifyDataSetChanged")
    private lateinit var presenter: HomePresenter
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
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
        drawerLayout = findViewById(R.id.drawableLayout)
        navigationView = findViewById(R.id.nvHomeToDetailAccount)


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

        //xử lý sự kiện đóng mở sử dụng ActionBarDrawer
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        //lắng nghe sự kiện đóng mở khi ấn nút
        drawerLayout.addDrawerListener(toggle)
        ibHomeMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
            nvHomeToDetailAccount.getHeaderView(0).tvHeaderNameAccount.text =
                Constant.account.firstname + " " +
                        Constant.account.lastname
        }
        navigationView.setNavigationItemSelectedListener {
            var intent = Intent(this, DetailAccountActivity::class.java)
            when (it.itemId) {
                R.id.itemAcc -> startActivity(intent)
                R.id.itemNoti -> Toast.makeText(
                    applicationContext,
                    "Click on Notifications",
                    Toast.LENGTH_SHORT
                ).show()
                R.id.itemSto -> Toast.makeText(
                    applicationContext,
                    "Click on Storate",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }


        //ads
        var arrayImage = mutableListOf(
            R.drawable.image16, R.drawable.image17,
            R.drawable.image13, R.drawable.image14, R.drawable.image15
        )
        for (image in arrayImage) {
            flipperImage(image)
        }
    }

    private fun flipperImage(image: Int) {
        val ivHomeSlide = ImageView(this)
        ivHomeSlide.setBackgroundResource(image)
        vlHomeSlide.addView(ivHomeSlide)
        vlHomeSlide.flipInterval = 5000
        vlHomeSlide.setInAnimation(this, R.anim.slide_right)
        vlHomeSlide.setOutAnimation(this, R.anim.slide_left)
        vlHomeSlide.isAutoStart = true
    }

    override fun onItemClick(position: Int) {
        var intent = Intent(this, TimelineActivity::class.java)
        intent.putExtra("idalbum", mutableListBaby[position].idalbum)
        intent.putExtra("nameAlbum", mutableListBaby[position].name)
        intent.putExtra("urlimage", mutableListBaby[position].urlimage)
        intent.putExtra("birthday", mutableListBaby[position].birthday)
        getResult.launch(intent)
    }

    //khi ấn button sẽ gọi tới hàm này -> cần gọi để view hide drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            true
        }
        return super.onOptionsItemSelected(item)
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
