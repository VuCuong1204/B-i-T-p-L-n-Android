package baitaplon.babyphoto.screen.timeline

import baitaplon.babyphoto.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_preview_time_line.*

class PreviewTimeLineActivity : AppCompatActivity() {
    private var imagePre: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_time_line)
        var bundle: Bundle? = intent.extras
        imagePre = bundle?.getString("image").toString()
        Glide.with(this)
            .load(imagePre)
            .placeholder(R.drawable.image_default)
            .into(ivPreviewTimeLine)
        ibPreviewBack.setOnClickListener {
            finish()
        }
    }
}
