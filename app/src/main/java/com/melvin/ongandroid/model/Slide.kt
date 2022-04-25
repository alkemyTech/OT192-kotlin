package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Slide
 * Data class to get a list of slides
 * created on 24 April 2022 by Leonel Gomez
 *
 * @property id    integer($int32)
 * @property name   string
 * @property description    string
 * @property image  string
 * @property order  integer($int32) update: some responses are null
 * @property userId integer($int32) update: some responses are null
 * @property createdAt  string($date-time)
 * @property updatedAt  string($date-time)
 * @property deletedAt  string($date-time) update: some responses are null
 * @constructor Create empty Slide
 */
data class Slide(
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var image: String = "",
    var order: Int? = 0,
    @SerializedName("user_id")
    var userId: Int? = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("deleted_at")
    var deletedAt: String? = "",
)