package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.R
import com.melvin.ongandroid.databinding.ItemRecyclerHomeTestimonialsBinding
import com.melvin.ongandroid.model.HomeTestimonials

class HomeTestimonialsItemAdapter : ListAdapter<HomeTestimonials, HomeTestimonialsHolder>(ComparadorHomeTestimonials()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTestimonialsHolder {
        return HomeTestimonialsHolder.create(parent)
    }
    var onClickArrow: (() -> Unit)? = null

    override fun onBindViewHolder(holder: HomeTestimonialsHolder, position: Int) {
        val testimonials = getItem(position)

        // si es el últmo elemento, esconder la cardview, y crear un nuevo imageView con
        // la flecha para mas contenido.
        // para mejorar la visibilidad, cambiamos las dimensiones del layout
        if (position == itemCount - 1) {
            holder.binding.apply {

                cvTestimonials.visibility = View.GONE

                val backImage = ImageView(holder.itemView.context)
                backImage.id = View.generateViewId()


                constraintItemTestimonials.layoutParams =
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    )
                        .apply {
                            circleConstraint = backImage.id
                            topMargin = 300
                        }

                backImage.setImageResource(R.drawable.ic_arrow)

                backImage.layoutParams = ConstraintLayout.LayoutParams(
                    200,
                    200,
                )

                // add setOnClickListener to the arrow image
                backImage.setOnClickListener{
                    onClickArrow?.invoke()
                }
                constraintItemTestimonials.addView(backImage)


            }
        } else {

            holder.binding.apply {
                // Set Name
                tvTestimonialsName.text = testimonials.name
                // Set Description
                tvTestimonialsHeading.text = testimonials.description
                // Load the image url and set it on this ImageView
                imgTestimonials.load(testimonials.imgUrl)

            }
        }


    }
}

class HomeTestimonialsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: ItemRecyclerHomeTestimonialsBinding = ItemRecyclerHomeTestimonialsBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): HomeTestimonialsHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemRecyclerHomeTestimonialsBinding.inflate(layoutInflater, parent, false)

            return HomeTestimonialsHolder(binding.root)

        }
    }

}

class ComparadorHomeTestimonials : DiffUtil.ItemCallback<HomeTestimonials>() {
    override fun areItemsTheSame(oldItem: HomeTestimonials, newItem: HomeTestimonials): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: HomeTestimonials, newItem: HomeTestimonials): Boolean {
        return oldItem.imgUrl == newItem.imgUrl
    }

}
