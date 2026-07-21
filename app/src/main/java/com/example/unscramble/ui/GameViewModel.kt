package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords

class GameViewModel : ViewModel() {

    // Estado simplificado (estilo principiante "lazy")
    var currentScrambledWord by mutableStateOf("")
    var currentWordCount by mutableStateOf(1)
    var score by mutableStateOf(0)
    var isGuessedWordWrong by mutableStateOf(false)
    var isGameOver by mutableStateOf(false)
    var userGuess by mutableStateOf("")

    private var usedWords: MutableSet<String> = mutableSetOf()
    private var currentWord: String = ""

    init {
        resetGame()
    }

    fun resetGame() {
        usedWords.clear()
        score = 0
        currentWordCount = 1
        isGameOver = false
        isGuessedWordWrong = false
        userGuess = ""
        proximoTurno()
    }

    fun updateUserGuess(word: String) {
        userGuess = word
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            score += SCORE_INCREASE
            if (usedWords.size >= MAX_NO_OF_WORDS) {
                isGameOver = true
            } else {
                currentWordCount++
                proximoTurno()
            }
            isGuessedWordWrong = false
        } else {
            isGuessedWordWrong = true
        }
        userGuess = ""
    }

    fun skipWord() {
        if (usedWords.size >= MAX_NO_OF_WORDS) {
            isGameOver = true
        } else {
            currentWordCount++
            proximoTurno()
        }
        userGuess = ""
        isGuessedWordWrong = false
    }

    private fun proximoTurno() {
        // Elegir palabra random
        var word = allWords.random()
        while (usedWords.contains(word)) {
            word = allWords.random()
        }
        currentWord = word
        usedWords.add(word)

        // Mezclarla (shuffle)
        val temp = word.toCharArray()
        temp.shuffle()
        while (String(temp) == word) {
            temp.shuffle()
        }
        currentScrambledWord = String(temp)
    }
}
