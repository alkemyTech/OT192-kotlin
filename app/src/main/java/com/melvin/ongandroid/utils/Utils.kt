package com.melvin.ongandroid.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.util.PatternsCompat

/*
Function that Checks if a String is a valid form of email.
It was changed to Jetpack PatternsCompat because of Patterns throws npe in tests
 */
fun String.isEmailValid():Boolean =
    PatternsCompat.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Is password valid
 * created on 5 May 2022 by Leonel Gomez
 *
 * @return true if password is valid
 */
fun String.isPasswordValid(): Boolean =
    PatternsLogIn.PASSWORD
        .matcher(this)
        .matches()

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

/**
 * Convert HTML to string
 * Return the displayable styled text from the provided HTML string
 * created on 24 April 2022 by Leonel Gomez
 * modified on 17 May 2022 by Leonel Gomez to manage nullity
 *
 */
fun String?.convertHtmlToString(): String {
    if (this == null)
        return ""
    //As fromHtml is deprecated, whe need to ask for Build version to includes the new int parameter
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }
}

fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}

fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

fun openWebPage(url: String, context: Context) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    context.startActivity(intent)
}

/**
 * Hide keyboard
 * created on 5 May 2022 by Leonel Gomez
 *
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}