package com.example.sistartertimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private var time = 0
    private var isRunning = false
    private var timerTask: Timer? = null
    private var index: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_start.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) start() else pause()
        }

        fab_reset.setOnClickListener {
            reset()
        }

        btn_lab.setOnClickListener {
            if (time!=0) lapTime()
        }
    }

    private fun start() {
        fab_start.setImageResource(R.drawable.ic_pause_black_24p)

        timerTask = timer(period = 1000) {
            time++

            runOnUiThread {
                timeText.text = formatTime(time)
            }
        }
    }

    private fun pause() {
        fab_start.setImageResource(R.drawable.ic_play)
        timerTask?.cancel()
    }

    private fun reset() {
        timerTask?.cancel()

        time = 0
        isRunning = false
        fab_start.setImageResource(R.drawable.ic_play)
        timeText.text = "00:00:00"

        lap_Layout.removeAllViews()
        index = 1
    }

    private fun lapTime() {
        val lapTime = time
        val textView = TextView(this).apply {
            textSize = 20f
        }
        textView.text = formatTime(time)

        lap_Layout.addView(textView, 0)
        index++
    }

    private fun formatTime(time: Int): String {
        val hour = String.format("%02d", time / 3600)
        val time: Int = time % 3600
        val min = String.format("%02d", time / 60)
        val sec = String.format("%02d", time % 60)
        return "$hour:$min:$sec"
    }

}