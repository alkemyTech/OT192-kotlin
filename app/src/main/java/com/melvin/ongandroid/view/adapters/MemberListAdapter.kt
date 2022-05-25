package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerAboutUsBinding
import com.melvin.ongandroid.model.MemberUI
import com.melvin.ongandroid.model.Members
import com.melvin.ongandroid.utils.convertHtmlToString

/**
 * Class for ListAdapter that replaces [MemberItemAdapter].
 * Now uses [Members] model from api response. Replaces
 * [MemberUI] used to mock responseC
 */
class MemberListAdapter() :
    ListAdapter<Members, RecyclerView.ViewHolder>
        (DiffUtilCallback()) {

    var onItemClicked: ((Members) -> Unit)? = null

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
            value: Members,
            listener: ((Members) -> Unit)?,
        ) {
            with(binding) {
                //Set texts
                tvName.text = value.name
                tvDescription.text = value.description.convertHtmlToString().trim()
                //Load the image url and set it on this ImageView
                imgMember.load(value.image)

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
    class DiffUtilCallback : DiffUtil.ItemCallback<Members>(){
        override fun areItemsTheSame(oldItem: Members, newItem: Members): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Members, newItem: Members): Boolean {
            return oldItem.id == newItem.id
        }

    }
}