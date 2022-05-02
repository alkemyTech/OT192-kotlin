package com.melvin.ongandroid.model

/**
 * ActivityUI
 * created on 1 May 2022 by Leonel Gomez
 *
 * @property name
 * @property description
 * @property image
 * @constructor Create empty Activity UI
 */
data class ActivityUI(
    var name: String = "",
    var description: String = "",
    var image: String = "",
)

/**
 * To UI
 * extension function to convert 'Activity' object received by API to "ActivityUI" object needed in the UI
 * created on 1 May 2022 by Leonel Gomez
 *
 * @return a Activity object with the attributes needed by the UI
 */
/* TODO: Remove comments after implementing Activity (Domain) object
fun Activity.toUI(): ActivityUI = ActivityUI(
    name = name,
    description = description,
    image = image,
)
 */