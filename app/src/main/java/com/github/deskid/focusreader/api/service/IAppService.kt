package com.github.deskid.focusreader.api.service

import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.ApiResponse
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface IAppService {
    @GET("http://appb.dapenti.com/index.php?s=/Home/api/loading_pic")
    fun splashImage(): LiveData<ApiResponse<Resource<String>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/tugua")
    fun getTuGua(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 10): LiveData<ApiResponse<Resource<List<TuGua>>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/yitu")
    fun getZenImage(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 30): LiveData<ApiResponse<Resource<List<ZenImage>>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/duanzi")
    fun getJoke(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 10): LiveData<ApiResponse<Resource<List<Duanzi>>>>

    //avoid 304
    @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/latest")
    fun getZhihuLatest(): LiveData<ApiResponse<Zhihu>>

    //avoid 304
    @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/{id}")
    fun getZhihuDetail(@Path("id") id: String): LiveData<ApiResponse<ZhihuDetail>>

    @GET
    fun get(@Url url:String): LiveData<ApiResponse<ResponseBody>>

    @GET("https://interface.meiriyiwen.com/article/{type}")
    fun getArticle(@Path("type") type: String): LiveData<ApiResponse<Article>>
}
