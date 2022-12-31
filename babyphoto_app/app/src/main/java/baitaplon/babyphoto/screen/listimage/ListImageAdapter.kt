package baitaplon.babyphoto.screen.listimage

import baitaplon.babyphoto.R
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ListImageAdapter(
    private var dataSet: MutableList<String>,
    private var arrayCb: MutableList<Boolean>,
    private var iListImage: IListImage

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val viewTypeCamera: Int = 0;
        private const val viewTypeImage: Int = 1;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == viewTypeCamera) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_camera, parent, false)
            return ViewCamera(view)
        }
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.image_album_item, parent, false)
        return ViewImage(view)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.apply {
            when (holder) {
                is ViewCamera -> holder.bind()
                is ViewImage -> holder.bind(position - 1)
            }
        }
    }

    inner class ViewCamera(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                iListImage.setCamera()
            }
        }

    }

    inner class ViewImage(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        private val cbImage: ImageView = itemView.findViewById(R.id.ivCheckbox)
        fun bind(position: Int) {
            ivImage.setImageBitmap(BitmapFactory.decodeFile(dataSet[position]))
            itemView.setOnClickListener {
                if (arrayCb[position]) {
                    arrayCb[position] = false
                    cbImage.setImageResource(R.drawable.ic_select_off)
                    iListImage.setImage(position, false)

                } else {
                    arrayCb[position] = true
                    cbImage.setImageResource(R.drawable.ic_select_on)
                    iListImage.setImage(position, true)
                }
            }

            itemView.setOnLongClickListener {
                iListImage.showPreview(dataSet[position])
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return viewTypeCamera
        }
        return viewTypeImage
    }

}
