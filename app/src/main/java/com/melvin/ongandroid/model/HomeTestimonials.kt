package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Home testimonials
 *
 * @property id id of the testimonial
 * @property name name of the testimonial
 * @property imgUrl  image url string
 * @property description description of the testimonial
 * @constructor Create empty Home testimonials
 */
data class HomeTestimonials(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("image") val imgUrl: String,
    @SerializedName("description") val description: String
)