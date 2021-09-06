package com.example.dailyforecastapp.Error

import androidx.lifecycle.MutableLiveData

class ErrorLiveData<T> : MutableLiveData<HandleError<T>>() {

    fun postConnectionError(throwable: String?) {
        postValue(HandleError<T>().connectionError(throwable!!))
    }

    fun postError(throwable: String?) {
        postValue(HandleError<T>().error(throwable!!))
    }

}