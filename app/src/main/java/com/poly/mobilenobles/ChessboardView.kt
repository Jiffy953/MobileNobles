package com.poly.mobilenobles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.poly.mobilenobles.ChessMainLoop.Move
import android.util.Log


class ChessboardView(context: Context, attrs: AttributeSet?, private val chessMainLoop: ChessMainLoop) : View(context, attrs) {
    private val boardPaint = Paint().apply {
        style = Paint.Style.FILL

    }
    private val piecePaint = Paint().apply {
        textSize = 60f
        textAlign = Paint.Align.CENTER
    }


    //TODO Fix these guys here, don't want copies
    fun isWhitePiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('W')
    }

    fun isBlackPiece(piece: ChessMainLoop.Piece): Boolean {
        return piece.name.startsWith('B')
    }

    fun whichTurnGraphic(){


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

    private var draggingPiece: ChessMainLoop.Piece? = null
    private var draggingPieceStartX = 0
    private var draggingPieceStartY = 0
    private var draggingPieceX = 0
    private var draggingPieceY = 0


    private fun screenToBoard(x: Int, y: Int): Pair<Int, Int>? {
        val squareSize = minOf(width, height) / 8
        val xOffset = (width - (squareSize * 8)) / 2
        val yOffset = (height - (squareSize * 8)) / 2

        val boardX = (x - xOffset) / squareSize
        val boardY = (y - yOffset) / squareSize

        return if (boardX in 0..7 && boardY in 0..7) {
            Pair(boardX, boardY)
        } else {
            null
        }
    }

    private fun boardToScreenX(boardX: Int): Int {
        val squareSize = minOf(width, height) / 8
        val xOffset = (width - (squareSize * 8)) / 2
        return xOffset + boardX * squareSize
    }

    private fun boardToScreenY(boardY: Int): Int {
        val squareSize = minOf(width, height) / 8
        val yOffset = (height - (squareSize * 8)) / 2
        return yOffset + boardY * squareSize
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x.toInt()
                val y = event.y.toInt()
                val squareCoords = screenToBoard(x, y)
                if (squareCoords != null) {
                    val (boardX, boardY) = squareCoords
                    draggingPiece = chessMainLoop.chessboard[boardY][boardX]
                    if (draggingPiece != ChessMainLoop.Piece.EMPTY) {
                        draggingPieceStartX = boardX
                        draggingPieceStartY = boardY
                        draggingPieceX = x
                        draggingPieceY = y
                        invalidate()
                    } else {
                        draggingPiece = null
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (draggingPiece != null) {
                    draggingPieceX = event.x.toInt()
                    draggingPieceY = event.y.toInt()
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (draggingPiece != null) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    val squareCoords = screenToBoard(x, y)
                    if (squareCoords != null) {
                        val (boardX, boardY) = squareCoords
                        val move = Move(draggingPieceStartX, draggingPieceStartY, boardX, boardY)
                        val isWhiteToMove = chessMainLoop.isWhiteToMove

                        Log.d("onTouchEvent", "Trying to make a move: $move")

                        // Replace the call to makeMove() with a call to chessMainLoop.makeMoveOnCurrentBoard()
                        val moveMade = chessMainLoop.makeMoveOnCurrentBoard(move, isWhiteToMove)

                        if (moveMade) {
                            // Update the game state
                            chessMainLoop.switchTurns()
                        } else {

                            Log.d("onTouchEvent", "Invalid move: $move")
                            // Invalid move, return the piece to its starting position
                            draggingPieceX = boardToScreenX(draggingPieceStartX)
                            draggingPieceY = boardToScreenY(draggingPieceStartY)
                        }
                    }
                    draggingPiece = null
                    invalidate()
                }
            }
        }
        return true
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val chessboard = chessMainLoop.chessboard
        val squareSize = minOf(width, height) / 8

        // Calculate the horizontal and vertical offsets to center the chessboard
        val xOffset = (width - (squareSize * 8)) / 2
        val yOffset = (height - (squareSize * 8)) / 2

        // Draw the chessboard
        for (y in 0..7) {
            for (x in 0..7) {
                boardPaint.color = if ((x + y) % 2 == 0) Color.WHITE else Color.LTGRAY
                val left = (x * squareSize).toFloat() + xOffset
                val top = (y * squareSize).toFloat() + yOffset
                val right = left + squareSize
                val bottom = top + squareSize
                canvas.drawRect(RectF(left, top, right, bottom), boardPaint)

                // Draw the pieces
                val piece = chessboard[y][x]
                val imageResId = getPieceImageResource(piece)
                if (imageResId != 0) {
                    val bitmap = BitmapFactory.decodeResource(resources, imageResId)
                    val pieceXOffset = (squareSize - bitmap.width) / 2
                    val pieceYOffset = (squareSize - bitmap.height) / 2
                    canvas.drawBitmap(bitmap, left + pieceXOffset, top + pieceYOffset, null)
                }
            }
        }

        // Draw the dragging piece above the board
        if (draggingPiece != null) {
            val imageResId = getPieceImageResource(draggingPiece!!)
            val bitmap = BitmapFactory.decodeResource(resources, imageResId)
            val drawX = (draggingPieceX - squareSize / 2).toFloat()
            val drawY = (draggingPieceY - squareSize / 2).toFloat()
            canvas.drawBitmap(bitmap, drawX, drawY, null)
        }
    }
}
