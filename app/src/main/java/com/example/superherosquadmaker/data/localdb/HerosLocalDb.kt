package com.example.superherosquadmaker.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Hero::class, ComicLocal::class], version = 8, exportSchema = false)
abstract class HerosLocalDb : RoomDatabase() {

    abstract val heroesDao: Marveldao


    companion object {

        @Volatile
        private var INSTANCE: HerosLocalDb? = null

        fun getInstance(context: Context): HerosLocalDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HerosLocalDb::class.java,
                        "superheroes_local_db"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}