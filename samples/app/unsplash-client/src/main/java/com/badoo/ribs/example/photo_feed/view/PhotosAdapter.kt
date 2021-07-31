package com.badoo.ribs.example.photo_feed.view

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

internal class PhotosAdapter : ListAdapter<PhotoListItem, PhotoListItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoListItemViewHolder =
        when (viewType) {
            photoViewType -> PhotoViewHolder.create(parent.context)
            nextLoadingViewType -> NextPageLoadingViewHolder.create(parent.context, parent)
            nextLoadingErrorViewType -> NextPageLoadingErrorViewHolder.create(
                parent.context,
                parent
            )
            else -> throw IllegalArgumentException("View type $viewType is not supported by this adapter")
        }

    override fun onBindViewHolder(holder: PhotoListItemViewHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is PhotoListItem.PhotoItem -> (holder as? PhotoViewHolder)?.bind(item)
            is PhotoListItem.NextPageLoadingItem -> (holder as? NextPageLoadingViewHolder)?.bind()
            is PhotoListItem.NextPageLoadingErrorItem ->
                (holder as? NextPageLoadingErrorViewHolder)?.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is PhotoListItem.PhotoItem -> photoViewType
            is PhotoListItem.NextPageLoadingItem -> nextLoadingViewType
            is PhotoListItem.NextPageLoadingErrorItem -> nextLoadingErrorViewType
        }

    companion object {
        private const val photoViewType = 1
        private const val nextLoadingViewType = 2
        private const val nextLoadingErrorViewType = 3
    }
}

private class DiffCallback : DiffUtil.ItemCallback<PhotoListItem>() {

    override fun areItemsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean =
        oldItem == newItem
}
