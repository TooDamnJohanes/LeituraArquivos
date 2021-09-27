package com.example.leituraarquivos.service

import android.content.Context

class SecurityPreferences(context: Context) {

    private val mSharedPreferences =
        context.getSharedPreferences("uri", Context.MODE_PRIVATE)

    fun storeString(key: String, value: String) {
        // EDITO O VALOR DE UMA SHARED PREFERENCE, BEM PARECIDO COM O QUE FAZEMOS COM DICIONÁRIOS
        mSharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        // AO INVOCAR ESSA FUNÇÃO, EU ESTOU PEGANDO EM UM SHARED PREFERENCE, O VALOR QUE ESTÁ
        // ARMAZENADO DENTRO DA MINHA CHAVE, QUE RECEBO POR PARAMETRO
        return mSharedPreferences.getString(key, "") ?: ""
    }
}