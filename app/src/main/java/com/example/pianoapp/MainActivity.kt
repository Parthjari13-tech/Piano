package com.example.pianoapp

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val whiteKeys = listOf("C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5")
    private val blackKeys = listOf("C#4", "D#4", "", "F#4", "G#4", "A#4", "")
    private var highlightedKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPianoKeys()
        setupButtons()
    }

    private fun setupPianoKeys() {
        val whiteKeyIds = listOf(
            R.id.key_c, R.id.key_d, R.id.key_e, R.id.key_f,
            R.id.key_g, R.id.key_a, R.id.key_b, R.id.key_c5
        )
        val blackKeyIds = listOf(
            R.id.key_cs, R.id.key_ds, View.NO_ID, R.id.key_fs,
            R.id.key_gs, R.id.key_as, View.NO_ID
        )

        whiteKeyIds.forEachIndexed { index, viewId ->
            val key = findViewById<Button>(viewId)
            val note = whiteKeys[index]
            key.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        PianoSoundGenerator.playNote(note)
                        v.setBackgroundColor(ContextCompat.getColor(this, R.color.key_white_pressed))
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.setBackgroundColor(
                            if (highlightedKey == note)
                                ContextCompat.getColor(this, R.color.key_highlight)
                            else
                                ContextCompat.getColor(this, R.color.key_white)
                        )
                        true
                    }
                    else -> false
                }
            }
        }

        blackKeyIds.forEachIndexed { index, viewId ->
            if (viewId == View.NO_ID) return@forEachIndexed
            val key = findViewById<Button>(viewId)
            val note = blackKeys[index]
            key.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        PianoSoundGenerator.playNote(note)
                        v.setBackgroundColor(ContextCompat.getColor(this, R.color.key_black_pressed))
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.setBackgroundColor(ContextCompat.getColor(this, R.color.key_black))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btn_lessons).setOnClickListener {
            startActivity(Intent(this, LessonsActivity::class.java))
        }
    }

    fun highlightKey(note: String?) {
        highlightedKey?.let { prev ->
            val viewId = getNoteViewId(prev)
            if (viewId != View.NO_ID) {
                val isBlack = prev.contains("#")
                findViewById<Button>(viewId).setBackgroundColor(
                    ContextCompat.getColor(this, if (isBlack) R.color.key_black else R.color.key_white)
                )
            }
        }
        highlightedKey = note
        note?.let {
            val viewId = getNoteViewId(it)
            if (viewId != View.NO_ID) {
                findViewById<Button>(viewId).setBackgroundColor(
                    ContextCompat.getColor(this, R.color.key_highlight)
                )
            }
        }
    }

    private fun getNoteViewId(note: String): Int = when (note) {
        "C4" -> R.id.key_c; "C#4" -> R.id.key_cs; "D4" -> R.id.key_d
        "D#4" -> R.id.key_ds; "E4" -> R.id.key_e; "F4" -> R.id.key_f
        "F#4" -> R.id.key_fs; "G4" -> R.id.key_g; "G#4" -> R.id.key_gs
        "A4" -> R.id.key_a; "A#4" -> R.id.key_as; "B4" -> R.id.key_b
        "C5" -> R.id.key_c5; else -> View.NO_ID
    }
}
