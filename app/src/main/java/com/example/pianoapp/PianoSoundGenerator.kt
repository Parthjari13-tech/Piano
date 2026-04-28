package com.example.pianoapp

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.exp
import kotlin.math.pow

object PianoSoundGenerator {

    private const val sampleRate = 44100

    private val noteFrequencies = mapOf(
        "C4" to 261.63,
        "C#4" to 277.18,
        "D4" to 293.66,
        "D#4" to 311.13,
        "E4" to 329.63,
        "F4" to 349.23,
        "F#4" to 369.99,
        "G4" to 392.00,
        "G#4" to 415.30,
        "A4" to 440.00,
        "A#4" to 466.16,
        "B4" to 493.88,
        "C5" to 523.25
    )

    fun playNote(note: String) {
        val frequency = noteFrequencies[note] ?: return
        Thread {
            val duration = 2.0 // longer sustain
            val numSamples = (duration * sampleRate).toInt()
            val samples = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val t = i.toDouble() / sampleRate

                // Multiple harmonics for richer piano-like tone
                val fundamental  = sin(2.0 * PI * frequency * t)
                val harmonic2    = sin(2.0 * PI * frequency * 2 * t) * 0.5
                val harmonic3    = sin(2.0 * PI * frequency * 3 * t) * 0.25
                val harmonic4    = sin(2.0 * PI * frequency * 4 * t) * 0.15
                val harmonic5    = sin(2.0 * PI * frequency * 5 * t) * 0.08
                val harmonic6    = sin(2.0 * PI * frequency * 6 * t) * 0.05

                // Mix harmonics
                val mix = (fundamental + harmonic2 + harmonic3 + harmonic4 + harmonic5 + harmonic6) / 2.03

                // Piano-like envelope: fast attack, slow decay, long release
                val attack = if (t < 0.01) t / 0.01 else 1.0  // 10ms attack
                val decay = exp(-2.5 * t)                       // slow exponential decay
                val envelope = attack * decay

                samples[i] = (mix * envelope * Short.MAX_VALUE * 0.85).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(sampleRate)
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(samples.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(samples, 0, samples.size)
            audioTrack.play()
            Thread.sleep((duration * 1000).toLong())
            audioTrack.stop()
            audioTrack.release()
        }.start()
    }
}
