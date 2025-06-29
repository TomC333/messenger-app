package com.sgvas21.ddadi21.messengerapp.utils

import android.content.Context
import androidx.core.content.edit

object SessionManager {
    private const val PREF_NAME = "session"
    private const val KEY_SIGNED_IN = "signed_in"
    private const val KEY_USERNAME = "username"

    /**
     * Saves the signed-in status in SharedPreferences.
     *
     * @param context Context to access SharedPreferences.
     * @param signedIn Boolean indicating if the user is signed in.
     */
    fun saveSignedIn(context: Context, signedIn: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putBoolean(KEY_SIGNED_IN, signedIn)
            }
    }

    /**
     * Returns whether the user is currently signed in.
     *
     * @param context Context to access SharedPreferences.
     *
     * @return True if the user is signed in, false otherwise.
     */
    fun isSignedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_SIGNED_IN, false)
    }

    /**
     * Saves the signed-in user's username in SharedPreferences.
     *
     * @param context Context to access SharedPreferences.
     * @param username The username string to save.
     */
    fun saveUsername(context: Context, username: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_USERNAME, username)
            }
    }

    /**
     * Retrieves the saved username of the signed-in user.
     *
     * @param context Context to access SharedPreferences.
     *
     * @return The saved username, or null if none exists.
     */
    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
    }

    /**
     * Clears all saved session data (signed-in status and username).
     *
     * @param context Context to access SharedPreferences.
     */
    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                clear()
            }
    }
}