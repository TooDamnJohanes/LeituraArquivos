package com.example.leituraarquivos.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.leituraarquivos.R
import com.example.leituraarquivos.service.listeners.WorkerListener
import com.example.leituraarquivos.service.model.WorkerModel
import com.example.leituraarquivos.service.util.WorkersDiffUtil
import com.example.leituraarquivos.view.viewholder.WorkerViewHolder

class WorkerAdapter: RecyclerView.Adapter<WorkerViewHolder>() {

    private var mList: List<WorkerModel> = arrayListOf()
    private lateinit var mListener: WorkerListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.main_fun_item, parent, false)
        return WorkerViewHolder(item, mListener)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun attachListener(listener: WorkerListener) {
        mListener = listener
    }


    fun updateList(list: List<WorkerModel>) {
        val workersDiffUtil = WorkersDiffUtil(mList, list)
        val diffUtilResult = DiffUtil.calculateDiff(workersDiffUtil)
        mList = list
        diffUtilResult.dispatchUpdatesTo(this)
    }

}