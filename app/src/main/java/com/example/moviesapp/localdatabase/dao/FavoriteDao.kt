package com.example.moviesapp.localdatabase.dao

import androidx.room.*
import com.example.moviesapp.ui.models.ItemDetails

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(itemDetails: ItemDetails)

    @Query("select * From itemDetails")
    fun getAllFavorites(): List<ItemDetails>

    @Delete
    fun deleteFavorite(itemDetails: ItemDetails?)

}