package com.julius.anoma.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.julius.anoma.data.datasource.FeedsDataSourceFactory
import com.julius.anoma.data.dto.Feed
import com.julius.anoma.data.dto.NetworkMessage

enum class NetworkStatus { SUCCESS, ERROR, START }
class MainActivityViewModel(
    config: PagedList.Config,
    dataSourceFactory: FeedsDataSourceFactory,
    val networkStatus: MutableLiveData<Event<NetworkMessage>>
) : ViewModel() {
    val feeds: LiveData<PagedList<Feed>> = LivePagedListBuilder(dataSourceFactory, config).build()
    val title: MutableLiveData<String> = MutableLiveData()

    fun setTitle(header: String) {
        title.postValue(header)
    }

    fun invalidateDataSource() {
        feeds.value?.dataSource?.invalidate()
    }
}