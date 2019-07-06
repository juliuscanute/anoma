package com.julius.anoma.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.julius.anoma.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_full_screen.*
import org.koin.android.viewmodel.ext.android.viewModel

private const val URI_STRING = "uri_string"

class FullScreenFragment : Fragment() {
    private var uri: String? = null
    private val mainActivityViewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uri = it.getString(URI_STRING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        Picasso.get()
            .load(uri)
            .error(R.drawable.ic_broken_image_placeholder)
            .placeholder(R.drawable.ic_crop_original)
            .into(displayImage)
    }

    companion object {
        @JvmStatic
        fun newBundle(uri: String) =
            Bundle().apply {
                putString(URI_STRING, uri)
            }
    }
}
