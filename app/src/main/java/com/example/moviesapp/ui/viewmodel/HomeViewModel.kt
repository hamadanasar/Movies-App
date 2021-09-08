package com.example.moviesapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.apis.ApiManager
import com.example.moviesapp.handleData.ErrorLiveData
import com.example.moviesapp.localdatabase.LocalDataBase
import com.example.moviesapp.ui.models.ItemDetails
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {

    var handleError = ErrorLiveData<ArrayList<ItemDetails>>()

    var selectedMovie = ItemDetails()

    private val favMoviesData: MutableLiveData<ArrayList<ItemDetails>> = MutableLiveData()
    val favMoviesLiveData: LiveData<ArrayList<ItemDetails>> = favMoviesData

    private val apiManager = ApiManager().apis
    private val favouriteDao = LocalDataBase.getInstance()?.favoriteDao()!!

    fun getTopRated() {
        val handler = CoroutineExceptionHandler { _, exception ->
            handleError.postConnectionError(exception.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO).launch(handler) {
            try {
                val response = apiManager.getTopRated(BuildConfig.Apikey)

                if (response!!.isSuccessful) {
                    getAllFavourite(response.body()!!.TopRatedList!!)
                } else {
                    handleError.postError("Something went wrong")
                }
            } catch (e: Exception) {
                handleError.postConnectionError("Internet Connection Failure")
            }
        }
    }

    fun getNowPlaying() {
        val handler = CoroutineExceptionHandler { _, exception ->
            handleError.postConnectionError(exception.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO).launch(handler) {
            try {
                val response = apiManager.getNowPlaying(BuildConfig.Apikey)

                if (response.isSuccessful) {

                    getAllFavourite(response.body()!!.nowPlayingList!!)
                } else {
                    handleError.postConnectionError("Something went wrong")
                }
            } catch (e: Exception) {
                handleError.postConnectionError("Internet Connection Failure")
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

        handleError.postSuccess(allMovies)
    }

}