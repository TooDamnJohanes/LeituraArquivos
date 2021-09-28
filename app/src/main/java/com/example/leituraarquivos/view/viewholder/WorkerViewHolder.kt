package com.example.leituraarquivos.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.leituraarquivos.R
import com.example.leituraarquivos.service.listeners.WorkerListener
import com.example.leituraarquivos.service.model.WorkerModel

class WorkerViewHolder(itemView: View, val listener: WorkerListener) : RecyclerView.ViewHolder(itemView) {

    val mName: TextView = itemView.findViewById(R.id.text_name_worker)
    val mCode: TextView = itemView.findViewById(R.id.text_id_worker)
    val mColuna: ConstraintLayout = itemView.findViewById(R.id.coluna_operador)
    private var mImageWorker: ImageView = itemView.findViewById(R.id.image_worker)

    fun bindData(worker: WorkerModel) {
        mName.text = worker.descFuncionario
        mCode.text = worker.codFuncionario.toString()

        if(worker.complemento == "M" || worker.complemento == "m") {
            mImageWorker.setImageResource(R.drawable.ic_mecanico)
        } else {
            mImageWorker.setImageResource(R.drawable.ic_worker)
        }

        mColuna.setOnClickListener { listener.onListClick(worker.codFuncionario) }
    }

}