package com.julius.anoma.repository

interface Repository {
    fun getFeeds(): FeedAggregator
}