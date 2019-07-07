package com.julius.anoma.data.repository

import com.julius.anoma.data.dto.FeedAggregator

interface Repository {
    fun getFeeds(): FeedAggregator
}