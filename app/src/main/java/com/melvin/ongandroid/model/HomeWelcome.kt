package com.melvin.ongandroid.model
import com.melvin.ongandroid.utils.convertHtmlToString


/**
 * Home welcome
 * See a presentation of the activities carried out by SOMOS MAS
 *
 * @property title
 * @property imgUrl image url string
 * @property description
 * @constructor Create empty HomeWelcome object
 */
data class HomeWelcome(
    var title: String = "",
    var imgUrl: String = "",
    var description: String = ""
)

/**
 * To UI
 * extension function to convert 'Slide' object received by API to "HomeWelcome" object needed in the UI
 * created on 24 April 2022 by Leonel Gomez
 *
 * @return a HomeWelcome object with the attributes needed by the UI
 */
fun Slide.toUI(): HomeWelcome = HomeWelcome(
    title = name.convertHtmlToString(),
    imgUrl = image,
    description = description.convertHtmlToString(),
)
