package com.buuzz.DonorConnectionApp.utils.apihelper

import com.buuzz.DonorConnectionApp.utils.helpers.AppLogger
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object DoesNetworkHaveInternet {
    val TAG = this.javaClass.name

    // Make sure to execute this on a background thread.
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            AppLogger.logD(TAG, "PING success.")
            true
        } catch (e: IOException) {
            AppLogger.logD(TAG, "No internet connection.")
            false
        }
    }
}