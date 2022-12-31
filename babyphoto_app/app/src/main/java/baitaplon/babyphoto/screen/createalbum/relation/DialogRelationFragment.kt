package baitaplon.babyphoto.screen.createalbum.relation

import baitaplon.babyphoto.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DialogRelationFragment : BottomSheetDialogFragment() {
    private lateinit var rvRelation: RecyclerView
    private lateinit var bottomSheetDialog: BottomSheetDialog
    var position: Int = 0
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_relation_fragment, null)
        bottomSheetDialog.setContentView(view)
        rvRelation = view.findViewById(R.id.rvDialogRelationRelation)
        setRecyclerviewRelation()
        return bottomSheetDialog
    }

    private fun setRecyclerviewRelation() {
        rvRelation.layoutManager = LinearLayoutManager(context)
        var arrayRelation: MutableList<Relation> = arrayListOf(
            Relation("Father"),
            Relation("Mother"),
            Relation("Brother"),
            Relation("Sister"),
            Relation("Aunt"),
            Relation("Uncle"),
            Relation("Grandpa"),
            Relation("Grandma"),
            Relation("Nanny"),
            Relation("Family Friend")
        )
        val iRelation: IRelation = object : IRelation {
            override fun getName(name: String) {
                iCreateName.getName(name)

                Handler().postDelayed(object : Runnable {
                    override fun run() {
                        position = 1
                        bottomSheetDialog.dismiss()
                    }

                }, 300)
            }
        }
        val adapter = RelationAdapter(arrayRelation, iRelation)
        rvRelation.adapter = adapter
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ICreateName) {
            iCreateName = context
        }
    }

    lateinit var iCreateName: ICreateName

    interface ICreateName {
        fun getName(name: String)
    }

}
