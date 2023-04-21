package com.poly.mobilenobles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ChessboardView(context: Context, attrs: AttributeSet?, private val chessMainLoop: ChessMainLoop) : View(context, attrs) {
    private val boardPaint = Paint().apply {
        style = Paint.Style.FILL

    }
    private val piecePaint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }


    //TODO Fix these guys here, dont want copies
    fun isWhitePiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('W')
    }

    fun isBlackPiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('B')
    }

    private fun getPieceImageResource(piece: ChessMainLoop.Piece): Int {
        return when (piece) {
            ChessMainLoop.Piece.BLACK_PAWN -> R.drawable.pdt
            ChessMainLoop.Piece.BLACK_ROOK -> R.drawable.rdt
            ChessMainLoop.Piece.BLACK_BISHOP -> R.drawable.bdt
            ChessMainLoop.Piece.BLACK_KNIGHT -> R.drawable.ndt
            ChessMainLoop.Piece.BLACK_KING -> R.drawable.kdt
            ChessMainLoop.Piece.BLACK_QUEEN -> R.drawable.qdt


            ChessMainLoop.Piece.WHITE_PAWN -> R.drawable.plt
            ChessMainLoop.Piece.WHITE_ROOK -> R.drawable.rlt
            ChessMainLoop.Piece.WHITE_BISHOP -> R.drawable.blt
            ChessMainLoop.Piece.WHITE_KNIGHT -> R.drawable.nlt
            ChessMainLoop.Piece.WHITE_KING -> R.drawable.klt
            ChessMainLoop.Piece.WHITE_QUEEN -> R.drawable.qlt
            else -> 0 // For EMPTY or any other invalid piece
        }
    }


    @SuppressLint("DrawAllocation")
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
                val imageResId = getPieceImageResource(piece)
                if (imageResId != 0) {
                    //If this method is too slow we can import a proper library for handling images
                    val bitmap = BitmapFactory.decodeResource(resources, imageResId)
                    val xOffset = (squareSize - bitmap.width) / 2
                    val yOffset = (squareSize - bitmap.height) / 2
                    canvas.drawBitmap(bitmap, left + xOffset, top + yOffset, null)
                }
            }
        }
    }
}

