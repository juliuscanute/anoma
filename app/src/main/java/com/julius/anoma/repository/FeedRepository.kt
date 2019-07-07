package com.julius.anoma.repository

import com.julius.anoma.repository.api.FeedApi
import com.julius.anoma.repository.exception.FetchException
import retrofit2.HttpException
import retrofit2.Response

class FeedRepository(val api: FeedApi) : Repository {
    override fun getFeeds(): FeedAggregator {
        var feeds = FeedAggregator()
        try {
            val call = api.getFeeds()
            val result = call.execute()
            if (isRequestNotSuccessfull(result)) {
                throw HttpException(result)
            }
            result.body()?.let { feedAggregator ->
                feeds = feedAggregator
                feeds.rows = feeds.rows.filter { isNotEmpty(it) }.toList()
            }
            if (feeds.title == null)
                throw HttpException(result)
        } catch (e: Exception) {
            throw FetchException("Unable to fetch total pages")
        }
        return feeds
    }

    private fun isNotEmpty(it: Feed) = !(it.title == null && it.description == null && it.imageHref == null)

    private fun isRequestNotSuccessfull(result: Response<*>) =
        !(result.isSuccessful && result.code() == 200 && result.body() != null)
}