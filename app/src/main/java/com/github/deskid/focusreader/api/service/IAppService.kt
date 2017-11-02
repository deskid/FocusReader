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
    @GET("https://news-at.zhihu.com/api/4/news/before/{date}")
    fun getZhihuHistory(@Path("date") date: String): LiveData<ApiResponse<Zhihu>>

    //avoid 304
    @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/{id}")
    fun getZhihuDetail(@Path("id") id: String): LiveData<ApiResponse<ZhihuDetail>>

    @GET
    fun get(@Url url:String): LiveData<ApiResponse<ResponseBody>>

    @Headers("User-Agent: Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.116 Mobile Safari/537.36 Meiriyiwen-App-Client/1.0")
    @GET("https://interface.meiriyiwen.com/article/{type}")
    fun getArticle(@Path("type") type: String): LiveData<ApiResponse<Article>>

    @GET("https://api.readhub.me/topic/")
    fun getReadhubTopics(@Query("lastCursor") lastCursor: Long? = null, @Query("pageSize") pageSize: Int = 10): LiveData<ApiResponse<Topics>>

    @GET("https://api.readhub.me/technews/")
    fun getReadhubTechnews(@Query("lastCursor") lastCursor: Long? = null, @Query("pageSize") pageSize: Int = 10): LiveData<ApiResponse<Technews>>

    @GET("https://api.readhub.me/news/")
    fun getReadhubNews(@Query("lastCursor") lastCursor: Long? = null, @Query("pageSize") pageSize: Int = 10): LiveData<ApiResponse<News>>
}
