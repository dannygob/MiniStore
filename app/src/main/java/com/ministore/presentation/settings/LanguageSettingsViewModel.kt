package com.ministore.presentation.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import android.app.Activity
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class LanguageSettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(getCurrentLanguage())
    val currentLanguage: StateFlow<String> = _currentLanguage

    private fun getCurrentLanguage(): String {
        return preferences.getString(LANGUAGE_KEY, Locale.getDefault().language) ?: "en"
    }

    fun setLanguage(context: Context, languageCode: String) {
        viewModelScope.launch {
            preferences.edit().putString(LANGUAGE_KEY, languageCode).apply()
            _currentLanguage.value = languageCode
            updateResources(context, languageCode)

            // Recreate all activities to apply the new language
            (context as? Activity)?.recreate()
        }
    }

    private fun updateResources(context: Context, language: String) {
        val locale = when (language) {
            "en" -> Locale("en")
            "es" -> Locale("es")
            "ar-MA" -> Locale("ar", "MA")
            "zh-CN" -> Locale("zh", "CN")
            else -> Locale("en")
        }
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    companion object {
        private const val LANGUAGE_KEY = "app_language"
    }
} 