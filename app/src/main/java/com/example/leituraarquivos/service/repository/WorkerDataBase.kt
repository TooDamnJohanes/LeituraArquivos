package com.example.leituraarquivos.service.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.leituraarquivos.service.model.WorkerModel

@Database(entities = [WorkerModel::class], version = 1)
abstract class WorkerDataBase: RoomDatabase(){

    abstract fun WorkerDAO(): WorkerDAO

    companion object {
        private lateinit var INSTANCE: WorkerDataBase

        fun getDatabase(context: Context): WorkerDataBase {

            if(!Companion::INSTANCE.isInitialized){
                synchronized(WorkerDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context, WorkerDataBase::class.java, "workerDB")
                        .build()
                }
            }

            return INSTANCE
        }
    }

}