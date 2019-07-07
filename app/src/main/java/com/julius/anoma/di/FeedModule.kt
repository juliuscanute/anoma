package com.julius.anoma.di

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.julius.anoma.FeedsApplication
import com.julius.anoma.data.repository.FeedRepository
import com.julius.anoma.data.repository.Repository
import com.julius.anoma.data.api.FeedApi
import com.julius.anoma.data.datasource.FeedsDataSourceFactory
import com.julius.anoma.data.dto.NetworkMessage
import com.julius.anoma.ui.Event
import com.julius.anoma.ui.MainActivityViewModel
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://dl.dropboxusercontent.com/"
const val PAGE_SIZE = 13

val feedModule = module(override = true) {
    single { PagedList.Config.Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(false).build() }
    single { getLruCache(FeedsApplication.instance) }
    single { getCache(FeedsApplication.instance, 10 * 1024 * 1024) }
    single { createPicasso(FeedsApplication.instance, get()) }
    single { createApiClient(get()) }
    single { FeedRepository(get()) as Repository }
    single { MutableLiveData<Event<NetworkMessage>>() }
    factory { FeedsDataSourceFactory(get(), get()) }
    viewModel { MainActivityViewModel(get(), get(), get()) }
}

private fun createApiClient(cache: Cache): FeedApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(createHttpClient(cache))
        .build()
    return retrofit.create(FeedApi::class.java)
}

private fun createPicasso(context: Context, lruCache: LruCache) = Picasso.Builder(context).memoryCache(lruCache).build()

private fun createHttpClient(cache: Cache): OkHttpClient {
    return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
        var request = chain.request()
        request =
            request.newBuilder().header(
                "Cache-Control",
                "public,max-stale=" + 60 * 60 * 24 * 7 // Offline cache available for 7 days
            ).build()
        chain.proceed(request)
    }.build()
}

private fun getCache(context: Context, size: Long) = Cache(context.cacheDir, size)

private fun getLruCache(context: Context) = LruCache(context)