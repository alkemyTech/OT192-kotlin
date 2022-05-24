package com.melvin.ongandroid.view.adapters

import android.text.Html
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
import com.melvin.ongandroid.databinding.NovedadesItemBinding
import com.melvin.ongandroid.model.News

class NovedadesAdapter : ListAdapter<News, NovedadesHolder>(ComparadorNovedades()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovedadesHolder {
        return NovedadesHolder.create(parent)
    }
    var onClickArrow: (() -> Unit)? = null

    override fun onBindViewHolder(holder: NovedadesHolder, position: Int) {
        val novedad = getItem(position)

        // si es el últmo elemento, esconder la cardview, y crear un nuevo imageView con
        // la flecha para mas contenido.
        // para mejorar la visibilidad, cambiamos las dimensiones del layout
        if (position == itemCount - 1) {
            holder.binding.apply {

                carviewNovedades.visibility = View.GONE

                val backImage = ImageView(holder.itemView.context)
                backImage.id = View.generateViewId()


                constraintItemNovedades.layoutParams =
                    ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    )
                        .apply {
                            circleConstraint = backImage.id
                            topMargin = 200
                        }

                backImage.setImageResource(R.drawable.ic_arrow)

                backImage.layoutParams = ConstraintLayout.LayoutParams(
                    200,
                    200
                )

                // add setOnClickListener to the arrow image
                backImage.setOnClickListener{
                    onClickArrow?.invoke()
                }
                constraintItemNovedades.addView(backImage)


            }
        } else {

            holder.binding.apply {
                imageViewNovedades.load(novedad.image)
                textViewNovedades.text = Html.fromHtml(novedad.content)
            }
        }


    }
}

class NovedadesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding: NovedadesItemBinding = NovedadesItemBinding.bind(itemView)

    companion object {
        fun create(parent: ViewGroup): NovedadesHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NovedadesItemBinding.inflate(layoutInflater, parent, false)

            return NovedadesHolder(binding.root)

        }
    }

}

class ComparadorNovedades : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.image == newItem.image
    }

}
