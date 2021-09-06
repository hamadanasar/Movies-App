package com.example.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dailyforecastapp.Error.ErrorLiveData
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.apis.ApiManager
import com.example.moviesapp.localdatabase.LocalDataBase
import com.example.moviesapp.ui.models.ItemDetails
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    var topRatedHandleError = ErrorLiveData<String>()
    var nowPlayingHandleError = ErrorLiveData<String>()

    var selectedMovie = ItemDetails()

    private val moviesData: MutableLiveData<ArrayList<ItemDetails>> = MutableLiveData()
    val moviesLiveData: LiveData<ArrayList<ItemDetails>> = moviesData

    private val favMoviesData: MutableLiveData<ArrayList<ItemDetails>> = MutableLiveData()
    val favMoviesLiveData: LiveData<ArrayList<ItemDetails>> = favMoviesData

    private val apiManager = ApiManager().apis
    private val favouriteDao = LocalDataBase.getInstance()?.favoriteDao()!!

    fun getTopRated() {
        val handler = CoroutineExceptionHandler { _, exception ->
            topRatedHandleError.postConnectionError(exception.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO).launch(handler) {
            try {
                val response = apiManager.getTopRated(BuildConfig.Apikey)

                if (response!!.isSuccessful) {
                    getAllFavourite(response.body()!!.TopRatedList!!)
                } else {
                    topRatedHandleError.postError("Something went wrong")
                }
            } catch (e: Exception) {
                topRatedHandleError.postConnectionError("Internet Connection Failure")
            }
        }
    }

    fun getNowPlaying() {
        val handler = CoroutineExceptionHandler { _, exception ->
            nowPlayingHandleError.postConnectionError(exception.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO).launch(handler) {
            try {
                val response = apiManager.getNowPlaying(BuildConfig.Apikey)

                if (response.isSuccessful) {

                    getAllFavourite(response.body()!!.nowPlayingList!!)
                } else {
                    nowPlayingHandleError.postConnectionError("Something went wrong")
                }
            } catch (e: Exception) {
                nowPlayingHandleError.postConnectionError("Internet Connection Failure")
            }
        }
    }

    fun addToFavourite(movie: ItemDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                favouriteDao.insertFavorite(movie)
            } catch (e: Exception) {
                Log.d("Exception", "" + e.localizedMessage)
            }
        }
    }

    fun removeFromFavourite(movie: ItemDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                favouriteDao.deleteFavorite(movie)
            } catch (e: Exception) {
                Log.d("Exception", "" + e.localizedMessage)
            }
        }
    }

    private fun getAllFavourite(moviesData: ArrayList<ItemDetails>) {
        favMoviesData.postValue(
            favouriteDao.getAllFavorites() as ArrayList
        )
        filterData(moviesData, favMoviesData.value!!)
    }

    private fun filterData(allMovies: ArrayList<ItemDetails>, fav: ArrayList<ItemDetails>) {
        allMovies.forEachIndexed() { _, prod ->
            if (fav.any { fav -> prod.movieId == fav.movieId }) {
                prod.isFavourite = true
            }
        }
        moviesData.postValue(allMovies)
    }

}