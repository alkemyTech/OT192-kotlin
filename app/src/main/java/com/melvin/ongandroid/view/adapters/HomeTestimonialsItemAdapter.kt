package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerHomeTestimonialsBinding
import com.melvin.ongandroid.model.HomeTestimonials

class HomeTestimonialsItemAdapter() :
    ListAdapter<HomeTestimonials, RecyclerView.ViewHolder>(DiffUtilCallback()) {
    var onItemClicked: ((HomeTestimonials) -> Unit)? = null
    var onMoreItemClicked: ((HomeTestimonials) -> Unit)? = null

    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return constructor of ViewHolder referencing inflated layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerHomeTestimonialsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return when (viewType) {
            -1 -> ViewHolderPlus(binding)
            else -> ViewHolder(binding)
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
        when (holder.itemViewType) {
            -1 -> {
                val viewHolder: ViewHolderPlus = holder as ViewHolderPlus
                viewHolder.bind(getItem(position), onMoreItemClicked)
            }
            else -> {
                val viewHolder: ViewHolder = holder as ViewHolder
                viewHolder.bind(getItem(position), onItemClicked)
            }
        }
    }

    /**
     * Get item view type
     *
     * @param position
     * @return
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1)
            -1
        else
            0
    }

    /**
     * Submit list
     *
     * @param list
     */
    override fun submitList(list: MutableList<HomeTestimonials>?) {
        val copy: MutableList<HomeTestimonials> = mutableListOf()
        if (list != null) {
            copy.addAll(list)
        }
        //Agrego un elemento (el último será distinto) para la flecha
        copy.add(HomeTestimonials())
        super.submitList(copy)
    }


    /**
     * View holder
     *
     * @property binding reference to item layout
     * @constructor Create empty View holder
     */
    class ViewHolder(private val binding: ItemRecyclerHomeTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.cvTestimonials) {

        /**
         * Bind
         * Used to connect items to layout
         *
         * @param value the item
         * @param listener a function to invoke when the item in clicked
         */
        internal fun bind(
            value: HomeTestimonials,
            listener: ((HomeTestimonials) -> Unit)?,
        ) {
            with(binding) {
                //Set texts
                tvTestimonialsHeading.text = value.heading
                //Load the image url and set it on this ImageView
                imgTestimonials.load(value.imgUrl)
                //Hide Arrow Resource
                imgMoreTestimonials.visibility = View.GONE

                cvTestimonials.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }

    /**
     * View holder plus
     *
     * @property binding
     * @constructor Create empty View holder plus
     */
    class ViewHolderPlus(private val binding: ItemRecyclerHomeTestimonialsBinding) :
        RecyclerView.ViewHolder(binding.cvTestimonials) {

        /**
         * Bind
         *
         * @param value
         * @param listener
         */
        internal fun bind(
            value: HomeTestimonials,
            listener: ((HomeTestimonials) -> Unit)?
        ) {
            with(binding) {
                //Hide RecyclerView fields
                lytCardView.visibility = View.GONE
                //Show Arrow resource
                imgMoreTestimonials.isVisible = true

                cvTestimonials.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }

    /**
     * Diff util callback
     *
     * @constructor Create empty Diff util callback
     */
    class DiffUtilCallback : DiffUtil.ItemCallback<HomeTestimonials>() {
        override fun areItemsTheSame(
            oldItem: HomeTestimonials,
            newItem: HomeTestimonials
        ): Boolean =
            oldItem.heading == newItem.heading

        override fun areContentsTheSame(
            oldItem: HomeTestimonials,
            newItem: HomeTestimonials
        ): Boolean =
            oldItem == newItem

    }
}