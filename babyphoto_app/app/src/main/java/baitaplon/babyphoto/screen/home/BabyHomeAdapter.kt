package baitaplon.babyphoto.screen.home

import baitaplon.babyphoto.R
import baitaplon.babyphoto.data.model.AlbumBaby
import baitaplon.babyphoto.screen.createalbum.CreateAlbumActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class BabyHomeAdapter(
    val context: Context,
    private val dataViewBabyHome: MutableList<AlbumBaby> = mutableListOf(),
    private val dataImage: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val viewTypeAdd: Int = 0;
        private const val viewTypeBaby: Int = 1;
    }

    private lateinit var mListener: onItemClickListenerr

    interface onItemClickListenerr {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListenerr) {
        mListener = listener
    }

    //create item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == viewTypeAdd) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_add_baby_layout, parent, false)
            ViewTitle(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_view_baby_layout, parent, false)
            view.setOnClickListener {

            }
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.apply {
            when (holder) {
                is ViewTitle -> holder.bind(dataImage)
                is ViewHolder -> holder.bind(position - 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataViewBabyHome.count() + 1
    }

    inner class ViewTitle(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ibHomeAddBaby: ImageButton = itemView.findViewById(R.id.ibHomeAddBaby)
        fun bind(image: Int) {
            ibHomeAddBaby.setBackgroundResource(image)
            ibHomeAddBaby.setOnClickListener {
                val intent = Intent(context, CreateAlbumActivity::class.java)
                context.startActivity(intent)
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivHomeViewBaby: ImageView = view.findViewById(R.id.ivHomeViewBaby)
        private val tvHomeViewBabyName: TextView = view.findViewById(R.id.tvHomeViewBabyName)
        private val tvHomeViewBabyCountItem: TextView =
            view.findViewById(R.id.tvHomeViewBabyCountItem)
        private val tvHomeViewBabyItem: TextView = view.findViewById(R.id.tvHomeViewBabyItem)
        fun bind(position: Int) {

            Glide.with(context)
                .load(dataViewBabyHome[position].urlimage)
                .placeholder(R.drawable.image_default)
                .into(ivHomeViewBaby)
            tvHomeViewBabyName.text = dataViewBabyHome[position].name
            tvHomeViewBabyCountItem.text = dataViewBabyHome[position].amountimage
            tvHomeViewBabyItem.text = "images"
            itemView.setOnClickListener {
                if (dataViewBabyHome.isNotEmpty()) {
                    if (position != RecyclerView.NO_POSITION)
                        this@BabyHomeAdapter.mListener.onItemClick(position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return viewTypeAdd
        }
        return viewTypeBaby
    }
}
