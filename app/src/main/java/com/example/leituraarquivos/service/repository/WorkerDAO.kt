package com.example.leituraarquivos.service.repository

import androidx.room.*
import com.example.leituraarquivos.service.model.WorkerModel
import com.example.leituraarquivos.service.util.WorkerConstants
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface WorkerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(worker: WorkerModel): Completable

    @Update
    fun update(worker: WorkerModel): Completable

    @Query("SELECT * FROM ${WorkerConstants.TABLE.TABLE_NAME}")
    fun load(): Observable<List<WorkerModel>>

    @Query("DELETE FROM worker")
    fun clear(): Completable

    @Query(
        "SELECT * FROM ${WorkerConstants.TABLE.TABLE_NAME} WHERE " +
                "${WorkerConstants.TABLE.COD_FUNC} = :codFuncionario"
    )
    fun get(codFuncionario: Long): Observable<WorkerModel>

}