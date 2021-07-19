package com.badoo.ribs.example.photo_feed.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.badoo.ribs.example.R


abstract class PhotoListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

internal class PhotoViewHolder(private val photoView: AspectRatioImageView) :
    PhotoListItemViewHolder(photoView) {

    fun bind(item: PhotoListItem.PhotoItem) {
        with(item) {
            photoView.setAspectRatio(photo.width, photo.height)
            photoView.load(photo.url)
            photoView.setOnClickListener {
                onClicked(photo)
            }
        }
    }

    companion object {
        fun create(context: Context) = PhotoViewHolder(
            AspectRatioImageView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        )
    }
}

internal class NextPageLoadingViewHolder(itemView: View) : PhotoListItemViewHolder(itemView) {


    fun bind() {
    }

    companion object {
        fun create(context: Context, parent: ViewGroup) = NextPageLoadingViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.item_photo_feed_next_page_loading,
                    parent,
                    false
                )
        )
    }
}

internal class NextPageLoadingErrorViewHolder(itemView: View) :
    PhotoListItemViewHolder(itemView) {
    private val retry = itemView.findViewById<ImageView>(R.id.photoFeed_retry)

    fun bind(item: PhotoListItem.NextPageLoadingErrorItem) {
        retry.setOnClickListener {
            item.onClicked()
        }
    }

    companion object {
        fun create(context: Context, parent: ViewGroup) = NextPageLoadingErrorViewHolder(
            LayoutInflater.from(context)
                .inflate(
                    R.layout.item_photo_feed_next_page_loading_error,
                    parent,
                    false
                )
        )
    }
}
