package com.example.dailyforecastapp.Error

class HandleError<T> {

    private var status: DataStatus? = null

    private var connectionError: String? = null
    private var error: String? = null

    fun connectionError(error: String): HandleError<T> {
        status = DataStatus.CONNECTIONERROR
        this.connectionError = error

        return this
    }

    fun error(error: String): HandleError<T> {
        status = DataStatus.ERROR
        this.error = error
        this.connectionError = null
        return this
    }

    fun getStatus(): DataStatus {
        return status!!
    }

    fun getError(): String? {
        return error
    }

    fun getConnectionError(): String? {
        return connectionError
    }

    enum class DataStatus {
        CONNECTIONERROR, ERROR
    }
}