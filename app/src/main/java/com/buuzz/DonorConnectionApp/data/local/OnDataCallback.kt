package com.buuzz.DonorConnectionApp.data.local

interface OnDataCallback {
    fun onData(data: String?){}
    fun onError(errorMessage: String?){}
}