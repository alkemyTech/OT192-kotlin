package com.melvin.ongandroid.model

import com.melvin.ongandroid.utils.convertHtmlToString

/**
 * MemberUI
 * created on 30 April 2022 by Leonel Gomez
 *
 * @property name string
 * @property photo string image url
 * @property description string
 * @constructor Create empty Member UI object
 */
data class MemberUI(
    var name: String = "",
    var photo: String = "",
    var description: String = "",
    var facebookUrl: String = "",
    var linkedinUrl: String = "",
)

/**
 * To UI
 * extension function to convert 'Member' object received by API to "MemberUI" object needed in the UI
 * created on 30 April 2022 by Leonel Gomez
 *
 * @return a Member object with the attributes needed by the UI
 */
/* TODO: Remove comments after implementing Member (Domain) object
fun Member.toUI(): MemberUI = MemberUI(
    name = name,
    photo = image,
    description = description.convertHtmlToString(),
    facebookUrl = facebookUrl,
    linkedinUrl = linkedinUrl,
)
 */