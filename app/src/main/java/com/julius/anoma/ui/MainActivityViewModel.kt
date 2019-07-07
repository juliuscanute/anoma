package com.julius.anoma.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julius.anoma.repository.Feed
import com.julius.anoma.repository.Repository
import kotlinx.coroutines.*
import java.lang.Exception

enum class NetworkStatus { SUCCESS, ERROR, START }
class MainActivityViewModel(private val repository: Repository, val dispatcher: CoroutineDispatcher) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Default + viewModelJob)

    val feeds: MutableLiveData<List<Feed>> = MutableLiveData<List<Feed>>().apply { value = arrayListOf() }
    val title: MutableLiveData<String> = MutableLiveData()
    val networkStatus: MutableLiveData<Event<NetworkStatus>> = MutableLiveData()

    init {
        getFeeds()
    }

    fun getFeeds() {
        uiScope.launch {
            try {
                networkStatus.postValue(Event(NetworkStatus.START))
                val newsFeed = loadData()
                feeds.postValue(newsFeed.rows)
                title.postValue(newsFeed.title)
                networkStatus.postValue(Event(NetworkStatus.SUCCESS))
            } catch (e: Exception) {
                networkStatus.postValue(Event(NetworkStatus.ERROR))
            }

        }
    }

    private suspend fun loadData() = withContext(dispatcher) {
        repository.getFeeds()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}