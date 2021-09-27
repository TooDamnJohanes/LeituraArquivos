package com.example.leituraarquivos.service.repository

import androidx.room.*
import com.example.leituraarquivos.service.model.WorkerModel
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface WorkerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(worker: WorkerModel) : Completable

    @Update
    fun update(worker: WorkerModel) : Completable

    @Query("SELECT * FROM worker")
    fun load() : Observable<List<WorkerModel>>

    @Query("DELETE FROM worker")
    fun clear(): Completable

    @Query("SELECT * FROM worker WHERE codFuncionario = :codFuncionario")
    fun get(codFuncionario: Long): Observable<WorkerModel>

}