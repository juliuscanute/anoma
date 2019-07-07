package com.julius.anoma.ui

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.julius.anoma.R
import com.julius.anoma.data.dto.Feed
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class FeedAdapter :
    PagedListAdapter<Feed, RecyclerView.ViewHolder>(REPO_COMPARATOR), KoinComponent {

    private val picasso: Picasso by inject()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.feed_item_card, parent, false)
        return ViewItemHolder(listItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val feedItem = getItem(position)
        feedItem?.let {
            (holder as ViewItemHolder).bind(feedItem)
        }
    }

    inner class ViewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val heading: TextView = itemView.findViewById(R.id.heading)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val context = itemView.context
        fun bind(feedItem: Feed) {
            heading.text = feedItem.title
            content.text = feedItem.description
            loadThumbnail(feedItem)
        }

        private fun loadThumbnail(feedItem: Feed) {
            feedItem.imageHref?.let {
                try {
                    val uri = Uri.parse(it)
                    picasso
                        .load(uri)
                        .transform(
                            RoundedCornersTransformation(
                                context.resources.getDimensionPixelSize(R.dimen.image_corner_radius),
                                context.resources.getDimensionPixelSize(R.dimen.image_corner_margin)
                            )
                        )
                        .error(R.drawable.ic_broken_image_placeholder)
                        .placeholder(R.drawable.ic_crop_original)
                        .into(thumbnail, object : Callback {
                            override fun onSuccess() {
                                thumbnail.setOnClickListener {
                                    feedItem.imageHref?.let {
                                        val bundle = FullScreenFragment.newBundle(it)
                                        itemView.findNavController().navigate(R.id.listToFullScreenAction, bundle)
                                    }
                                }
                            }

                            override fun onError(e: Exception?) {
                                thumbnail.setOnClickListener {}
                            }

                        })
                } catch (e: Exception) {
                    thumbnail.setImageResource(R.drawable.ic_broken_image_placeholder)
                }
            }
            if (feedItem.imageHref == null) {
                thumbnail.setImageResource(R.drawable.ic_broken_image_placeholder)
                thumbnail.setOnClickListener {}
            }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem.description == newItem.description && oldItem.title == newItem.title && oldItem.imageHref == newItem.imageHref
        }
    }
}