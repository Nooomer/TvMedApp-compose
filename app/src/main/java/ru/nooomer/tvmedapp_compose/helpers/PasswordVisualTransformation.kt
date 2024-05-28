package ru.nooomer.tvmedapp_compose.helpers

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation


class PasswordVisualTransformation(private val mask: Char = '\u2022') : VisualTransformation {
	override fun filter(text: AnnotatedString): TransformedText {
		return TransformedText(
			text = AnnotatedString(mask.toString().repeat(text.text.length)),
			offsetMapping = object : OffsetMapping {

				override fun originalToTransformed(offset: Int): Int = offset

				override fun transformedToOriginal(offset: Int): Int = offset
			}
		)
	}
}