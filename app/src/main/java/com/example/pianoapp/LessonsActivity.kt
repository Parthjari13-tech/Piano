package com.example.pianoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Lesson(
    val id: Int,
    val title: String,
    val description: String,
    val difficulty: String,
    val notes: List<String>
)

class LessonsActivity : AppCompatActivity() {

    private val lessons = listOf(
        Lesson(1, "Play Middle C", "Learn to find and play the C note.", "Beginner", listOf("C4")),
        Lesson(2, "C Major Scale", "Play all 8 notes of the C major scale.", "Beginner",
            listOf("C4", "D4", "E4", "F4", "G4", "A4", "B4", "C5")),

        // 🎬 Bollywood Songs
        Lesson(3, "Tum Hi Ho ❤️", "Aashiqui 2 — one of the most loved Bollywood melodies.", "Beginner",
            listOf(
                "B4", "A4", "G4", "E4",
                "G4", "A4", "B4", "A4", "G4",
                "B4", "A4", "G4", "E4",
                "G4", "E4", "D4", "C4",
                "D4", "E4", "G4", "A4", "G4", "E4",
                "G4", "A4", "B4", "A4", "G4"
            )),

        Lesson(4, "Kal Ho Naa Ho 🌟", "A timeless classic from the 2003 film.", "Beginner",
            listOf(
                "E4", "G4", "A4", "G4", "E4",
                "D4", "E4", "G4", "A4",
                "C5", "B4", "A4", "G4",
                "E4", "G4", "A4", "G4", "E4",
                "D4", "E4", "G4", "E4", "D4", "C4"
            )),

        Lesson(5, "Lag Ja Gale 🎶", "Beautiful classic by Lata Mangeshkar.", "Beginner",
            listOf(
                "C4", "D4", "E4", "G4",
                "A4", "G4", "E4", "D4",
                "E4", "G4", "A4", "B4",
                "A4", "G4", "E4",
                "C4", "D4", "E4", "G4",
                "A4", "G4", "E4", "D4", "C4"
            )),

        Lesson(6, "Jeena Jeena 🎵", "The hit song from Badlapur (2015).", "Beginner",
            listOf(
                "E4", "E4", "G4", "A4",
                "G4", "E4", "D4", "E4",
                "G4", "G4", "A4", "B4",
                "A4", "G4", "E4",
                "E4", "G4", "A4", "G4",
                "E4", "D4", "C4", "D4", "E4"
            )),

        Lesson(7, "Mary Had a Little Lamb", "A classic beginner melody.", "Beginner",
            listOf("E4", "D4", "C4", "D4", "E4", "E4", "E4",
                   "D4", "D4", "D4", "E4", "G4", "G4")),

        Lesson(8, "Twinkle Twinkle", "Another great beginner song.", "Beginner",
            listOf("C4", "C4", "G4", "G4", "A4", "A4", "G4",
                   "F4", "F4", "E4", "E4", "D4", "D4", "C4"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)

        val prefs = getSharedPreferences("piano_progress", Context.MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_lessons)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = LessonAdapter(lessons, prefs) { lesson ->
            val intent = Intent(this, LessonDetailActivity::class.java)
            intent.putExtra("lesson_id", lesson.id)
            intent.putExtra("lesson_title", lesson.title)
            intent.putExtra("lesson_description", lesson.description)
            intent.putExtra("lesson_difficulty", lesson.difficulty)
            intent.putStringArrayListExtra("lesson_notes", ArrayList(lesson.notes))
            startActivity(intent)
        }

        findViewById<Button>(R.id.btn_back).setOnClickListener { finish() }
    }
}

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val prefs: android.content.SharedPreferences,
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.lesson_title)
        val description: TextView = view.findViewById(R.id.lesson_description)
        val difficulty: TextView = view.findViewById(R.id.lesson_difficulty)
        val checkmark: ImageView = view.findViewById(R.id.lesson_checkmark)
        val card: View = view.findViewById(R.id.lesson_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        val isCompleted = prefs.getBoolean("lesson_${lesson.id}_completed", false)
        holder.title.text = lesson.title
        holder.description.text = lesson.description
        holder.difficulty.text = lesson.difficulty
        holder.checkmark.visibility = if (isCompleted) View.VISIBLE else View.GONE
        holder.card.setOnClickListener { onClick(lesson) }
    }

    override fun getItemCount() = lessons.size
}
