package com.github.deskid.focusreader.api.service

import com.github.deskid.focusreader.api.PentiResource
import com.github.deskid.focusreader.api.data.*
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.*

typealias Data<T> = PentiResource<T>

interface IAppService {
    /**
     * 获取 500px 启动页图案
     * @param quality 图片大小质量
     * @return [Photos500px]
     */
    @GET("https://api.500px.com/v1/photos?consumer_key=HocY5wY9GQaa9sdNO9HvagCGuGt34snyMTHckIQJ")
    fun splash500pxImage(@Query("feature") feature: String = "popular",
                         @Query("sort") sort: String = "rating",
                         @Query("image_size") quality: Int = 4,
                         @Query("rpp") count: Int = 1): Flowable<Photos500px>

    /**
     * 获取 图卦-喷嚏图卦
     * @param pageNum 页数.
     * @param limitNum 每页offset.
     * @return [ZenImage]
     */
    @GET("https://appb.dapenti.com/index.php?s=/Home/api/tugua")
    fun getTuGua(@Query("p") pageNum: Int = 1,
                 @Query("limit") limitNum: Int = 10): Flowable<Data<List<TuGua>>>

    /**
     * 获取 意图-喷嚏图卦
     * @param pageNum 页数.
     * @param limitNum 每页offset.
     * @return [ZenImage]
     */
    @GET("https://appb.dapenti.com/index.php?s=/Home/api/yitu")
    fun getZenImage(@Query("p") pageNum: Int = 1,
                    @Query("limit") limitNum: Int = 10): Flowable<Data<List<ZenImage>>>

    /**
     * 获取 段子-喷嚏图卦
     * @param pageNum 页数.
     * @param limitNum 每页offset.
     * @return [Duanzi]
     */
    @GET("https://appb.dapenti.com/index.php?s=/Home/api/duanzi")
    fun getJoke(@Query("p") pageNum: Int = 1,
                @Query("limit") limitNum: Int = 10): Flowable<Data<List<Duanzi>>>

    /**
     * 获取 知乎日报 当日日报
     * @return [Zhihu]
     */
    // avoid 304 for debug
    // @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/latest")
    fun getZhihuLatest(): Flowable<Zhihu>

    /**
     * 获取 知乎日报 往日日报
     * @param date 日期.
     * @return [Zhihu]
     */
    // avoid 304 for debug
    // @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/before/{date}")
    fun getZhihuHistory(@Path("date") date: String): Flowable<Zhihu>

    /**
     * 获取 知乎详情
     * @param id 链接地址.
     * @return [ZhihuDetail]
     */
    // avoid 304 for debug
    // @Headers("If-Modified-Since: Tue, 12 May 2015 00:00:00 GMT")
    @GET("https://news-at.zhihu.com/api/4/news/{id}")
    fun getZhihuDetail(@Path("id") id: String): Flowable<ZhihuDetail>

    /**
     * 获取 html 内容
     * @param url 链接地址.
     * @return [ResponseBody]
     */
    @GET
    fun getContent(@Url url: String): Flowable<ResponseBody>

    /**
     * 获取 每日一文
     * @param type 文章类型 - 随机(random)、今日(day).
     * @return [Article]
     */
    @Headers("User-Agent: Mozilla/5.0 (Linux; Android 7.1.1; MI 6 Build/NMF26X; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/60.0.3112.116 Mobile Safari/537.36 Meiriyiwen-App-Client/1.0")
    @GET("https://interface.meiriyiwen.com/article/{type}")
    fun getArticle(@Path("type") type: String): Flowable<Article>

    /**
     * 获取 热门话题-readhub
     * @param lastCursor 最后的动态id，用于分页.
     * @param pageSize 每页数量.
     * @return [Topics]
     */
    @GET("https://api.readhub.me/topic/")
    fun getReadhubTopics(@Query("lastCursor") lastCursor: Long? = null,
                         @Query("pageSize") pageSize: Int = 10): Flowable<Topics>

    /**
     * 获取 开发者资讯-readhub
     * @param lastCursor 最后的动态id，用于分页.
     * @param pageSize 每页数量.
     * @return [Technews]
     */
    @GET("https://api.readhub.me/technews/")
    fun getReadhubTechnews(@Query("lastCursor") lastCursor: Long? = null,
                           @Query("pageSize") pageSize: Int = 10): Flowable<Technews>

    /**
     * 获取 科技动态-readhub
     * @param lastCursor 最后的动态id，用于分页.
     * @param pageSize 每页数量.
     * @return [News]
     */
    @GET("https://api.readhub.me/news/")
    fun getReadhubNews(@Query("lastCursor") lastCursor: Long? = null,
                       @Query("pageSize") pageSize: Int = 10): Flowable<News>

    /**
     * 获取 即时查看-readhub
     * @param topicId 话题Id.
     * @return [News]
     */
    @GET("https://api.readhub.me/topic/instantview/")
    fun getReadhubInstantView(@Query("topicId") topicId: String): Flowable<InstantView>
}
