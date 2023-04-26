package com.poly.mobilenobles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private val chessMainLoop = ChessMainLoop()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)


        setContentView(R.layout.activity_main)

        // Buttons
        val button1 = findViewById<Button>(R.id.button)
        val button2 = findViewById<Button>(R.id.button2)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button9 = findViewById<Button>(R.id.button9)

        button6.visibility = View.INVISIBLE
        button7.visibility = View.INVISIBLE
        button9.visibility = View.INVISIBLE

        button2.setOnClickListener{
            button2.visibility = View.INVISIBLE
            button5.visibility = View.INVISIBLE
            button1.visibility = View.INVISIBLE
            button6.visibility = View.VISIBLE
            button7.visibility = View.VISIBLE

            button7.setOnClickListener{
                button9.visibility = View.VISIBLE
                button7.visibility = View.INVISIBLE
                button9.setOnClickListener{
                    button7.visibility = View.VISIBLE
                    button9.visibility = View.INVISIBLE
                }
            }

            button6.setOnClickListener{
                button2.visibility = View.VISIBLE
                button5.visibility = View.VISIBLE
                button1.visibility = View.VISIBLE
                button6.visibility = View.INVISIBLE
                button7.visibility = View.INVISIBLE
                button9.visibility = View.INVISIBLE
            }

        }
        // Close Game
        button1.setOnClickListener{
            exitProcess(0)
        }

        // Start Game
        button5.setOnClickListener{
        val chessboardView = ChessboardView(this, null, chessMainLoop)
        setContentView(chessboardView)

        }
    }
}

