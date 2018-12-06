package com.github.deskid.focusreader

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.Executors

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testFlatMap() {
        val numbers = ArrayList(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10))

        Observable.fromIterable(numbers)
                .flatMap { it -> return@flatMap Observable.just(it * it) }
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(3)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    print(it.toString() + " ")
                }
    }


    @Test
    fun testConcatMap() {
        val numbers = ArrayList(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10))

        Observable.fromIterable(numbers)
                .concatMap { it -> return@concatMap Observable.just(it * it) }
                .subscribeOn(Schedulers.from(Executors.newFixedThreadPool(3)))
                .subscribe {
                    print(it.toString() + " ")
                }

    }




}
