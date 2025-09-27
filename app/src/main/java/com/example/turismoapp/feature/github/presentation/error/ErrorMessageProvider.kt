package com.example.turismoapp.feature.github.presentation.error

import android.content.Context
import com.example.turismoapp.R
import com.example.turismoapp.feature.github.domain.error.Failure

class ErrorMessageProvider(private val context: Context) {
    fun getMessage(failure: Failure): String {
        return when (failure) {
            is Failure.NetworkConnection -> context.getString(R.string.error_network)
            is Failure.ServerError -> context.getString(R.string.error_server)
            is Failure.NotFound -> context.getString(R.string.error_not_found)
            is Failure.EmptyBody -> context.getString(R.string.error_empty_response)
            is Failure.Unknown -> context.getString(R.string.error_unknown)
        }
    }
}