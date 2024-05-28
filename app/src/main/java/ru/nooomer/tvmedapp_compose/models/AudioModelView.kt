package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nooomer.tvmedapp_compose.api.API
import ru.nooomer.tvmedapp_compose.api.models.SoundDto
import java.util.UUID

class AudioModelView(treatmentId: UUID) : ViewModel() {
	val audioFlow: Flow<MutableSet<SoundDto>> = flow {
		emit(API.getAllAudio(treatmentId))
	}
}