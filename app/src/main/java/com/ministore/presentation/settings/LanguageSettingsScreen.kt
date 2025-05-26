package com.ministore.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ministore.R
import java.util.*

@Composable
fun LanguageSettingsScreen(
    viewModel: LanguageSettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text(text = stringResource(R.string.settings_language)) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language options
        LanguageOption(
            name = "English",
            locale = "en",
            isSelected = currentLanguage == "en",
            onSelect = { viewModel.setLanguage(context, "en") }
        )

        LanguageOption(
            name = "Español",
            locale = "es",
            isSelected = currentLanguage == "es",
            onSelect = { viewModel.setLanguage(context, "es") }
        )

        LanguageOption(
            name = "العربية",
            locale = "ar-MA",
            isSelected = currentLanguage == "ar-MA",
            onSelect = { viewModel.setLanguage(context, "ar-MA") }
        )

        LanguageOption(
            name = "简体中文",
            locale = "zh-CN",
            isSelected = currentLanguage == "zh-CN",
            onSelect = { viewModel.setLanguage(context, "zh-CN") }
        )
    }
}

@Composable
private fun LanguageOption(
    name: String,
    locale: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name)
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
} 