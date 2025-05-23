package com.buuzz.DonorConnectionApp.data.remote

object ApiEndPoints {


    const val REFRESH_TOKEN = "refreshToken"

    const val REGISTER = "api/register"
    const val LOGIN = "api/login"
    const val USER_DETAILS = "api/user"
    const val LOG_OUT  ="api/logout"


    const val INIT_CONTENT = "api/content/init-content"
    const val CREATE_POST = "api/post/createPost"
    const val GET_ALL_POST = "api/post/getAllPosts"
    const val GET_POST_BY_CATEGORY = "api/post/getPostsByCategoryId"
    const val GET_POST_BY_SEARCH = "api/post/searchPosts"
    const val GET_POST_BY_TAG = "api/post/getPostsByTags"
    const val GET_POST = "api/post/getPost"
    const val REQUEST_POST = "api/post/requestPost"
    const val CANCEL_POST = "api/post/cancelRequest"
    const val USER_REQUESTS = "api/post/userRequests"
    const val GET_LOCATION = "api/map/getLocation"
    const val BOOKMARK = "api/post/bookmarkPost"
    const val GET_BOOKMARK_LIST = "api/post/getBookmarkedPosts"
    const val GET_RECOMMENDATION_LIST = "api/post/getRecommendedPosts"


}