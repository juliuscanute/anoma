package com.julius.anoma.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.julius.anoma.R
import kotlinx.android.synthetic.main.fragment_feed_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class FeedListFragment : Fragment() {

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivityViewModel.feeds.observe(this, Observer {
            feedList.adapter = FeedAdapter(it)
        })
        mainActivityViewModel.title.observe(this, Observer {
            (activity as AppCompatActivity).supportActionBar?.title = it
        })
    }
}
