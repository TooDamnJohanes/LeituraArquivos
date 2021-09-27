package com.example.leituraarquivos.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.leituraarquivos.R
import com.example.leituraarquivos.service.listeners.WorkerListener
import com.example.leituraarquivos.service.model.WorkerModel

class WorkerViewHolder(itemView: View, val listener: WorkerListener) : RecyclerView.ViewHolder(itemView) {

    val mName: TextView = itemView.findViewById(R.id.text_name_worker)
    val mCode: TextView = itemView.findViewById(R.id.text_id_worker)
    private var mImageWorker: ImageView = itemView.findViewById(R.id.image_worker)

    fun bindData(worker: WorkerModel) {
        mName.text = worker.descFuncionario
        mCode.text = worker.codFuncionario.toString()

        if(worker.complemento == "M") {
            mImageWorker.setImageResource(R.drawable.ic_baseline_mec)
        }

        mName.setOnClickListener { listener.onListClick(worker.codFuncionario) }
    }

}