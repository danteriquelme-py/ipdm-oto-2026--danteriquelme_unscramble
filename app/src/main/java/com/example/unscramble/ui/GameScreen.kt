package com.example.unscramble.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unscramble.R

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .safeDrawingPadding()
            .padding(mediumPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.app_name),
            style = typography.titleLarge,
        )
        
        // Layout principal
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(mediumPadding),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(mediumPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(mediumPadding)
            ) {
                Text(
                    modifier = Modifier
                        .clip(shapes.medium)
                        .background(colorScheme.surfaceTint)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(alignment = Alignment.End),
                    text = stringResource(R.string.word_count, gameViewModel.currentWordCount),
                    style = typography.titleMedium,
                    color = colorScheme.onPrimary
                )
                Text(
                    text = gameViewModel.currentScrambledWord,
                    style = typography.displayMedium
                )
                Text(
                    text = stringResource(R.string.instructions),
                    textAlign = TextAlign.Center,
                    style = typography.titleMedium
                )
                OutlinedTextField(
                    value = gameViewModel.userGuess,
                    singleLine = true,
                    shape = shapes.large,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface,
                        disabledContainerColor = colorScheme.surface,
                    ),
                    onValueChange = { gameViewModel.updateUserGuess(it) },
                    label = {
                        if (gameViewModel.isGuessedWordWrong) {
                            Text(stringResource(R.string.wrong_guess))
                        } else {
                            Text(stringResource(R.string.enter_your_word))
                        }
                    },
                    isError = gameViewModel.isGuessedWordWrong,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { gameViewModel.checkUserGuess() }
                    )
                )
            }
        }

        // Botones
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(mediumPadding),
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { gameViewModel.checkUserGuess() }
            ) {
                Text(text = stringResource(R.string.submit), fontSize = 16.sp)
            }

            OutlinedButton(
                onClick = { gameViewModel.skipWord() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.skip), fontSize = 16.sp)
            }
        }

        // Puntaje
        Card(modifier = Modifier.padding(20.dp)) {
            Text(
                text = stringResource(R.string.score, gameViewModel.score),
                style = typography.headlineMedium,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Dialogo final
        if (gameViewModel.isGameOver) {
            val activity = (LocalContext.current as Activity)
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = stringResource(R.string.congratulations)) },
                text = { Text(text = stringResource(R.string.you_scored, gameViewModel.score)) },
                dismissButton = {
                    TextButton(onClick = { activity.finish() }) {
                        Text(text = stringResource(R.string.exit))
                    }
                },
                confirmButton = {
                    TextButton(onClick = { gameViewModel.resetGame() }) {
                        Text(text = stringResource(R.string.play_again))
                    }
                }
            )
        }
    }
}
