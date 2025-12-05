package com.turismoapp.mayuandino.feature.github.domain.error

sealed class Failure : Throwable() {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    object NotFound : Failure()
    object EmptyBody : Failure()
    data class Unknown(val exception: Throwable) : Failure()
}