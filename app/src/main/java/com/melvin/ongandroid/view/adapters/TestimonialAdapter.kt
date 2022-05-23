package com.melvin.ongandroid.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.melvin.ongandroid.databinding.ItemRowTestimonialBinding
import com.melvin.ongandroid.databinding.NovedadesItemBinding
import com.melvin.ongandroid.model.Testimonial

class TestimonialAdapter: ListAdapter<Testimonial, TestimonialViewHolder>(ComparadorTestimonios()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestimonialViewHolder {
        return TestimonialViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TestimonialViewHolder, position: Int) {
        val testimonio = getItem(position)

        holder.binding.apply {
            imageViewItemTestimonial.load(testimonio.image)
            textViewItemTestimonialName.text = testimonio.name
            textViewTiemTestDescripcion.text = testimonio.description

        }
    }
}

class TestimonialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemRowTestimonialBinding.bind(itemView)

    companion object{
        fun create(parent:ViewGroup):TestimonialViewHolder{
            val binding = ItemRowTestimonialBinding.inflate(LayoutInflater.from(parent.context)
                , parent, false)
            return TestimonialViewHolder(binding.root)

        }
    }

}

class ComparadorTestimonios(): DiffUtil.ItemCallback<Testimonial>() {
    override fun areItemsTheSame(oldItem: Testimonial, newItem: Testimonial): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Testimonial, newItem: Testimonial): Boolean {
        return oldItem.id == newItem.id
    }

}
