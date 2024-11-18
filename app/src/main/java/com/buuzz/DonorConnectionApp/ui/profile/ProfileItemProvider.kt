package com.buuzz.DonorConnectionApp.ui.profile

import android.content.Context
import com.buuzz.DonorConnectionApp.R

object ProfileItemProvider {
    fun getProfileMoreItems(context: Context): ArrayList<ProfileModel> {
        val moreProfileArray = ArrayList<ProfileModel>()
        moreProfileArray.clear()
        moreProfileArray.add(
            ProfileModel(
                context.resources.getString(R.string.logout),
                context.resources.getString(R.string.about_us),
            )
        )
        return moreProfileArray
    }

}