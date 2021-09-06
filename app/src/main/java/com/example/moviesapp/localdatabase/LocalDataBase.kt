package com.example.moviesapp.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviesapp.localdatabase.dao.FavoriteDao
import com.example.moviesapp.ui.models.ItemDetails

@Database(entities = [ItemDetails::class], version = 2, exportSchema = false)
@TypeConverters(Converter::class)
abstract class LocalDataBase() : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var localDataBase: LocalDataBase? = null

        fun getInstance(): LocalDataBase? {
            if (localDataBase == null)
                throw  NullPointerException("database is null");
            return localDataBase
        }

        fun init(context: Context) {
            synchronized(LocalDataBase::class) {
                localDataBase =
                    Room.databaseBuilder(context, LocalDataBase::class.java, "AndroidTaskDB")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
        }
    }

}