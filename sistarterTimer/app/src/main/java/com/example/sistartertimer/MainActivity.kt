package com.example.sistartertimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        timeIn.visibility = View.GONE
        timeOut.visibility = View.VISIBLE

        hourOut.text = hourIn.text.toString()
        minOut.text = minIn.text.toString()
        secOut.text = secIn.text.toString()

        var hour: Int = if (hourIn.text.toString() == "") {
            0
        } else {
            hourIn.text.toString().toInt()
        }
        var min: Int = if (minIn.text.toString() == "") {
            0
        } else {
            minIn.text.toString().toInt()
        }
        var sec: Int = if (secIn.text.toString() == "") {
            0
        } else {
            secIn.text.toString().toInt()
        }


        timerTask = timer(period = 1000) {

            //if (sec == 0 && min == 0 && hour == 0) timerTask?.cancel()

            time++

            when {
                sec != 0 -> sec--
                min != 0 -> {
                    sec = 60
                    sec--
                    min--
                }
                hour != 0 -> {
                    min = 60
                    hour--
                    min--
                    sec = 60
                    sec--
                }
                //sec == 1 -> timerTask?.cancel()
                //else -> timerTask?.cancel()
            }

            runOnUiThread {
                timeText.text = formatTime(time)
                hourOut.text = String.format("%02d", hour)
                minOut.text = String.format("%02d", min)
                secOut.text = String.format("%02d", sec)
            }
            if (sec == 0 && min == 0 && hour == 0) timerTask?.cancel()
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

        timeIn.visibility = View.VISIBLE
        timeOut.visibility = View.GONE

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