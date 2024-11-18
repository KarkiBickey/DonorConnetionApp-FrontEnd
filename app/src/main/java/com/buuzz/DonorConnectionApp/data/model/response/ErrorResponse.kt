package com.buuzz.DonorConnectionApp.data.model.response

data class ErrorResponse(
    val code: Int,
    val message: String,
    val status: Boolean
)