package com.melvin.ongandroid.utils

/*
Function that Checks if a String is a valid form of email.
 */
fun String.isEmailValid():Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 *Checks if a String is not empyt, and it length is greater or equal to 3(for names like Ana)
 * and all char are letters(This prevent numbers or other char)
 */

fun String.checkFirstOrLastName() =
    this.isNotEmpty() && this.length >= 3 && this.all { it.isLetter() }

/**
 * Check if the contact Message is greater to 30 char.
 */

fun String.checkContactMessage(): Boolean = this.isNotEmpty() && this.length >= 30