package ru.nooomer.tvmedapp_compose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.android.ext.android.inject
import ru.nooomer.tvmedapp_compose.RetrofitService.SessionManager
import ru.nooomer.tvmedapp_compose.api.models.SymptomDto
import ru.nooomer.tvmedapp_compose.api.models.TreatmentDto
import ru.nooomer.tvmedapp_compose.interfaces.PreferenceDataType
import ru.nooomer.tvmedapp_compose.models.CardsViewModel
import ru.nooomer.tvmedapp_compose.models.SymptomViewModel
import ru.nooomer.tvmedapp_compose.models.TreatmentModelView
import ru.nooomer.tvmedapp_compose.ui.theme.TvMedApp_composeTheme
import ru.nooomer.tvmedapp_compose.ui.theme.cardCollapsedBackgroundColor
import ru.nooomer.tvmedapp_compose.ui.theme.cardExpandedBackgroundColor
import java.util.UUID

var data = mutableListOf<MutableList<String?>>()

class TreatmentActivity : ComponentActivity(), PreferenceDataType {
	private val cardsViewModel by viewModels<CardsViewModel>()
	private val symptomViewModel by viewModels<SymptomViewModel>()
	private val ssm by inject<SessionManager>()
	private val treatmentFlow = TreatmentModelView().treatmentFlow
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TvMedApp_composeTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
				) { // the key define when the block is relaunched // Your coroutine code her
					CardsScreen(cardsViewModel)
					LogoutButton()
					AddNewTreatmentButton()
				}
			}
		}
	}

	@Composable
	private fun AddNewTreatmentButton() {
		var alertDialog = remember { mutableStateOf(false) }
		Column(
			modifier = Modifier
				.padding(10.dp),
			horizontalAlignment = Alignment.End,
			verticalArrangement = Arrangement.Bottom,
		) {
			ExtendedFloatingActionButton(modifier = Modifier, onClick = {
				alertDialog.value = true
			}) {
				Icon(
					imageVector = Icons.Default.Add, contentDescription = null, tint = Color.Black
				)
			}
		}
		if (alertDialog.value) {
			alertDialog.value = Alert()
		}
	}

	@Composable
	private fun LogoutButton() {
		Column(
			modifier = Modifier.padding(all = 10.dp),
			horizontalAlignment = Alignment.Start,
			verticalArrangement = Arrangement.Bottom,
		) {
			ExtendedFloatingActionButton(onClick = {
				ssm.clearSession()
				this@TreatmentActivity.startActivity(
					Intent(
						this@TreatmentActivity, MainActivity::class.java
					)
				)
				val activity = (this@TreatmentActivity as? Activity)
				activity?.finish()
			}) {
				Icon(
					imageVector = Icons.Default.ExitToApp,
					contentDescription = null,
					tint = Color.Black
				)
			}
		}
	}

	@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
	@Composable
	fun CardsScreen(viewModel: CardsViewModel) {
		val cards by viewModel.cards.collectAsStateWithLifecycle()
		val expandedCardIds by viewModel.expandedCardIdsList.collectAsStateWithLifecycle()
		val error = remember { mutableStateOf(false) }
		if (cards!!.isNotEmpty()) {
			Scaffold { paddingValues ->
				if (!error.value) {
					LazyColumn(Modifier.padding(paddingValues)) {
						items(cards!!, TreatmentDto::id) { card ->
							ExpandableCard(
								card = card,
								onCardArrowClick = { viewModel.onCardArrowClicked(card.id) },
								expanded = expandedCardIds!!.contains(card.id),
							)
						}
					}
				} else {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						Text(stringResource(R.string.an_error_occurred_try_again_later))
					}
				}
			}
		} else {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = stringResource(R.string.you_don_t_have_any_treatment),
					fontSize = 24.sp
				)
			}
		}
	}

	@SuppressLint("UnusedTransitionTargetStateParameter")
	@Composable
	fun ExpandableCard(
		card: TreatmentDto,
		onCardArrowClick: () -> Unit,
		expanded: Boolean,
	) {
		val transitionState = remember {
			MutableTransitionState(expanded).apply {
				targetState = !expanded
			}
		}
		val transition = updateTransition(transitionState, label = "transition")
		val cardBgColor by transition.animateColor({
			tween(durationMillis = 450)
		}, label = "bgColorTransition") {
			if (expanded) cardExpandedBackgroundColor else cardCollapsedBackgroundColor
		}
		val cardPaddingHorizontal by transition.animateDp({
			tween(durationMillis = 450)
		}, label = "paddingTransition") {
			if (expanded) 24.dp else 24.dp
		}
		val cardElevation by transition.animateDp({
			tween(durationMillis = 450)
		}, label = "elevationTransition") {
			if (expanded) 24.dp else 4.dp
		}
		val cardRoundedCorners by transition.animateDp({
			tween(
				durationMillis = 450, easing = FastOutSlowInEasing
			)
		}, label = "cornersTransition") {
			if (expanded) 0.dp else 16.dp
		}
		val arrowRotationDegree by transition.animateFloat({
			tween(durationMillis = 450)
		}, label = "rotationDegreeTransition") {
			if (expanded) 0f else 180f
		}
		val context = LocalContext.current
		val contentColour = remember {
			Color(ContextCompat.getColor(context, R.color.black))
		}

		Card(
			colors = CardDefaults.cardColors(
				contentColor = contentColour,
				containerColor = cardBgColor
			),
			elevation = CardDefaults.cardElevation(
				defaultElevation = cardElevation
			),
			shape = RoundedCornerShape(cardRoundedCorners),
			modifier = Modifier.fillMaxWidth().padding(
				horizontal = cardPaddingHorizontal, vertical = 8.dp
			)
		) {
			Column {
				Box {
					CardTitle(title = "Обращение №${card.id.toString().split("-").last()}")
					CardArrow(
						degrees = arrowRotationDegree, onClick = onCardArrowClick
					)
					ChatButton(Modifier.align(Alignment.CenterEnd), card.id)
				}
				ExpandableContent(visible = expanded, card)
			}
		}
	}

	@Composable
	private fun ChatButton(modifier: Modifier, treatmentId: UUID) {
		IconButton(modifier = modifier, onClick = {
			ssm.save(CLICKED_TREATMENT_ID, treatmentId)
			this.startActivity(
				Intent(
					this, ChatActivity::class.java
				)
			)
			//activity?.finish()
		}, content = {
			Icon(Icons.Filled.Message, "message")
		})
	}

	@Composable
	fun CardArrow(
		degrees: Float, onClick: () -> Unit
	) {
		IconButton(onClick = onClick, content = {
			Icon(
				painter = painterResource(id = R.drawable.ic_expand_less_24),
				contentDescription = "Expandable Arrow",
				modifier = Modifier.rotate(degrees),
			)
		})
	}

	@Composable
	fun CardTitle(title: String) {
		Text(
			text = title,
			modifier = Modifier.fillMaxWidth().padding(16.dp),
			textAlign = TextAlign.Center,
		)
	}

	@Composable
	fun ExpandableContent(
		visible: Boolean = true, card: TreatmentDto
	) {
		val enterTransition = remember {
			expandVertically(
				expandFrom = Alignment.Top, animationSpec = tween(450)
			) + fadeIn(
				initialAlpha = 0.3f, animationSpec = tween(450)
			)
		}
		val exitTransition = remember {
			shrinkVertically(
				// Expand from the top.
				shrinkTowards = Alignment.Top, animationSpec = tween(450)
			) + fadeOut(
				// Fade in with the initial alpha of 0.3f.
				animationSpec = tween(450)
			)
		}

		AnimatedVisibility(
			visible = visible, enter = enterTransition, exit = exitTransition
		) {
			Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.Top) {
				Column(
					modifier = Modifier,
					horizontalAlignment = Alignment.Start,
					verticalArrangement = Arrangement.Top
				) {
					Text(
						modifier = Modifier.padding(start = 6.dp),
						text = stringResource(R.string.patient, card.patient.surename),
						fontSize = 24.sp
					)
					if (card.doctor != null) {
						Text(
							modifier = Modifier.padding(start = 6.dp),
							text = stringResource(R.string.doctor, card.doctor!!.surename),
							fontSize = 24.sp
						)
					}
				}
				Column(
					modifier = Modifier.fillMaxHeight(),
					horizontalAlignment = Alignment.End,
					verticalArrangement = Arrangement.Bottom
				) {
					Text(
						modifier = Modifier.padding(start = 6.dp),
						text = stringResource(R.string.created_date, card.createdDate),
						fontSize = 24.sp
					)
				}
			}
		}
	}

	@Composable
	fun Alert(): Boolean {
		val tmp = remember { mutableStateOf(true) }
		val symptoms by symptomViewModel.symptoms.collectAsStateWithLifecycle()
		var selectedIds: MutableSet<UUID> = mutableSetOf()
		AlertDialog(onDismissRequest = {
			tmp.value = false
		}, title = {
			Text(text = stringResource(R.string.add_new_treatment))
		}, text = {
			Column(
				modifier = Modifier
			) {
				Text(text = stringResource(R.string.choose_you_symptom))
				if (!symptoms?.isEmpty()!!) {
					MultiComboBox(
						stringResource(R.string.choose_symptoms),
						symptoms,
						{ symptomDtoList ->
							symptomDtoList?.forEach {
								selectedIds.add(it.id)
							}
						})
				}
			}
		}, confirmButton = {
			TextButton(onClick = {
				tmp.value = false
			}) {
				Text(stringResource(R.string.create))
			}
		}, dismissButton = {
			TextButton(onClick = {
				tmp.value = false
			}) {
				Text(stringResource(R.string.dismiss))
			}
		},
			modifier = Modifier
		)
		return tmp.value
	}

	@OptIn(ExperimentalMaterial3Api::class)
	@Composable
	fun MultiComboBox(
		labelText: String,
		options: List<SymptomDto>?,
		onOptionsChosen: (List<SymptomDto>?) -> Unit,
		selectedIds: List<UUID> = emptyList(),
	) {
		var expanded by remember { mutableStateOf(false) }
		// when no options available, I want ComboBox to be disabled
		val isEnabled by rememberUpdatedState { options?.isNotEmpty() }
		val selectedOptionsList = remember { mutableStateListOf<UUID>() }

		//Initial setup of selected ids
		selectedIds.forEach {
			selectedOptionsList.add(it)
		}

		ExposedDropdownMenuBox(
			expanded = expanded,
			onExpandedChange = {
				if (isEnabled()!!) {
					expanded = !expanded
					if (!expanded) {
						onOptionsChosen(options?.filter { it.id in selectedOptionsList }?.toList())
					}
				}
			},
			modifier = Modifier
				.requiredHeight(150.dp)
				.padding(top = 30.dp),
		) {
			val selectedSummary = when (selectedOptionsList.size) {
				0 -> ""
				1 -> options?.first { it.id == selectedOptionsList.first() }?.symptomsName
				else -> stringResource(R.string.choose, selectedOptionsList.size)
			}
			TextField(
				enabled = isEnabled()!!,
				modifier = Modifier.menuAnchor(),
				readOnly = true,
				value = selectedSummary!!,
				onValueChange = {},
				label = { Text(text = labelText) },
				trailingIcon = {
					ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
				},
				colors = ExposedDropdownMenuDefaults.textFieldColors(),
			)
			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = {
					expanded = false
					onOptionsChosen(options?.filter { it.id in selectedOptionsList }?.toList())
				},
			) {
				for (option in options!!) {

					//use derivedStateOf to evaluate if it is checked
					var checked = remember {
						derivedStateOf { option.id in selectedOptionsList }
					}.value

					DropdownMenuItem(
						text = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Checkbox(
									checked = checked,
									onCheckedChange = { newCheckedState ->
										if (newCheckedState) {
											selectedOptionsList.add(option.id)
										} else {
											selectedOptionsList.remove(option.id)
										}
									},
								)
								Text(text = option.symptomsName)
							}
						},
						onClick = {
							if (!checked) {
								selectedOptionsList.add(option.id)
							} else {
								selectedOptionsList.remove(option.id)
							}
						},
						contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
					)
				}
			}
		}
	}
}