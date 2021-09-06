package com.example.moviesapp.localdatabase

import androidx.room.TypeConverter
import com.example.moviesapp.ui.models.ItemDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converter {

    @TypeConverter
    fun fromListToString(weather: List<ItemDetails>): String? {
        val gson = Gson()
        return gson.toJson(weather)
    }

    @TypeConverter
    fun fromStringToList(string: String?): List<ItemDetails> {
        val listType: Type = object : TypeToken<ItemDetails>() {}.type
        return Gson().fromJson(string, listType)
    }

    @TypeConverter
    fun fromIntListToString(weather: List<Int>): String? {
        val gson = Gson()
        return gson.toJson(weather)
    }

    @TypeConverter
    fun fromStringToIntList(string: String?): List<Int> {
        val listType: Type = object : TypeToken<Int>() {}.type
        return Gson().fromJson(string, listType)
    }

}