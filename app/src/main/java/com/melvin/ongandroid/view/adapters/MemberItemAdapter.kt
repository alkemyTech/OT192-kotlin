package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerAboutUsBinding
import com.melvin.ongandroid.model.MemberUI

/**
 * Member item adapter
 * created on 30 April 2022 by Leonel Gomez
 *
 * @constructor Create empty Member item adapter
 */
class MemberItemAdapter() :
    ListAdapter<MemberUI, RecyclerView.ViewHolder>(DiffUtilCallback()) {
    var onItemClicked: ((MemberUI) -> Unit)? = null

    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return constructor of ViewHolder referencing inflated layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerAboutUsBinding.inflate(
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
    class ViewHolder(private val binding: ItemRecyclerAboutUsBinding) :
        RecyclerView.ViewHolder(binding.cvAboutUs) {

        /**
         * Bind
         * Used to connect items to layout
         *
         * @param value the item
         * @param listener a function to invoke when the item in clicked
         */
        internal fun bind(
            value: MemberUI,
            listener: ((MemberUI) -> Unit)?,
        ) {
            with(binding) {
                //Set texts
                tvName.text = value.name
                tvDescription.text = value.description
                //Load the image url and set it on this ImageView
                imgMember.load(value.photo)

                cvAboutUs.setOnClickListener {
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
    class DiffUtilCallback : DiffUtil.ItemCallback<MemberUI>() {
        override fun areItemsTheSame(oldItem: MemberUI, newItem: MemberUI): Boolean =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: MemberUI, newItem: MemberUI): Boolean =
            oldItem == newItem

    }
}
