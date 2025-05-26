package com.ministore.presentation.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

/**
 * Enhanced Toast Manager with different types of notifications
 */
class ToastManager(private val context: Context) {
    fun showSuccess(message: String) {
        showCustomToast(message, Toast.LENGTH_SHORT)
    }

    fun showError(message: String) {
        showCustomToast(message, Toast.LENGTH_LONG)
    }

    fun showInfo(message: String) {
        showCustomToast(message, Toast.LENGTH_SHORT)
    }

    fun showWarning(message: String) {
        showCustomToast(message, Toast.LENGTH_LONG)
    }

    fun showSuccess(@StringRes messageResId: Int) {
        showCustomToast(context.getString(messageResId), Toast.LENGTH_SHORT)
    }

    fun showError(@StringRes messageResId: Int) {
        showCustomToast(context.getString(messageResId), Toast.LENGTH_LONG)
    }

    fun showInfo(@StringRes messageResId: Int) {
        showCustomToast(context.getString(messageResId), Toast.LENGTH_SHORT)
    }

    fun showWarning(@StringRes messageResId: Int) {
        showCustomToast(context.getString(messageResId), Toast.LENGTH_LONG)
    }

    fun showFormattedSuccess(@StringRes messageResId: Int, vararg args: Any) {
        showCustomToast(context.getString(messageResId, *args), Toast.LENGTH_SHORT)
    }

    fun showFormattedError(@StringRes messageResId: Int, vararg args: Any) {
        showCustomToast(context.getString(messageResId, *args), Toast.LENGTH_LONG)
    }

    private fun showCustomToast(message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}

/**
 * CompositionLocal to provide ToastManager throughout the app
 */
val LocalToastManager = staticCompositionLocalOf<ToastManager> {
    error("No ToastManager provided")
}

/**
 * Composable utility function to show toasts
 */
@Composable
fun rememberToastManager(): ToastManager {
    val context = LocalContext.current
    return ToastManager(context)
} 