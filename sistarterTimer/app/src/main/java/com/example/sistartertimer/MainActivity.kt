package com.example.sistartertimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
//import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private var time = 0
    private var isRunning = false
    private var timer: Timer? = null
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

        var hour = strToInt(hourIn.text.toString())
        var min = strToInt(minIn.text.toString())
        var sec = strToInt(secIn.text.toString())

        hourOut.text = formatTime(hour)
        minOut.text = formatTime(min)
        secOut.text = formatTime(sec)

        hourIn.text = null
        minIn.text = null
        secIn.text = null


        val timer = Timer()
        timer.schedule(object : TimerTask(){

            override fun run() {
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
                }

                runOnUiThread {
                    timeText.text = formatWholeTime(time)
                    hourOut.text = formatTime(hour)
                    minOut.text = formatTime(min)
                    secOut.text = formatTime(sec)
                }

                if (sec == 0 && min == 0 && hour == 0) timer.cancel()
            }

        }, 1000, 1000)
    }

    private fun pause() {
        fab_start.setImageResource(R.drawable.ic_play)
        timer?.cancel()
    }

    private fun reset() {
        timer?.cancel()

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
        //val lapTime = time
        val textView = TextView(this).apply {
            textSize = 20f
        }
        textView.text = formatWholeTime(time)

        lap_Layout.addView(textView, 0)
        index++
    }

    private fun formatWholeTime(time: Int): String {
        val hour = String.format("%02d", time / 3600)
        val time: Int = time % 3600
        val min = String.format("%02d", time / 60)
        val sec = String.format("%02d", time % 60)
        return "$hour:$min:$sec"
    }

    private fun formatTime(time: Int): String {
        val time = String.format("%02d", time)
        return "$time"
    }

    private fun strToInt(timeIn: String): Int {
        return if (timeIn == "") 0
        else timeIn.toInt()
    }
}