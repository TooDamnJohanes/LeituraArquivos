package com.example.leituraarquivos.view.viewmodel


import android.app.Application
import android.net.Uri
import android.os.Environment
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
    private val mDownloads = application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

    private var resultList = arrayListOf<List<String>>()
    private var worker = WorkerModel()

    private var mValidate = MutableLiveData<Boolean>()
    var validate: LiveData<Boolean> = mValidate

    private lateinit var mAtualizadora: List<WorkerModel>
    private var mWorkerList = MutableLiveData<List<WorkerModel>>()
    var workers: LiveData<List<WorkerModel>> = mWorkerList

    // FUNÇÃO RESPONSÁVEL EM LER OS ARQUIVOS
    @Throws(IOException::class)
    fun readTextFromUri(uri: Uri) {
        mURI = uri
        resultList.clear()
        mRepository.clear()
        contentResolver.openInputStream(mURI)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                var line: String? = reader.readLine()
                while (line != null) {
                    val teste = line.split(";")
                    if (teste.size == 5) {
                        resultList.add(teste)
                    }
                    line = reader.readLine()
                }
            }
        }
        mValidate.value = verifyData()
    }

    private fun verifyData(): Boolean {
        for (i in 0 until resultList.count()) {
            // VERIFICA ID IGUAIS
            for (j in i + 1 until resultList.count()) {
                if (resultList[i][0] == resultList[j][0]) {
                    return false
                }
            }
            // VERIFICA CAMPOS EM NULO
            for (j in 0..4) {
                if (resultList[i][j] == "") {
                    return false
                }
            }
        }
        setData()
        return true
    }

    private fun setData() {
        val COD_FUNCIONARIO = 0
        val DESC_FUNCIONARIO = 1
        val COMPLEMENTO = 2
        val RESERVADO1 = 3
        val RESERVADO2 = 4

        for (i in 0 until resultList.count()) {
            worker = WorkerModel().apply {
                this.codFuncionario = resultList[i][COD_FUNCIONARIO].toLong()
                this.descFuncionario = resultList[i][DESC_FUNCIONARIO]
                this.complemento = resultList[i][COMPLEMENTO]
                this.reservado1 = resultList[i][RESERVADO1]
                this.reservado2 = resultList[i][RESERVADO2]
            }
            mRepository.save(worker).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveObserver)
        }
    }

    fun load() {
        val disposable = mRepository.load().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(loadObserver)
        mCompositeDisposable.add(disposable)
    }

    fun atualizaArquivo() {
        if (::mAtualizadora.isInitialized) {
            updateFile(mAtualizadora)
        }
    }

    private fun updateFile(workers: List<WorkerModel>) {
        if (::mURI.isInitialized) {
            try {
                Completable.create {

                    contentResolver.openFileDescriptor(mURI, "rw")?.use {
                        FileWriter(it.fileDescriptor).use { fileWriter ->
                            workers.forEach {
                                println("${it.codFuncionario};${it.descFuncionario};${it.complemento};${it.reservado1};${it.reservado2}")
                                println("Thread Rodando ${Thread.currentThread().name}")
                                fileWriter.write(
                                    "${it.codFuncionario};${it.descFuncionario};${it.complemento};${it.reservado1};${it.reservado2}\n"
                                )
                            }
                        }
                        it.close()
                    }
                    it.onComplete()
                }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(updateObserver)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private val saveObserver: CompletableObserver
        get() =
            object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    println("Salvando no Banco de Dados")
                }

                override fun onComplete() {
                    mValidate.value = true
                }

                override fun onError(e: Throwable) {
                    mValidate.value = false
                }

            }

    private val loadObserver: DisposableObserver<List<WorkerModel>>
        get() = object : DisposableObserver<List<WorkerModel>>() {
            override fun onNext(t: List<WorkerModel>) {
                mWorkerList.value = t
                mAtualizadora = t
            }

            override fun onError(e: Throwable) {
                mWorkerList.value = arrayListOf()
            }

            override fun onComplete() {
                println("Carregamento Completo AQUI JÃO")
            }

        }

    private val updateObserver: CompletableObserver
        get() =
            object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    mCompositeDisposable.add(d)
                    println("Salvando o arquivo")
                }

                override fun onComplete() {
                    println("Salvo com Sucesso")
                }

                override fun onError(e: Throwable) {
                    println("ERRO AO SALVAR ARQUIVO, SEGUE MENSAGEM")
                    println(e.message.toString())
                }

            }

    fun clearDisposable() {
        mCompositeDisposable.clear()
    }


}
