package com.poly.mobilenobles

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.poly.mobilenobles.ChessMainLoop.Move
import android.util.Log
import android.media.MediaPlayer


@SuppressLint("ViewConstructor")
class ChessboardView(context: Context, attrs: AttributeSet?, private val chessMainLoop: ChessMainLoop) : View(context, attrs) {
    private val boardPaint = Paint().apply {
        style = Paint.Style.FILL
    }
    // Maps pieces to images

    /**
     * Returns the image resource ID for the given piece.
     * @param piece The piece to get the image resource for.
     * @return The image resource ID for the given piece.
     */
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
    /**
     * Plays a sound effect for a chess piece being moved.
     */
    private fun playMoveSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.moving)
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        mediaPlayer.start()
    }

    /**
     * Plays a sound effect for a checkmate event.
     */
    private fun playCheckmateSound() {
        val mediaPlayer = MediaPlayer.create(context, R.raw.vic)
        mediaPlayer.setOnCompletionListener { mp -> mp.release() }
        mediaPlayer.start()
    }


    /**
     * Converts the given screen coordinates to a pair of board coordinates (x, y).
     * @param x The x-coordinate of the screen position.
     * @param y The y-coordinate of the screen position.
     * @return A Pair object representing the board coordinates (x, y), or null if the screen position is not on the board.
     */
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

    /**
     * Converts the given board X-coordinate to a screen X-coordinate.
     * @param boardX The X-coordinate on the chessboard.
     * @return The corresponding X-coordinate on the screen.
     */
    private fun boardToScreenX(boardX: Int): Int {
        val squareSize = minOf(width, height) / 8
        val xOffset = (width - (squareSize * 8)) / 2
        return xOffset + boardX * squareSize
    }

    /**
     * Converts the given board Y-coordinate to a screen Y-coordinate.
     * @param boardY The Y-coordinate on the chessboard.
     * @return The corresponding Y-coordinate on the screen.
     */
    private fun boardToScreenY(boardY: Int): Int {
        val squareSize = minOf(width, height) / 8
        val yOffset = (height - (squareSize * 8)) / 2
        return yOffset + boardY * squareSize
    }


    private var draggingPiece: ChessMainLoop.Piece? = null
    private var draggingPieceStartX = 0
    private var draggingPieceStartY = 0
    private var draggingPieceX = 0
    private var draggingPieceY = 0

    /**
     * Handles touch events on the ChessBoardView.
     * @param event The MotionEvent object representing the touch event.
     * @return A boolean indicating whether the event was handled.
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Get X and Y of where touch happened
                val x = event.x.toInt()
                val y = event.y.toInt()

                // Convert to board coords
                val squareCoords = screenToBoard(x, y)

                // Make sure we're on the board
                if (squareCoords != null) {

                    // Update board X and Y
                    val (boardX, boardY) = squareCoords

                    // Get Piece
                    draggingPiece = chessMainLoop.chessboard[boardY][boardX]
                    if (draggingPiece != ChessMainLoop.Piece.EMPTY) {
                        draggingPieceStartX = boardX
                        draggingPieceStartY = boardY
                        draggingPieceX = x
                        draggingPieceY = y

                        Log.d("onTouchEvent", "Selected piece: $draggingPiece at ($draggingPieceStartX, $draggingPieceStartY)")

                        // Redraw
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
                    // Redraw
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                if (draggingPiece != null) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    val squareCoords = screenToBoard(x, y)
                    // Move piece if the coords are valid
                    if (squareCoords != null) {
                        val (boardX, boardY) = squareCoords
                        val move = Move(draggingPieceStartX, draggingPieceStartY, boardX, boardY)
                        val isWhiteToMove = chessMainLoop.isWhiteToMove

                        Log.d("onTouchEvent", "Attempting move: $move")

                        // Handles move outcomes
                        when (chessMainLoop.makeMoveOnCurrentBoard(move, isWhiteToMove)) {
                            ChessMainLoop.MoveResult.SUCCESS -> {
                                chessMainLoop.switchTurns()
                                playMoveSound()
                                // Redraw
                                invalidate()
                            }
                            ChessMainLoop.MoveResult.CHECKMATE -> {
                                // Checkmate view results here
                                chessMainLoop.switchTurns()
                                playCheckmateSound()
                                // Redraw
                                invalidate()
                            }
                            ChessMainLoop.MoveResult.INVALID -> {
                                Log.d("onTouchEvent", "Invalid move: $move")
                                // Return piece to original position
                                draggingPieceX = boardToScreenX(draggingPieceStartX)
                                draggingPieceY = boardToScreenY(draggingPieceStartY)
                            }
                        }
                    }
                    draggingPiece = null
                    // Redraw
                    invalidate()
                }
            }
        }
        return true
    }


    /**
     * Draws the chessboard and pieces on the screen.
     * @param canvas The canvas to draw on.
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val chessboard = chessMainLoop.chessboard
        val squareSize = minOf(width, height) / 8

        // Offsets to center board
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
        boardPaint.color = Color.BLACK

        val screenWidth = width.toFloat()
        val screenHeight = height.toFloat()

        // Top turn
        if (!chessMainLoop.isWhiteToMove) {
            val yourTurn = "Your Turn"
            val textPaint = Paint()
            textPaint.color = Color.BLACK
            textPaint.textSize = 150f
            textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

            //Center text
            val textWidth = textPaint.measureText(yourTurn)
            val textHeight = textPaint.descent() - textPaint.ascent()
            val x = (screenWidth - textWidth) / 2
            val y = textHeight

            // Rotate text at top
            canvas.save()
            canvas.rotate(180f, x + textWidth / 2, y - textHeight / 2)
            canvas.drawText(yourTurn, x, y-20, textPaint)
            canvas.restore()
        }
        // Bottom turn
        else
        {
            val yourTurn = "Your Turn"
            val textPaint = Paint()
            textPaint.color = Color.BLACK
            textPaint.textSize = 150f
            textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)

            // Center text
            val textWidth = textPaint.measureText(yourTurn)
            val x = (screenWidth - textWidth) / 2
            val y = screenHeight - textPaint.descent()

            canvas.drawText(yourTurn, x, y, textPaint)
        }


        val left = 0*squareSize.toFloat()+xOffset
        val bottomtop = 4*squareSize.toFloat()

        if (chessMainLoop.isCheckmate(chessboard, chessMainLoop.isWhiteToMove)) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gameoverwhite)
            canvas.drawBitmap(bitmap, left, bottomtop, null)
        } else if(chessMainLoop.isCheckmate(chessboard, !chessMainLoop.isWhiteToMove)) {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.gameoverblack)
            canvas.drawBitmap(bitmap, left, bottomtop, null)
        }



        // Dragging pieces over the board
        if (draggingPiece != null) {
            val imageResId = getPieceImageResource(draggingPiece!!)
            val bitmap = BitmapFactory.decodeResource(resources, imageResId)
            val drawX = (draggingPieceX - squareSize / 2).toFloat()
            val drawY = (draggingPieceY - squareSize / 2).toFloat()
            canvas.drawBitmap(bitmap, drawX, drawY, null)
        }
    }
}
