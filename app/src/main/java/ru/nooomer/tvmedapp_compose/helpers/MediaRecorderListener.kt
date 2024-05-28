package ru.nooomer.tvmedapp_compose.helpers

import android.media.MediaRecorder
import android.media.MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED
import android.media.MediaRecorder.OnInfoListener
import ru.nooomer.tvmedapp_compose.stopRecording

class MediaRecorderListener : OnInfoListener {
	override fun onInfo(mr: MediaRecorder?, what: Int, extra: Int) {
		if (what == MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
			stopRecording()
		}
	}
}