package com.poly.mobilenobles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private val chessMainLoop = ChessMainLoop()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        // Create the ChessboardView instance and pass the ChessMainLoop instance to it
        val chessboardView = ChessboardView(this, null, chessMainLoop)

        // Set the created ChessboardView as the content view
        setContentView(chessboardView)
    }
}

