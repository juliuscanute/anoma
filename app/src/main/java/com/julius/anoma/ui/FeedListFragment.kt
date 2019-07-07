package com.julius.anoma.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.julius.anoma.R
import com.squareup.picasso.LruCache
import kotlinx.android.synthetic.main.fragment_feed_list.*
import okhttp3.Cache
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class FeedListFragment : Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by viewModel()
    private val imageCache: LruCache by inject()
    private val httpCache: Cache by inject()
    private val feedAdapter = FeedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedList.adapter = feedAdapter
        mainActivityViewModel.feeds.observe(this, Observer { list ->
            feedAdapter.submitList(list)
            updateAfterRefresh(list.size)
        })
        (activity as AppCompatActivity).setSupportActionBar(appToolbar)
        refreshLayout.setOnRefreshListener {
            imageCache.clear()
            httpCache.evictAll()
            startRefresh()
        }
        mainActivityViewModel.title.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.apply {
                title = it
            }
        })
        mainActivityViewModel.networkStatus.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let { message ->
                when (message.status) {
                    NetworkStatus.START -> {
                        refreshLayout.isRefreshing = true
                    }
                    NetworkStatus.SUCCESS -> {
                        refreshLayout.isRefreshing = false
                        mainActivityViewModel.setTitle(message.title)
                    }
                    NetworkStatus.ERROR -> {
                        refreshLayout.isRefreshing = false
                    }
                }
            }
        })
    }

    private fun updateAfterRefresh(dataSize: Int) {
        if (dataSize > 0)
            networkStatus.visibility = View.GONE
        else
            networkStatus.visibility = View.VISIBLE
    }

    private fun startRefresh() {
        mainActivityViewModel.invalidateDataSource()
    }
}
