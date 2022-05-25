package com.melvin.ongandroid.model

import com.google.gson.annotations.SerializedName

/**
 * Activity
 * created on 1 May 2022 by Leonel Gomez
 *
 * @property id
 * @property name
 * @property slug
 * @property description
 * @property image
 * @property userId
 * @property categoryId
 * @property createdAt
 * @property updatedAt
 * @property deletedAt
 * @property groupId
 * @constructor Create empty Activity
 */
data class Activity(
    var id: Int = 0,
    var name: String? = "",
    var slug: Any? = null,
    var description: String? = "",
    var image: String? = "",
    @SerializedName("user_id")
    var userId: String? = "",
    @SerializedName("category_id")
    var categoryId: Int? = 0,
    @SerializedName("created_at")
    var createdAt: String? = "",
    @SerializedName("updated_at")
    var updatedAt: String? = "",
    @SerializedName("deleted_at")
    var deletedAt: String? = "",
    @SerializedName("group_id")
    var groupId: String? = "",
)