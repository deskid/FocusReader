package com.github.deskid.focusreader.api.service

import android.arch.lifecycle.LiveData
import com.github.deskid.focusreader.api.ApiResponse
import com.github.deskid.focusreader.api.Resource
import com.github.deskid.focusreader.api.data.Duanzi
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.api.data.ZenImage
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IAppService {
    @GET("http://appb.dapenti.com/index.php?s=/Home/api/loading_pic")
    fun splashImage(): LiveData<ApiResponse<Resource<String>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/tugua")
    fun getTuGua(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 10): LiveData<ApiResponse<Resource<List<TuGua>>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/yitu")
    fun getZenImage(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 10): LiveData<ApiResponse<Resource<List<ZenImage>>>>

    @GET("http://appb.dapenti.com/index.php?s=/Home/api/duanzi")
    fun getJoke(@Query("p") pageNum: Int = 1, @Query("limit") limitNum: Int = 10): LiveData<ApiResponse<Resource<List<Duanzi>>>>

    @GET
    fun get(@Url url:String): Flowable<ResponseBody>
}
