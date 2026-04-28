package com.example.pianoapp

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.exp

object PianoSoundGenerator {

    private const val sampleRate = 44100

    private val noteFrequencies = mapOf(
        "C4" to 261.63f,
        "C#4" to 277.18f,
        "D4" to 293.66f,
        "D#4" to 311.13f,
        "E4" to 329.63f,
        "F4" to 349.23f,
        "F#4" to 369.99f,
        "G4" to 392.00f,
        "G#4" to 415.30f,
        "A4" to 440.00f,
        "A#4" to 466.16f,
        "B4" to 493.88f,
        "C5" to 523.25f
    )

    fun playNote(note: String) {
        val frequency = noteFrequencies[note] ?: return
        Thread {
            val duration = 1.0
            val numSamples = (duration * sampleRate).toInt()
            val samples = ShortArray(numSamples)

            for (i in 0 until numSamples) {
                val angle = 2.0 * PI * i * frequency / sampleRate
                val sample = (
                    sin(angle) * 0.6 +
                    sin(2 * angle) * 0.2 +
                    sin(3 * angle) * 0.1 +
                    sin(4 * angle) * 0.05
                )
                val envelope = exp(-3.0 * i / numSamples)
                samples[i] = (sample * envelope * Short.MAX_VALUE).toInt().toShort()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
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
