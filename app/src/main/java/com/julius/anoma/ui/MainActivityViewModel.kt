package com.julius.anoma.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julius.anoma.repository.Feed
import com.julius.anoma.repository.Repository

enum class Screen { LIST, FULLSCREEN }
class MainActivityViewModel(private val repository: Repository) : ViewModel() {
    val feeds: MutableLiveData<List<Feed>> = MutableLiveData()
    val title: MutableLiveData<String> = MutableLiveData()

    init {
        getFeeds()
    }

    fun getFeeds() {
        val newsFeed = repository.getFeeds()
        feeds.postValue(newsFeed.rows)
        title.postValue(newsFeed.title)
    }
}