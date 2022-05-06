package com.melvin.ongandroid.utils

import java.util.regex.Pattern

/**
 * Patterns log in
 * Regular expression patterns used in Log In
 * created on 5 May 2022 by Leonel Gomez
 *
 * @constructor Create empty Patterns log in
 */
object PatternsLogIn {

    //Regular expression pattern to validate password
    val PASSWORD: Pattern = Pattern.compile(
        "^" + //The password string will start this way
                "(?=.*[0-9])" + //The string must contain at least 1 numeric character
                "(?=.*[a-z])" + //The string must contain at least 1 lowercase alphabetical character
                "(?=.*[A-Z])" + //The string must contain at least 1 uppercase alphabetical character
                "(?=.*[!@#\$%^&*])" + //The string must contain at least one special character, but we are escaping reserved RegEx characters to avoid conflict
                ".{8,}" + //The string must be eight characters or longer
                "$"
    )
}