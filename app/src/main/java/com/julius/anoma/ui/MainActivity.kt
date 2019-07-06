package com.julius.anoma.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.julius.anoma.R
import kotlinx.android.synthetic.main.fragment_feed_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(appToolbar)
    }

}
