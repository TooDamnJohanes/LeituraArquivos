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
import com.example.leituraarquivos.service.util.WorkerConstants
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
                finish()
            } else {
                Toast.makeText(this, "Falha ao Salvar", Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.getWorker.observe(this, Observer {
            edit_cod_operador.setText(it.codFuncionario.toString())
            edit_cod_operador.isFocusable = false
            edit_cod_operador.setBackgroundColor(R.color.material_on_primary_disabled)
            edit_cod_operador.isFocusableInTouchMode = false
            edit_cod_operador.inputType = TYPE_NULL

            edit_nome_operador.setText(it.descFuncionario)
            edit_tipo_operador.setText(it.complemento)

            edit_reservado1.setText(it.reservado1)
            edit_reservado1.isFocusable = false
            edit_reservado1.setBackgroundColor(R.color.material_on_primary_disabled)
            edit_reservado1.isFocusableInTouchMode = false
            edit_reservado1.inputType = TYPE_NULL

            edit_reservado2.setText(it.reservado2)
            edit_reservado2.isFocusable = false
            edit_reservado2.setBackgroundColor(R.color.material_on_primary_disabled)
            edit_reservado2.isFocusableInTouchMode = false
            edit_reservado2.inputType = TYPE_NULL
        })
    }

    override fun onClick(view: View) {
        val id = view.id
        if(id == R.id.form_button_save_files) {
            val codigo = edit_cod_operador.text.toString()
            val nome = edit_nome_operador.text.toString()
            val tipoOp = edit_tipo_operador.text.toString()
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
        } else if(tipoOp != "M" && tipoOp != "O" && tipoOp != "m" && tipoOp != "o") {
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