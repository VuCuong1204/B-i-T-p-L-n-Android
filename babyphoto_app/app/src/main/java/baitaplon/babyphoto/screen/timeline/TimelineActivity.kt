package baitaplon.babyphoto.screen.timeline

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.Image
import baitaplon.babyphoto.screen.home.HomeActivity
import baitaplon.babyphoto.screen.listimage.ListImageActivity
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_timeline.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Suppress("DEPRECATION")
class TimelineActivity : AppCompatActivity(), ITimelineContract.View {
    private lateinit var presenter: TimelinePresenter
    private var idAlbum: String? = "0"
    private var nameAlbum: String? = ""
    private var urlimage: String? = ""
    private var birthday: String? = ""
    private var adapter: TimelineAdapter = TimelineAdapter(this)
    private var hasChange = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_timeline)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabTimeLineAdd)
        presenter = TimelinePresenter(this)

        val bundle: Bundle? = intent.extras
        idAlbum = bundle?.getString("idalbum")
        nameAlbum = bundle?.getString("nameAlbum")
        birthday = bundle?.getString("birthday")
        urlimage = bundle?.getString("urlimage")

        try {
            Glide.with(this)
                .load(urlimage)
                .placeholder(R.drawable.image_default)
                .into(civTimeLineAvatarCirCle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        if (birthday!![1] == '/') birthday = "0$birthday"
        if (birthday!![4] == '/') birthday =
            birthday?.substring(0, 3) + "0" + birthday?.substring(3)
        birthday.hashCode()
        val dt = LocalDate.parse("$birthday", formatter)

        tvTimeLineCountYear.text = (dt.until(LocalDate.now()).years).toString()
        tvTimeLineCountMonth.text = (dt.until(LocalDate.now()).months).toString()
        tvTimeLineCountDay.text = (dt.until(LocalDate.now()).days).toString()

        val format = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        tvTimeLineItemDateStart.text = (dt.format(format)).toString()
        tvTimeLineItemDateEnd.text = (LocalDate.now().format(format)).toString()
        tvTimelineItemTitle.text = nameAlbum.toString()
        var intentPre = Intent(this, PreviewTimeLineActivity::class.java)
        val rvTimelineViewImage: RecyclerView = findViewById(R.id.rvTimelineViewImage)
        val timelineAdapter = adapter

        timelineAdapter.callBack = object : TimelineAdapter.ICallBack {
            override fun onClick(position: Int) {
                intentPre.putExtra("image", timelineAdapter.dataImage[position].urlimage)
                startActivity(intentPre)
            }
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        rvTimelineViewImage.layoutManager = staggeredGridLayoutManager
        rvTimelineViewImage.adapter = timelineAdapter

        srlTimeLine.isRefreshing = true
        presenter.getImage(idAlbum)

        srlTimeLine.setOnRefreshListener {
            adapter.dataImage.clear()
            adapter.notifyDataSetChanged()
            srlTimeLine.isRefreshing = true
            presenter.getImage(idAlbum)
        }
        //callback result
        val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == 200) {
                    adapter.dataImage.clear()
                    adapter.notifyDataSetChanged()
                    srlTimeLine.isRefreshing = true
                    presenter.getImage(idAlbum)
                    hasChange = true
                }
            }

        fabAdd.setOnClickListener {
            var intent = Intent(this, ListImageActivity::class.java)
            intent.putExtra("idalbum", idAlbum)
            getResult.launch(intent)
        }

        ibTimeLineBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetImage(state: TimelineState, message: String, lImage: MutableList<Image>) {
        when (state) {
            TimelineState.SUCCESS -> {
                srlTimeLine.isRefreshing = false
                adapter.dataImage = lImage

//                rvTimelineViewImage.adapter!!.notifyDataSetChanged()

                if (lImage.isEmpty()) return

            }
            TimelineState.GET_IMAGE_FAILED -> {
                Toast.makeText(this, "Get image failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
