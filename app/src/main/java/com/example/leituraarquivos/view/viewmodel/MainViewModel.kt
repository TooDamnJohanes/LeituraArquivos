package com.example.leituraarquivos.view.viewmodel

import android.app.Application
import android.net.Uri
import android.provider.DocumentsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.leituraarquivos.service.model.WorkerModel
import com.example.leituraarquivos.service.repository.WorkerRepository
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.io.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = WorkerRepository(application)
    private lateinit var mURI: Uri
    private val mCompositeDisposable = CompositeDisposable()

    private val contentResolver = application.contentResolver

    private var resultList = arrayListOf<List<String>>()
    private var worker = WorkerModel()

    private var mValidate = MutableLiveData<Boolean>()
    var validate: LiveData<Boolean> = mValidate

    private var mValidateSave = MutableLiveData<Boolean>()
    var validateSave: LiveData<Boolean> = mValidateSave

    private var mWorkerList = MutableLiveData<List<WorkerModel>>()
    var workers: LiveData<List<WorkerModel>> = mWorkerList

    // FUNÇÃO RESPONSÁVEL EM LER OS ARQUIVOS
    @Throws(IOException::class)
    fun readTextFromUri(uri: Uri) {
        mURI = uri
        mRepository.clear()
        resultList.clear()
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    val teste = line.split(";")
                    resultList.add(teste)
                    line = reader.readLine()
                }
            }
        }
        mValidate.value = verifyData()
    }

    private fun verifyData(): Boolean {
        setData()
        return true
    }

    private fun setData() {
        for (i in 0 until resultList.count()) {
            worker = WorkerModel().apply {
                this.codFuncionario = resultList[i][0].toLong()
                this.descFuncionario = resultList[i][1]
                this.complemento = resultList[i][2]
                this.reservado1 = resultList[i][3]
                this.reservado2 = resultList[i][4]
            }
            mRepository.save(worker).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveObserver)
        }
    }

    private val saveObserver: CompletableObserver
        get() =
            object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    println("Teste")
                }

                override fun onComplete() {
                    mValidateSave.value = true
                }

                override fun onError(e: Throwable) {
                    mValidateSave.value = false
                }

            }

    fun load() {
        val disposable = mRepository.load().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(loadObserver)
        mCompositeDisposable.add(disposable)
    }

    private val loadObserver: DisposableObserver<List<WorkerModel>>
        get() = object: DisposableObserver<List<WorkerModel>>() {
            override fun onNext(t: List<WorkerModel>) {
                mWorkerList.value = t
                mWorkerList.value?.let { updateFile(it) }
            }

            override fun onError(e: Throwable) {
                mWorkerList.value = arrayListOf()
            }

            override fun onComplete() {
                println("Complete")
            }

        }

    fun updateFile(workers: List<WorkerModel>) {
        if (::mURI.isInitialized) {
            try {
                Observable.create<Boolean>{contentResolver.openFileDescriptor(mURI, "w")?.use {
                    FileWriter(it.fileDescriptor).use { fileWriter ->
                        workers.forEach {
                            println("${it.codFuncionario};${it.descFuncionario};${it.complemento};${it.reservado1};${it.reservado2}")
                            println("Thread Rodando ${Thread.currentThread().name}")
                            fileWriter.write(
                                "${it.codFuncionario};${it.descFuncionario};${it.complemento};${it.reservado1};${it.reservado2}\n"
                            )
                        }
                    }
                }}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }


}
