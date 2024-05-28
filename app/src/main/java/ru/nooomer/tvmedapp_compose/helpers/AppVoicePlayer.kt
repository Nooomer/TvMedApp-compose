package ru.nooomer.tvmedapp_compose.helpers

import android.content.Context
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.nooomer.tvmedapp_compose.api.API
import ru.nooomer.tvmedapp_compose.api.models.PredictDto
import java.io.File

class AppVoicePlayer(private val context: Context) {

	private lateinit var mMediaPlayer: MediaPlayer
	private lateinit var mFile: File
	private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
	var playing = false
	var downloaded = false
	var predicted: PredictDto = PredictDto("", 0.0)

	fun play(treatmentId: String, count: Int) {
		downloaded = false
		context.openFileOutput("${treatmentId}_${count}.mp3", 0)
		mFile = File("${context.filesDir.absolutePath}/${treatmentId}_${count}.mp3")
		println(mFile.absoluteFile)
		println(mFile.exists())
		println(mFile.length())
		println(mFile.isFile)
		if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
			println("start play")
			startPlay()
		} else {
			println("download")
			val scope = CoroutineScope(Dispatchers.Main + Job())
			scope.launch {
				val def = scope.asyncIO {
					API.getOneAudio(treatmentId, count, context.filesDir.absolutePath)
				}
				def.await()
				println("downloaded")
				mFile = File(
					"${context.filesDir.absolutePath}/${treatmentId}_${count}.mp3"
				)
				startPlay()
				println("started")
			}
		}
	}

	private fun getVoicePredict() {
		val scope = CoroutineScope(Dispatchers.Main + Job())
		var predRes: List<PredictDto> = listOf()
		scope.launch {
			scope.asyncIO {
				predRes = API.getPredict(
					mFile, context
				)!!
			}.join()
			println("Predicted: $predRes")
			predRes.forEach {
				if (it.value > predicted.value) {
					predicted = it
				}
			}
		}
	}

	fun getPredictResult(): PredictDto {
		return predicted
	}

	private fun startPlay() {
		init()
		println(mFile.absolutePath)
		mMediaPlayer.setDataSource(mFile.absolutePath)
		mMediaPlayer.prepare()
		mMediaPlayer.start()
		println("playing")
		playing = !playing
		downloaded = !downloaded
		getVoicePredict()
		mMediaPlayer.setOnCompletionListener {
			stop()
			release()
		}
	}

	fun stop() {
		mMediaPlayer.stop()
		mMediaPlayer.reset()
		release()
		playing = !playing
	}

	private fun release() {
		mMediaPlayer.release()
	}

	private fun init() {
		mMediaPlayer = MediaPlayer()
	}
}