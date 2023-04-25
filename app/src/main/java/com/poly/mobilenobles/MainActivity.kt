package com.poly.mobilenobles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlin.system.exitProcess


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private val chessMainLoop = ChessMainLoop()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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

        button1.setOnClickListener{
            exitProcess(0)
        }

        button5.setOnClickListener{
        // Create the ChessboardView instance and pass the ChessMainLoop instance to it
        val chessboardView = ChessboardView(this, null, chessMainLoop)

        // Set the created ChessboardView as the content view
        setContentView(chessboardView)

        }
    }
}

