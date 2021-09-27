package com.example.leituraarquivos.view

import android.annotation.SuppressLint
import android.content.RestrictionEntry.TYPE_NULL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.leituraarquivos.R
import com.example.leituraarquivos.service.constants.WorkerConstants
import com.example.leituraarquivos.service.model.WorkerModel
import com.example.leituraarquivos.view.viewmodel.WorkerFormViewModel
import kotlinx.android.synthetic.main.activity_worker_form.*
import java.lang.Exception

class WorkerFormActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: WorkerFormViewModel
    private var mWorkerId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_worker_form)
        mViewModel = ViewModelProvider(this).get(WorkerFormViewModel::class.java)
        observe()
        listeners()
        loadDataFromActivity()
    }

    private fun loadDataFromActivity() {
        val bundle = intent.extras
        if(bundle != null) {
            mWorkerId = bundle.getLong(WorkerConstants.BUNDLE.WORKER_ID)
            mViewModel.loadOne(mWorkerId)
            form_button_save_files.text = "Atualizar Operador"
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun listeners() {
        mViewModel.validate.observe(this, Observer {
            if(it) {
                Toast.makeText(this, "Operador Cadastrado Com Sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Falha ao Salvar", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.getWorker.observe(this, Observer {
            form_edit_text_codigo.setText(it.codFuncionario.toString())
//            form_edit_text_codigo.isFocusable = false
//            form_edit_text_codigo.setBackgroundColor(R.color.material_on_primary_disabled)
//            form_edit_text_codigo.isFocusableInTouchMode = false
//            form_edit_text_codigo.inputType = TYPE_NULL
            form_edit_text_nome.setText(it.descFuncionario)
            form_edit_text_tipo.setText(it.complemento)
        })
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.form_button_save_files) {
            val codigo = form_edit_text_codigo.text.toString()
            val nome = form_edit_text_nome.text.toString()
            val tipoOp = form_edit_text_tipo.text.toString()
            if(validateData(codigo, nome, tipoOp)) {
                val worker = WorkerModel().apply {
                    this.codFuncionario = codigo.toLong()
                    this.descFuncionario = nome
                    this.complemento = tipoOp
                    this.reservado1 = codigo
                    this.reservado2 = "4"
                }
                mViewModel.save(mWorkerId, worker)
            }
        }
    }

    private fun validateData(codigo: String, nome: String, tipoOp: String): Boolean {
        if (codigo.isEmpty() || nome.isEmpty() || tipoOp.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return false
        } else if(tipoOp != "M" && tipoOp != "O") {
            Toast.makeText(this, "Tipo de Operador precisa ser O ou M", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            codigo.toLong()
        } catch (e: Exception) {
            Toast.makeText(this, "Código Precisa ser apenas Numeros!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun observe() {
        form_button_save_files.setOnClickListener(this)
    }
}