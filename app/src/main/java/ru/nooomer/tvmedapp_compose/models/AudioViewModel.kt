package ru.nooomer.tvmedapp_compose.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.nooomer.tvmedapp_compose.api.models.SoundDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.ssm
import java.util.UUID

class AudioViewModel : ViewModel(), PreferenceDataType {
	private val treatmentId: UUID = UUID.fromString(ssm.fetch(CLICKED_TREATMENT_ID).toString())
	private val audioFlow = AudioModelView(treatmentId).audioFlow


	private val _list = MutableStateFlow(listOf<SoundDto>())
	val audio: StateFlow<List<SoundDto>> get() = _list


	init {
		getAudioData()
	}

	fun getAudioData() {
		viewModelScope.launch {
			withContext(Dispatchers.Default) {
				var audioList: MutableList<SoundDto>? = null
				audioFlow.collect {
					audioList = it.toMutableList()
				}
				_list.tryEmit(audioList!!)
			}
		}
	}
}