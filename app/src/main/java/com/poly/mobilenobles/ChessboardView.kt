package com.poly.mobilenobles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class ChessboardView(context: Context, attrs: AttributeSet?, private val chessMainLoop: ChessMainLoop) : View(context, attrs) {    private val boardPaint = Paint().apply {
        style = Paint.Style.FILL

    }
    private val piecePaint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }

    fun isWhitePiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('W')
    }

    fun isBlackPiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('B')
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val chessboard = chessMainLoop.chessboard
        val squareSize = minOf(width, height) / 8

        // Draw the chessboard
        for (y in 0..7) {
            for (x in 0..7) {
                boardPaint.color = if ((x + y) % 2 == 0) Color.WHITE else Color.LTGRAY
                val left = (x * squareSize).toFloat()
                val top = (y * squareSize).toFloat()
                val right = left + squareSize
                val bottom = top + squareSize
                canvas.drawRect(RectF(left, top, right, bottom), boardPaint)

                // Draw the pieces
                val piece = chessboard[y][x]
                if (piece != ChessMainLoop.Piece.EMPTY) {
                    piecePaint.color = if (isWhitePiece(piece)) Color.BLUE else Color.RED
                    val cx = left + squareSize / 2
                    val cy = top + squareSize / 2 - (piecePaint.ascent() + piecePaint.descent()) / 2
                    canvas.drawText(piece.symbol, cx, cy, piecePaint)

                }
            }
        }
    }
}
