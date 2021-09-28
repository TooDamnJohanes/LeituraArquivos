package com.example.leituraarquivos.service.util

import androidx.recyclerview.widget.DiffUtil
import com.example.leituraarquivos.service.model.WorkerModel

class WorkersDiffUtil(
    private val oldList: List<WorkerModel>,
    private val newList: List<WorkerModel>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}