package com.singh.mathgame

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.io.*

class ResultActivity : AppCompatActivity() {
    lateinit var result: TextView
    lateinit var playAgain : Button
    lateinit var exit: Button
    private val HIGH_SCORE_FILE = "high_scores.txt"  // Name of the external text file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        result = findViewById(R.id.textViewResult)
        playAgain = findViewById(R.id.buttonAgain)
        exit = findViewById(R.id.buttonExit)

        val score = intent.getIntExtra("score", 0)
        var currentHighScore = 0
        try {
            currentHighScore = readHighScore() // Read the current high score
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception (e.g., display a toast message)
        }
        result.text = "Your score: $score\n" +
                if (score > currentHighScore) "New High Score!" else ""  // Display new high score message

        // Save the new high score if applicable
        if (score > currentHighScore) {
            try {
                writeHighScore(score)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        playAgain.setOnClickListener {
            val intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        exit.setOnClickListener {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun readHighScore(): Int {
        try {
            val fileInputStream = openFileInput(HIGH_SCORE_FILE)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            val highScore = bufferedReader.readLine()?.toIntOrNull() ?: 0
            bufferedReader.close()
            return highScore
        } catch (e: FileNotFoundException) {
            return 0
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle other exceptions
            return 0
        } finally {
        }
    }
    private fun writeHighScore(score: Int) {
        try {
            val fileOutputStream = openFileOutput(HIGH_SCORE_FILE, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)

            bufferedWriter.write(score.toString())
            bufferedWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}