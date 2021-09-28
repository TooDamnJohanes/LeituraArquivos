package com.example.leituraarquivos.view.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.leituraarquivos.service.model.WorkerModel
import com.example.leituraarquivos.service.repository.WorkerRepository
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

class WorkerFormViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = WorkerRepository(application)
    private val mCompositeDisposable = CompositeDisposable()

    private var mValidate = MutableLiveData<Boolean>()
    var validate: LiveData<Boolean> = mValidate

    private var mGetWorker = MutableLiveData<WorkerModel>()
    var getWorker: LiveData <WorkerModel> = mGetWorker

    private var mWorker = MutableLiveData<WorkerModel>()
    var worker: LiveData <WorkerModel> = mWorker

    @SuppressLint("CheckResult")
    fun save(id: Long, worker: WorkerModel) {
        if(id == 0L) {
            try {
                mRepository.save(worker).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(saveObserver)

            } catch (e: Exception) {
                mValidate.value = false
            }
        } else {
            try {
                mRepository.update(worker).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(updateObserver)

            } catch (e: Exception) {
                mValidate.value = false
            }
        }
    }

    fun loadOne(codFuncionario: Long) {
        val disposable = mRepository.get(codFuncionario).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(getObserver)
        mCompositeDisposable.add(disposable)
    }

    private val getObserver: DisposableObserver<WorkerModel>
        get() =
            object : DisposableObserver<WorkerModel>() {
                override fun onNext(t: WorkerModel) {
                    mGetWorker.value = t
                }

                override fun onError(e: Throwable) {
                    println(e.message.toString())
                }

                override fun onComplete() {
                    println("Complete Getting")
                }

            }

    private val saveObserver: CompletableObserver
        get() =
            object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    println("Salvando o Operador Cadastrado")
                }

                override fun onComplete() {
                    mValidate.value = true
                }

                override fun onError(e: Throwable) {
                    mValidate.value = false
                }

            }

    private val updateObserver: CompletableObserver
        get() =
            object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    mCompositeDisposable.add(d)
                    println("Pegando o Operador Selecionado")
                }

                override fun onComplete() {
                    mValidate.value = true
                }

                override fun onError(e: Throwable) {
                    mValidate.value = false
                }

            }
}