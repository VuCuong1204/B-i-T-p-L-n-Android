package baitaplon.babyphoto.screen.createalbum.relation

import baitaplon.babyphoto.R
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RelationAdapter(
    private val dataSet: MutableList<Relation>,
    var iRelation: IRelation
) :
    RecyclerView.Adapter<RelationAdapter.ViewHolder>() {
    var count: Int = 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView
        private val ivCheckBox: ImageView

        init {
            tvName = view.findViewById(R.id.tvRelationName)
            ivCheckBox = view.findViewById(R.id.ivRelationCheckBox)
            itemView.setOnClickListener {
                count++
                if (count == 1) {
                    iRelation.getName(dataSet[adapterPosition].name)
                    tvName.setTextColor(Color.parseColor("#81D600"))
                    ivCheckBox.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.relation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val arrayRelation = dataSet[position]
        holder.tvName.text = arrayRelation.name
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}
