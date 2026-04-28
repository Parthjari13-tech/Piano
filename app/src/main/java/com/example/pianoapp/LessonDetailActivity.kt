package com.example.pianoapp

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LessonDetailActivity : AppCompatActivity() {

    private var lessonNotes = listOf<String>()
    private var currentNoteIndex = 0
    private var isCompleted = false

    private lateinit var tvInstruction: TextView
    private lateinit var tvProgress: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnRestart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)

        val lessonId = intent.getIntExtra("lesson_id", 0)
        val title = intent.getStringExtra("lesson_title") ?: ""
        val description = intent.getStringExtra("lesson_description") ?: ""
        lessonNotes = intent.getStringArrayListExtra("lesson_notes") ?: arrayListOf()

        tvInstruction = findViewById(R.id.tv_instruction)
        tvProgress = findViewById(R.id.tv_progress)
        tvStatus = findViewById(R.id.tv_status)
        btnRestart = findViewById(R.id.btn_restart)

        findViewById<TextView>(R.id.tv_lesson_title).text = title
        findViewById<TextView>(R.id.tv_lesson_desc).text = description

        setupPianoKeys()
        updateUI()
        highlightCurrentKey()

        btnRestart.setOnClickListener { restartLesson() }
        findViewById<Button>(R.id.btn_back_lesson).setOnClickListener { finish() }
    }

    private fun setupPianoKeys() {
        val keyNoteMap = mapOf(
            R.id.key_c to "C4", R.id.key_d to "D4", R.id.key_e to "E4",
            R.id.key_f to "F4", R.id.key_g to "G4", R.id.key_a to "A4",
            R.id.key_b to "B4", R.id.key_c5 to "C5",
            R.id.key_cs to "C#4", R.id.key_ds to "D#4", R.id.key_fs to "F#4",
            R.id.key_gs to "G#4", R.id.key_as to "A#4"
        )

        keyNoteMap.forEach { (viewId, note) ->
            val key = findViewById<Button>(viewId)
            val isBlack = note.contains("#")
            key.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        PianoSoundGenerator.playNote(note)
                        checkNote(note)
                        v.setBackgroundColor(ContextCompat.getColor(this,
                            if (isBlack) R.color.key_black_pressed else R.color.key_white_pressed))
                        true
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val isHighlighted = lessonNotes.getOrNull(currentNoteIndex) == note
                        v.setBackgroundColor(ContextCompat.getColor(this, when {
                            isHighlighted -> R.color.key_highlight
                            isBlack -> R.color.key_black
                            else -> R.color.key_white
                        }))
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun checkNote(playedNote: String) {
        if (isCompleted) return
        val expectedNote = lessonNotes.getOrNull(currentNoteIndex) ?: return

        if (playedNote == expectedNote) {
            tvStatus.text = "✅ Correct!"
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.correct_green))
            currentNoteIndex++

            if (currentNoteIndex >= lessonNotes.size) {
                isCompleted = true
                tvStatus.text = "🎉 Lesson Complete!"
                tvInstruction.text = "Amazing! You finished the lesson!"
                tvProgress.text = "${lessonNotes.size}/${lessonNotes.size}"
                btnRestart.text = "Play Again"
                saveProgress()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    highlightCurrentKey()
                    updateUI()
                }, 300)
            }
        } else {
            tvStatus.text = "❌ Try again! Play the highlighted key."
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.wrong_red))
        }
    }

    private fun highlightCurrentKey() {
        clearAllHighlights()
        val currentNote = lessonNotes.getOrNull(currentNoteIndex) ?: return
        val viewId = getNoteViewId(currentNote)
        if (viewId != View.NO_ID) {
            findViewById<Button>(viewId).setBackgroundColor(
                ContextCompat.getColor(this, R.color.key_highlight)
            )
        }
    }

    private fun clearAllHighlights() {
        listOf(R.id.key_c, R.id.key_d, R.id.key_e, R.id.key_f,
            R.id.key_g, R.id.key_a, R.id.key_b, R.id.key_c5).forEach {
            findViewById<Button>(it).setBackgroundColor(ContextCompat.getColor(this, R.color.key_white))
        }
        listOf(R.id.key_cs, R.id.key_ds, R.id.key_fs, R.id.key_gs, R.id.key_as).forEach {
            findViewById<Button>(it).setBackgroundColor(ContextCompat.getColor(this, R.color.key_black))
        }
    }

    private fun updateUI() {
        tvProgress.text = "$currentNoteIndex/${lessonNotes.size}"
        val currentNote = lessonNotes.getOrNull(currentNoteIndex)
        if (currentNote != null) {
            tvInstruction.text = "Play: $currentNote"
            tvStatus.text = "👆 Tap the highlighted key"
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.primary_dark))
        }
    }

    private fun restartLesson() {
        currentNoteIndex = 0
        isCompleted = false
        btnRestart.text = "Restart"
        updateUI()
        highlightCurrentKey()
    }

    private fun saveProgress() {
        val lessonId = intent.getIntExtra("lesson_id", 0)
        getSharedPreferences("piano_progress", Context.MODE_PRIVATE)
            .edit().putBoolean("lesson_${lessonId}_completed", true).apply()
    }

    private fun getNoteViewId(note: String): Int = when (note) {
        "C4" -> R.id.key_c; "C#4" -> R.id.key_cs; "D4" -> R.id.key_d
        "D#4" -> R.id.key_ds; "E4" -> R.id.key_e; "F4" -> R.id.key_f
        "F#4" -> R.id.key_fs; "G4" -> R.id.key_g; "G#4" -> R.id.key_gs
        "A4" -> R.id.key_a; "A#4" -> R.id.key_as; "B4" -> R.id.key_b
        "C5" -> R.id.key_c5; else -> View.NO_ID
    }
}
