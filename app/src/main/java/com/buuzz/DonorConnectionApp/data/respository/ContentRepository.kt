package com.buuzz.DonorConnectionApp.data.respository

import com.buuzz.DonorConnectionApp.data.local.DataStoreHelper
import com.buuzz.DonorConnectionApp.data.local.SharedPreferencesHelper
import com.buuzz.DonorConnectionApp.data.model.request.PostCreateModel
import com.buuzz.DonorConnectionApp.data.model.response.Category
import com.buuzz.DonorConnectionApp.data.model.response.GeocodeResponse
import com.buuzz.DonorConnectionApp.data.model.response.GetPostResponse
import com.buuzz.DonorConnectionApp.data.model.response.GetPostsResponse
import com.buuzz.DonorConnectionApp.data.model.response.InitContentData
import com.buuzz.DonorConnectionApp.data.model.response.PostRequestResponse
import com.buuzz.DonorConnectionApp.data.model.response.ResponseModel
import com.buuzz.DonorConnectionApp.data.model.response.Tag
import com.buuzz.DonorConnectionApp.data.model.response.UserRequestListResponse
import com.buuzz.DonorConnectionApp.data.remote.MainApi
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.Resource
import com.buuzz.DonorConnectionApp.utils.apihelper.safeapicall.SafeApiCall
import com.buuzz.DonorConnectionApp.utils.helpers.AppData
import com.buuzz.DonorConnectionApp.utils.helpers.AppLogger
import com.google.gson.Gson
import javax.inject.Inject

private const val TAG = "ContentRepository"

class ContentRepository @Inject constructor(
    private val mainApi: MainApi,
    private val dataStoreHelper: DataStoreHelper,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    suspend fun createPost(
        image: String?,
        title: String,
        desc: String,
        category_id: Int?,
        tag_id: List<Int>?,
        address: String,
        latitude: Double,
        longitude: Double
    ): Resource<ResponseModel> {
        val data = PostCreateModel(
            image = image,
            title = title,
            desc = desc,
            category_id = category_id,
            tag_id = tag_id,
            user_id = dataStoreHelper.readStringFromDatastore(AppData.USER_ID),
            address = address, latitude, longitude
        )
        val response = SafeApiCall.execute {
            mainApi.createPost(
                data
            )
        }
        when (response) {
            is Resource.Success -> {
                AppLogger.logD(TAG, "image uploaded")
            }

            is Resource.Failure -> {

            }
        }
        return response
    }

    suspend fun getUserId(): String {
        return dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
    }

    suspend fun getInitContents() {
        when (val response = SafeApiCall.execute { mainApi.getInitContents() }) {
            is Resource.Failure -> {
                AppLogger.logD(TAG, response.errorMsg)
            }

            is Resource.Success -> {
                if (response.value.success && !response.value.data?.tags.isNullOrEmpty()) {
                    dataStoreHelper.saveStringToDatastore(
                        AppData.INIT_CONTENTS to
                                Gson().toJson(response.value.data)
                    )
                }
            }
        }
    }

    suspend fun getTagList(): List<Tag> {
        val data = Gson().fromJson(
            dataStoreHelper.readStringFromDatastore(AppData.INIT_CONTENTS),
            InitContentData::class.java
        )
        return data?.tags ?: emptyList()
    }

    suspend fun getCategoryList(): List<Category> {
        val data = Gson().fromJson(
            dataStoreHelper.readStringFromDatastore(AppData.INIT_CONTENTS),
            InitContentData::class.java
        )
        return data?.categories ?: emptyList()
    }

    suspend fun getPostsByCategory(category_id: String?): Resource<GetPostsResponse> {
        return SafeApiCall.execute { mainApi.getPostsByCategory(category_id) }
    }
    suspend fun getPostsBySearch(query: String?): Resource<GetPostsResponse> {
        return SafeApiCall.execute { mainApi.getPostsBySearch(query) }
    }

    suspend fun getPostsByTag(tag_id: String): Resource<GetPostsResponse> {
        return SafeApiCall.execute { mainApi.getPostsByTag("[$tag_id]" ) }
    }

    suspend fun getAllPosts(): Resource<GetPostsResponse> {
        return SafeApiCall.execute {
            mainApi.getAllPosts(
                dataStoreHelper.readStringFromDatastore(
                    AppData.USER_ID
                )
            )
        }
    }

    suspend fun getPost(post_id: String?): Resource<GetPostResponse> {
        return SafeApiCall.execute { mainApi.getPost(                dataStoreHelper.readStringFromDatastore(AppData.USER_ID),post_id) }
    }

    suspend fun requestPost(post_id: String?): Resource<PostRequestResponse> {
        return SafeApiCall.execute {
            mainApi.requestPost(
                post_id,
                dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
            )
        }
    }

    suspend fun cancelPost(post_id: String?): Resource<ResponseModel> {
        return SafeApiCall.execute {
            mainApi.cancelPost(
                post_id,
                dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
            )
        }
    }


    suspend fun userRequests(): Resource<UserRequestListResponse> {
        return SafeApiCall.execute {
            mainApi.userRequests(
                dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
            )
        }
    }

    suspend fun getLocation(latitude: Double, longitude: Double): Resource<GeocodeResponse> {
        return SafeApiCall.execute { mainApi.getLocation(latitude, longitude) }
    }

    suspend fun getBookmarkList(): Resource<GetPostsResponse> {
        return SafeApiCall.execute {
            mainApi.getBookmarks(
                dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
            )
        }
    }

    suspend fun bookmarkPost(post_id: String?): Resource<ResponseModel> {
        return SafeApiCall.execute {
            mainApi.bookmark(post_id ,
                dataStoreHelper.readStringFromDatastore(AppData.USER_ID)
            )
        }
    }
    suspend fun getRecommendationList(post_id: String?): Resource<GetPostsResponse> {
        return SafeApiCall.execute {
            mainApi.getRecommendationList(post_id
            )
        }
    }



}