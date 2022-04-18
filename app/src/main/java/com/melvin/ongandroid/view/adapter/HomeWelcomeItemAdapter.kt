package com.melvin.ongandroid.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRecyclerHomeWelcomeBinding
import com.melvin.ongandroid.model.HomeWelcome
import kotlin.properties.Delegates

/**
 * Home welcome item adapter
 *
 * @constructor Create empty Home welcome item adapter
 */
class HomeWelcomeItemAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items: List<HomeWelcome> by Delegates.observable(emptyList()) { _, _, _ -> notifyDataSetChanged() }
    var onItemClicked: ((HomeWelcome) -> Unit)? = null

    /**
     * On create view holder
     *
     * @param parent
     * @param viewType
     * @return constructor of ViewHolder referencing inflated layout
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemRecyclerHomeWelcomeBinding.inflate(
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
        viewHolder.bind(items[position], onItemClicked)
    }

    /**
     * Set data
     * To store a mutable list in the adapter
     *
     * @param data is a mutable list of HomeWelcome object
     */
    fun setData(data: MutableList<HomeWelcome>) {
        this.items = data
    }

    /**
     * Get item count
     *
     * @return the size of the list of items
     */
    override fun getItemCount(): Int = items.size

    /**
     * View holder
     *
     * @property binding reference to item layout
     * @constructor Create empty View holder
     */
    class ViewHolder(private val binding: ItemRecyclerHomeWelcomeBinding) :
        RecyclerView.ViewHolder(binding.cvWelcome) {

        /**
         * Bind
         * Used to connect items to layout
         *
         * @param value the item
         * @param listener a function to invoke when the item in clicked
         */
        internal fun bind(
            value: HomeWelcome,
            listener: ((HomeWelcome) -> Unit)?,
        ) {
            with(binding) {
                //Set texts
                tvTitle.text = value.title
                tvDescription.text = value.description
                //Load the image url and set it on this ImageView
                imgWelcome.load(value.imgUrl)

                cvWelcome.setOnClickListener {
                    listener?.invoke(value)
                }
            }
        }
    }
}
