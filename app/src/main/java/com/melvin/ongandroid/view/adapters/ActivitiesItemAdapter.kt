package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerActivitiesBinding
import com.melvin.ongandroid.model.ActivityUI
import com.melvin.ongandroid.utils.convertHtmlToString

/**
 * Activities item adapter
 * created on 1 May 2022 by Leonel Gomez
 *
 * @constructor Create empty Activities item adapter
 */
class ActivitiesItemAdapter() :
    ListAdapter<ActivityUI, RecyclerView.ViewHolder>(DiffUtilCallback()) {
    var onItemClicked: ((ActivityUI) -> Unit)? = null

    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return constructor of ViewHolder referencing inflated layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerActivitiesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
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
     * View holder
     *
     * @property binding reference to item layout
     * @constructor Create empty View holder
     */
    class ViewHolder(private val binding: ItemRecyclerActivitiesBinding) :
        RecyclerView.ViewHolder(binding.cvActivities) {

        /**
         * Bind
         * Used to connect items to layout
         *
         * @param value the item
         * @param listener a function to invoke when the item in clicked
         */
        internal fun bind(
            value: ActivityUI,
            listener: ((ActivityUI) -> Unit)?,
        ) {
            with(binding) {
                //Set texts
                tvActivitiesTitle.text = value.name
                tvActivitiesDescription.text = value.description.convertHtmlToString()
                //Load the image url and set it on this ImageView
                imgActivities.load(value.image)

                cvActivities.setOnClickListener {
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
    class DiffUtilCallback : DiffUtil.ItemCallback<ActivityUI>() {
        override fun areItemsTheSame(oldItem: ActivityUI, newItem: ActivityUI): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: ActivityUI, newItem: ActivityUI): Boolean =
            oldItem == newItem

    }
}
