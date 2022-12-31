package baitaplon.babyphoto.screen.timeline

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.Image
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class TimelineAdapter(
    private var context: Context
) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {


    var dataImage: MutableList<Image> = mutableListOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    var callBack: ICallBack? = null
    // tạo view hiển thị cho từng item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.timeline_view_album, parent, false)

        return ViewHolder(view)
    }
    //liên kết các item trong danh sách với các view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataImage[position]
        try {
            Glide.with(context)
                .load(item.urlimage)
                .placeholder(R.drawable.image_default)
                .into(holder.ivTimeLineViewImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    //số lượng item
    override fun getItemCount(): Int {
        return dataImage.size
    }
    //ánh xạ các view của item
    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivTimeLineViewImage: ImageView = itemView.findViewById(R.id.ivTimeLineViewImage)

        init {
            itemView.setOnClickListener {
                callBack?.onClick(position)
            }
        }
    }

    interface ICallBack{
        fun onClick(position: Int)
    }
}


