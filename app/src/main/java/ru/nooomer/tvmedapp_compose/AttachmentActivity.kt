package ru.nooomer.tvmedapp_compose

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.nooomer.tvmedapp_compose.api.API.storeAudio
import ru.nooomer.tvmedapp_compose.api.models.PredictDto
import ru.nooomer.tvmedapp_compose.api.models.SoundDto
import ru.nooomer.tvmedapp_compose.helpers.AppVoicePlayer
import ru.nooomer.tvmedapp_compose.helpers.MediaRecorderListener
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.models.AudioViewModel
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
import java.io.File
import java.io.IOException

private lateinit var player: AppVoicePlayer
private lateinit var mediaRecorder: MediaRecorder
private var voisesCount = 0
private var state = false
private fun <T> CoroutineScope.asyncIO(ioFun: () -> T) = async(Dispatchers.IO) { ioFun() }
private lateinit var contex: Context
private lateinit var audioVm: AudioViewModel
private val scope = CoroutineScope(Dispatchers.Main + Job())

class AttachmentActivity : ComponentActivity(), PreferenceDataType {
	private val audioViewModel by viewModels<AudioViewModel>()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			TvMedApp_composeTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					audioVm = audioViewModel
					contex = LocalContext.current
					player = AppVoicePlayer(contex)
					AttachmentScreen(audioViewModel, contex)
				}
			}
		}
	}
}

fun setMediaRecorder(context: Context) {
	context.openFileOutput(
		"${ssm.fetch("clicked_treatment_id").toString()}_${voisesCount + 1}.mp3",
		0
	)
	val output = "${context.filesDir.absolutePath}/${
		ssm.fetch("clicked_treatment_id").toString()
	}_${voisesCount + 1}.mp3"
	mediaRecorder.apply {
		setAudioSource(MediaRecorder.AudioSource.MIC)
		setOutputFormat(MediaRecorder.OutputFormat.OGG)
		setAudioEncoder(MediaRecorder.AudioEncoder.OPUS)
		setAudioSamplingRate(48000)
		setAudioEncodingBitRate(220000)
		setOutputFile(output)
		setMaxDuration(20000)
		setOnInfoListener(MediaRecorderListener())
	}
}


@Composable
fun AttachmentScreen(audioViewModel: AudioViewModel, contex: Context) {

	val audio by audioViewModel.audio.collectAsStateWithLifecycle()
	val error = remember { mutableStateOf(false) }
	if (audio.isNotEmpty()) {
		Scaffold { paddingValues ->
			Column {
				if (!error.value) {
					VoicesList(paddingValues, audio, audioViewModel, contex)
				} else {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						Text(stringResource(R.string.an_error_occurred_try_again_later))
					}
				}
				Row(
					modifier = Modifier.fillMaxSize(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.Bottom
				) {
					MicButton(contex)
				}
			}
		}
	} else {
		Box(
			modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
		) {
			Text(
				text = "This treatment haven't audio", fontSize = 24.sp
			)
		}
	}
}

@Composable
private fun MicButton(contex: Context) {
	FloatingActionButton(
		onClick = {
			recordAudio(contex)
		},
	) {
		Icon(
			ImageVector.vectorResource(R.drawable.baseline_mic_24),
			contentDescription = null,
			tint = Color.Black
		)

	}
}

fun audioPermission(contex: Context): Boolean {
	return if (ContextCompat.checkSelfPermission(
			contex, android.Manifest.permission.RECORD_AUDIO
		) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
			contex,
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
		) != PackageManager.PERMISSION_GRANTED
	) {
		val permissions = arrayOf(
			android.Manifest.permission.RECORD_AUDIO,
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
			android.Manifest.permission.READ_EXTERNAL_STORAGE
		)
		ActivityCompat.requestPermissions(contex as Activity, permissions, 0)
		false
	} else {
		true
	}
}


fun recordAudio(contex: Context) {
	if (audioPermission(contex)) {
		mediaRecorder = MediaRecorder(contex)
		setMediaRecorder(contex)
		startRecording(contex)
	}
}

private fun startRecording(contex: Context) {
	try {
		mediaRecorder.prepare()
		mediaRecorder.start()
		state = true
		Toast.makeText(contex, "Recording started!", Toast.LENGTH_SHORT).show()
	} catch (e: IllegalStateException) {
		e.printStackTrace()
	} catch (e: IOException) {
		e.printStackTrace()
	}
}


fun stopRecording() {
	if (state) {
		mediaRecorder.stop()
		mediaRecorder.release()
		Toast.makeText(contex, "Recording stop!", Toast.LENGTH_SHORT).show()
		state = false
		scope.launch {
			scope.asyncIO {
				storeAudio(
					ssm.fetch("clicked_treatment_id").toString(),
					File(
						"${contex.filesDir.absolutePath}/${
							ssm.fetch("clicked_treatment_id").toString()
						}_${voisesCount + 1}.mp3"
					)
				)
			}.join()
			audioVm.getAudioData()
		}
	} else {
		Toast.makeText(contex, "You are not recording right now!", Toast.LENGTH_SHORT).show()
	}
}

@Composable
fun VoicesList(
	paddingValues: PaddingValues,
	audios: List<SoundDto>,
	audioViewModel: AudioViewModel,
	contex: Context
) {
	var playing = false
	var predictedResult = remember { mutableStateListOf<PredictDto>() }
	val maximum = remember { mutableStateOf(PredictDto("", 0.0)) }
	val lastClickedCount = remember { mutableIntStateOf(0) }
	voisesCount = audios.size
	LazyColumn(Modifier.padding(paddingValues)) {
		val audiosNew = audios.sortedBy { it.createdDate }
		items(audiosNew, SoundDto::id) { audio ->
			Row {
				IconButton(modifier = Modifier, onClick = {
					if (!player.playing) {
						player.play(
							ssm.fetch("clicked_treatment_id").toString(),
							audio.soundLink.split("_")[1].split(".")[0].toInt()
						)
						lastClickedCount.intValue = 0
					} else {
						player.stop()
						maximum.value = player.getPredictResult()
						lastClickedCount.intValue =
							audio.soundLink.split("_")[1].split(".")[0].toInt()
						println("last clicked count^ $lastClickedCount")
					}
				}) {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.baseline_attach_file_24),
						contentDescription = null,
						tint = Color.Black
					)
				}
				Column {
					Text("№" + audio.soundLink.split("_")[1].split(".")[0])
					Text("Дата отправки" + audio.createdDate)
					if ((lastClickedCount.intValue.toString() == audio.soundLink.split("_")[1].split(
							"."
						)[0])
					) {
						Text("${maximum.value.label} - ${maximum.value.value * 100}%")
					} else {
						Text("Еще не считалось, воспроизведите запись")
					}
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!", modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
	TvMedApp_composeTheme {
		Greeting("Android")
	}
}