package com.julius.anoma.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.julius.anoma.R
import com.julius.anoma.repository.Feed

class FeedAdapter(private val feedItems: List<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.feed_item_card, parent, false)
        return ViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return feedItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedItem = feedItems[position]
        holder.bind(feedItem)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val heading: TextView = itemView.findViewById(R.id.heading)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        fun bind(feedItem: Feed) {
            heading.text = feedItem.title
            content.text = feedItem.description
            thumbnail.setImageResource(R.drawable.ic_broken_image_placeholder)
        }
    }
}