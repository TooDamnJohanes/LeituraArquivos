package com.example.leituraarquivos.service.repository

import android.content.Context
import com.example.leituraarquivos.service.model.WorkerModel
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WorkerRepository (context: Context) {

    private val mDatabase = WorkerDataBase.getDatabase(context).WorkerDAO()

    fun save(worker: WorkerModel) : Completable {
        return mDatabase.save(worker).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
    }


    fun update(worker: WorkerModel) : Completable {
        return mDatabase.update(worker).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
    }


    fun load() : Observable<List<WorkerModel>> {
        return mDatabase.load().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun clear(): Completable {
        return mDatabase.clear().subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
    }

    fun get(id: Long): Observable<WorkerModel> {
        return mDatabase.get(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

}