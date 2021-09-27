package com.example.leituraarquivos.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worker")
data class WorkerModel (

    @ColumnInfo(name = "codFuncionario")
    @PrimaryKey
    var codFuncionario: Long = 0,

    @ColumnInfo(name = "descFuncionario")
    var descFuncionario: String = "",

    @ColumnInfo(name = "complemento")
    var complemento: String = "",

    @ColumnInfo(name = "reservado1")
    var reservado1: String = "",

    @ColumnInfo(name = "reservado2")
    var reservado2: String = ""

    )