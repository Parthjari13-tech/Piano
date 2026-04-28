# 🎹 PianoLearn Android App

A beginner-friendly piano learning app with 1 octave keyboard and interactive lessons.

## Features
- 1-octave piano keyboard (C4 to C5) with black keys
- 6 built-in lessons (C major scale, Mary Had a Little Lamb, Twinkle Twinkle, etc.)
- Interactive lesson mode — highlighted keys guide you
- Progress saved automatically
- Dark theme UI

---

## 🚀 Setup Instructions

### Step 1 — Open in Android Studio
1. Open Android Studio
2. Click **File → Open**
3. Select the `PianoApp` folder
4. Let Gradle sync finish

### Step 2 — Add Piano Sound Files ⚠️ REQUIRED
The app needs `.ogg` or `.wav` audio files for each note.

1. Create folder: `app/src/main/res/raw/`
2. Download free piano samples from: https://freesound.org
   - Search "piano C4", "piano D4", etc.
3. Rename and place files exactly as:
   - `note_c4.ogg`
   - `note_cs4.ogg`  (C sharp)
   - `note_d4.ogg`
   - `note_ds4.ogg`  (D sharp)
   - `note_e4.ogg`
   - `note_f4.ogg`
   - `note_fs4.ogg`  (F sharp)
   - `note_g4.ogg`
   - `note_gs4.ogg`  (G sharp)
   - `note_a4.ogg`
   - `note_as4.ogg`  (A sharp)
   - `note_b4.ogg`
   - `note_c5.ogg`

> 💡 Tip: You can also use the **Freepats** or **Piano from Above** free sample packs.

### Step 3 — Run the App
1. Connect your Android phone (USB debugging ON) or use the emulator
2. Click the ▶ Run button in Android Studio
3. Enjoy playing! 🎹

---

## 📁 Project Structure
```
PianoApp/
├── app/src/main/
│   ├── java/com/example/pianoapp/
│   │   ├── MainActivity.kt          # Piano keyboard screen
│   │   ├── LessonsActivity.kt       # Lessons list screen
│   │   └── LessonDetailActivity.kt  # Interactive lesson screen
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml
│   │   │   ├── activity_lessons.xml
│   │   │   ├── activity_lesson_detail.xml
│   │   │   └── item_lesson.xml
│   │   ├── values/
│   │   │   ├── colors.xml
│   │   │   └── styles.xml
│   │   └── raw/                     # ← Add your .ogg sound files here
│   └── AndroidManifest.xml
└── build.gradle
```

---

## 🎓 Lessons Included
1. Play Middle C
2. C Major Scale
3. Mary Had a Little Lamb
4. Twinkle Twinkle Little Star
5. C & G Chords
6. Ode to Joy

## 🛠️ Requirements
- Android Studio Hedgehog or newer
- Android SDK 24+ (Android 7.0)
- Kotlin 1.9+
