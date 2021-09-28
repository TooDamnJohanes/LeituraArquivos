package com.example.leituraarquivos

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.leituraarquivos.service.util.WorkerConstants
import com.example.leituraarquivos.service.listeners.WorkerListener

import com.example.leituraarquivos.view.WorkerFormActivity
import com.example.leituraarquivos.view.viewmodel.MainViewModel
import com.example.leituraarquivos.view.adapter.WorkerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mAdapter = WorkerAdapter()
    private lateinit var mListener: WorkerListener
    private lateinit var mViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // 1- OBTER A RECYCLER
        val recycler = findViewById<RecyclerView>(R.id.main_recyclerView_c_func)

        // 2- DEFINIR UM LAYOUT DE COMPORTAMENTO
        recycler.layoutManager = LinearLayoutManager(applicationContext)

        // 3- DEFINIR UM ADAPTER PARA A RECYCLER
        recycler.adapter = mAdapter

        mListener = object : WorkerListener {
            override fun onListClick(codFuncionario: Long) {
                val intent = Intent(applicationContext, WorkerFormActivity::class.java)
                val bundle = Bundle()
                bundle.putLong(WorkerConstants.BUNDLE.WORKER_ID, codFuncionario)
                intent.putExtras(bundle)
                startActivity(intent)
            }

        }
        observe()
        listeners()
    }

    override fun onResume() {
        super.onResume()
        mAdapter.attachListener(mListener)
        mViewModel.load()
        mViewModel.atualizaArquivo()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.clearDisposable()
    }

    private fun listeners() {
        mViewModel.validate.observe(this, Observer {
            if (it) {
                mViewModel.load()
            } else {
                Toast.makeText(this, "Falha ao Importar Cadastros", Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.workers.observe(this, Observer {
            if (it.count() > 0) {
                mAdapter.updateList(it)
            }
        })
    }

    fun observe() {
        main_button_import_files.setOnClickListener(this)
        main_button_input_files.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.main_button_import_files) {
            getFile()
        } else if (id == R.id.main_button_input_files) {
            val intent = Intent(this, WorkerFormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFile(): Boolean {
        return try {
            val intent = Intent().setType("text/plain")
                .setAction(Intent.ACTION_OPEN_DOCUMENT)
            startActivityForResult(Intent.createChooser(intent, "Selecione o Arquivo"), 111)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data

            selectedFile?.also { uri ->
                mViewModel.readTextFromUri(uri)
            }

        } else {
            Toast.makeText(this, "Falha ao Selecionar Arquivo", Toast.LENGTH_SHORT).show()
        }

    }

}