package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerNewsBinding
import com.melvin.ongandroid.model.News

class NewsItemAdapter : ListAdapter<News, RecyclerView.ViewHolder>(DiffUtilCallback()) {
    private var onItemClicked: ((News) -> Unit)? = null

    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return constructor of ViewHolder referencing inflated layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
       return ViewHolder(binding)
    }

    /**
     * View holder
     *
     * @property binding reference to item layout
     * @constructor Create empty View holder
     */
    class ViewHolder(private val binding: ItemRecyclerNewsBinding) :
        RecyclerView.ViewHolder(binding.cvNews) {

        /**
         * Bind
         * Used to connect items to layout
         *
         * @param value the item
         * @param listener a function to invoke when the item in clicked
         */
        internal fun bind(
            value: News,
            listener: ((News) -> Unit)?,
        ) {
            with(binding) {
                // Load the image url and set it on this ImageView
                imageNews.load(value.image)
                // Set the title text
                titleNews.text = value.name
                //Load the image url and set it on this ImageView
                descriptionNews.text = value.content

                cvNews.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }

    /**
     * On bind view holder
     * Connect the item in the position of the list with the Holder
     *
     * @param holder
     * @param position of the item in the list
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder: ViewHolder = holder as ViewHolder
        viewHolder.bind(getItem(position), onItemClicked)
    }

    /**
     * Submit list
     *
     * @param list
     */
    override fun submitList(list: MutableList<News>?) {
        val copy: MutableList<News> = mutableListOf()
        if (list != null) {
            copy.addAll(list)
        }
        super.submitList(copy)
    }

    /**
     * Diff util callback
     *
     * @constructor Create empty Diff util callback
     */
    class DiffUtilCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.image == newItem.image
        }
        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

}