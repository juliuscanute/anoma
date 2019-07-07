package com.julius.anoma.data.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource
import com.julius.anoma.data.dto.Feed
import com.julius.anoma.data.dto.FeedAggregator
import com.julius.anoma.data.dto.NetworkMessage
import com.julius.anoma.data.repository.Repository
import com.julius.anoma.ui.Event
import com.julius.anoma.ui.NetworkStatus
import java.lang.Exception
import kotlin.math.max

class FeedsDataSource(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) : PositionalDataSource<Feed>() {
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Feed>) {
        try {
            val data = loadData()
            callback.onResult(data.rows)
            networkState.postValue(
                Event(
                    NetworkMessage(NetworkStatus.SUCCESS, data.title ?: "")
                )
            )
        } catch (e: Exception) {
            networkState.postValue(
                Event(
                    NetworkMessage(NetworkStatus.ERROR, "Error-Unable to fetch")

                )
            )
        }
    }

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Feed>) {
        try {
            val data = loadData()
            callback.onResult(data.rows, max(params.requestedStartPosition, 0))
            networkState.postValue(
                Event(
                    NetworkMessage(NetworkStatus.SUCCESS, data.title ?: "")
                )
            )
        } catch (e: Exception) {
            networkState.postValue(
                Event(
                    NetworkMessage(NetworkStatus.ERROR, "Error")
                )
            )
        }
    }

    private fun loadData(): FeedAggregator {
        networkState.postValue(
            Event(
                NetworkMessage(NetworkStatus.START, "Starting data Load")
            )
        )
        return repository.getFeeds()
    }
}