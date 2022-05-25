package com.melvin.ongandroid.model


import com.google.gson.annotations.SerializedName

data class News(
    var id: Int = 0,
    var name: String = "",
    var slug: Any = Any(),
    var content: String = "",
    var image: String = "",
    @SerializedName("user_id")
    var userId: String? = "" ,
    @SerializedName("category_id")
    var categoryId: Int? = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("deleted_at")
    var deletedAt: Any = Any(),
    @SerializedName("group_id")
    var groupId: Any = Any()
)