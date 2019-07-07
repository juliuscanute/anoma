package com.julius.anoma.data.datasource

import androidx.lifecycle.MutableLiveData
import com.julius.anoma.data.dto.Feed
import com.julius.anoma.ui.Event
import com.julius.anoma.ui.NetworkStatus
import androidx.paging.DataSource
import com.julius.anoma.data.dto.NetworkMessage
import com.julius.anoma.data.repository.Repository

class FeedsDataSourceFactory(
    private val repository: Repository,
    private val networkState: MutableLiveData<Event<NetworkMessage>>
) : DataSource.Factory<Int, Feed>() {

    val sourceLiveData = MutableLiveData<FeedsDataSource>()

    override fun create(): DataSource<Int, Feed> {
        val source = FeedsDataSource(repository, networkState)
        sourceLiveData.postValue(source)
        return source
    }
}