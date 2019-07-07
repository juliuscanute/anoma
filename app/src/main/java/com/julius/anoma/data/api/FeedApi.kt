package com.julius.anoma.data.api

import com.julius.anoma.data.dto.FeedAggregator
import retrofit2.Call
import retrofit2.http.GET

interface FeedApi{
    @GET("s/2iodh4vg0eortkl/facts.json")
    fun getFeeds(): Call<FeedAggregator>
}