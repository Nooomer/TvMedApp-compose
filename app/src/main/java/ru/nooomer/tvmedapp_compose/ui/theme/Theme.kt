package ru.nooomer.tvmedapp_compose.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val cardCollapsedBackgroundColor = Color(0xFFFEFFFD)
val cardExpandedBackgroundColor = Color(0xFFFFDA6D)
var textFieldColor: TextFieldColors? = null

private val DarkColorScheme = darkColorScheme(
	primary = Purple80,
	secondary = PurpleGrey80,
	tertiary = Pink80,
	background = Color.Black
)

private val LightColorScheme = lightColorScheme(
	primary = Purple40,
	secondary = PurpleGrey40,
	tertiary = Pink40,
	background = Color.LightGray
	/* Other default colors to override
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/
)

@Composable
fun TvMedApp_composeTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	// Dynamic color is available on Android 12+
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	textFieldColor = TextFieldDefaults.colors(
		focusedTextColor = Color.Black,
		unfocusedTextColor = Color.Black,
		errorTextColor = Color.Black,
		focusedContainerColor = Color.LightGray,
		unfocusedContainerColor = Color.LightGray,
		disabledContainerColor = Color.LightGray,
		errorContainerColor = Color.LightGray,
		errorCursorColor = Color.Red,
		focusedIndicatorColor = Color.Transparent,
		unfocusedIndicatorColor = Color.Transparent,
		disabledIndicatorColor = Color.Transparent,
		errorIndicatorColor = Color.Transparent,
		errorLabelColor = Color.Red,
	)
	val colorScheme = when {
		dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
			val context = LocalContext.current
			if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
		}

		darkTheme -> DarkColorScheme
		else -> LightColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.primary.toArgb()
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}