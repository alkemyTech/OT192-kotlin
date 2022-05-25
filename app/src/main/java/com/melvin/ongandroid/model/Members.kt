package com.melvin.ongandroid.model


import com.google.gson.annotations.SerializedName

data class Members(
    var id: Int = 0,
    var name: String = "",
    var image: String = "",
    var description: String = "",
    var facebookUrl: String = "",
    var linkedinUrl: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("updated_at")
    var updatedAt: String = "",
    @SerializedName("deleted_at")
    var deletedAt: String? = "",
    @SerializedName("group_id")
    var groupId: Any = Any()
)