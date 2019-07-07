package com.julius.anoma.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.julius.anoma.FeedsApplication
import com.julius.anoma.repository.FeedRepository
import com.julius.anoma.repository.Repository
import com.julius.anoma.repository.api.FeedApi
import com.julius.anoma.ui.MainActivityViewModel
import com.squareup.picasso.LruCache
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.Exception

private const val BASE_URL = "https://dl.dropboxusercontent.com/"

val feedModule = module(override = true) {
    single { getLruCache(FeedsApplication.instance) as LruCache }
    single { getCache(FeedsApplication.instance, 10 * 1024 * 1024) }
    single { createPicasso(FeedsApplication.instance, get()) }
    single { createApiClient(FeedsApplication.instance, get()) }
    single { FeedRepository(get()) as Repository }
    viewModel { MainActivityViewModel(get(), Dispatchers.Default) }
}

private fun createApiClient(context: Context, cache: Cache): FeedApi {
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(createHttpClient(context, cache))
        .build()
    return retrofit.create(FeedApi::class.java)
}

private fun createPicasso(context: Context, lruCache: LruCache) = Picasso.Builder(context).memoryCache(lruCache).build()

private fun createHttpClient(context: Context, cache: Cache): OkHttpClient {
    return OkHttpClient.Builder().cache(cache).addInterceptor { chain ->
        var request = chain.request()
        request = if (hasNetwork(context))
            request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
        else
            request.newBuilder().header(
                "Cache-Control",
                "public,max-stale=" + 60 * 60 * 24 * 7 // Offline cache available for 7 days
            ).build()
        chain.proceed(request)
    }.build()
}

private fun hasNetwork(context: Context): Boolean {
    var isConnected = false
    try {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
    } catch (e: Exception) {
        Log.e("FeedModule", "Unable to determine connection state.")
    }
    return isConnected
}

private fun getCache(context: Context, size: Long) = Cache(context.cacheDir, size)

private fun getLruCache(context: Context) = LruCache(context)