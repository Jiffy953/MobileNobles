package com.poly.mobilenobles

class ChessMainLoop {
    val chessboard: Array<Array<Piece>> = initializeChessboard()
    enum class Piece(val symbol: String) {
        EMPTY(" "),
        WHITE_PAWN("P"), WHITE_ROOK("R"), WHITE_KNIGHT("N"), WHITE_BISHOP("B"), WHITE_QUEEN("Q"), WHITE_KING("K"),
        BLACK_PAWN("p"), BLACK_ROOK("r"), BLACK_KNIGHT("n"), BLACK_BISHOP("b"), BLACK_QUEEN("q"), BLACK_KING("k")
    }


    fun initializeChessboard(): Array<Array<Piece>> {
        return arrayOf(
            arrayOf(Piece.BLACK_ROOK, Piece.BLACK_KNIGHT, Piece.BLACK_BISHOP, Piece.BLACK_QUEEN, Piece.BLACK_KING, Piece.BLACK_BISHOP, Piece.BLACK_KNIGHT, Piece.BLACK_ROOK),
            arrayOf(Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN, Piece.BLACK_PAWN),
            arrayOf(Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY),
            arrayOf(Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY),
            arrayOf(Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY),
            arrayOf(Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY, Piece.EMPTY),
            arrayOf(Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN, Piece.WHITE_PAWN),
            arrayOf(Piece.WHITE_ROOK, Piece.WHITE_KNIGHT, Piece.WHITE_BISHOP, Piece.WHITE_QUEEN, Piece.WHITE_KING, Piece.WHITE_BISHOP, Piece.WHITE_KNIGHT, Piece.WHITE_ROOK)
        )
    }


    fun chessboardToString(chessboard: Array<Array<Piece>>): String {
        val stringBuilder = StringBuilder()
        for (row in chessboard) {
            for (piece in row) {
                stringBuilder.append("$piece ")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    data class Move(val startX: Int, val startY: Int, val endX: Int, val endY: Int)

    fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0..7 && y in 0..7
    }

    fun getKingMoves(x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()
        val directions = listOf(-1, 0, 1)

        for (dx in directions) {
            for (dy in directions) {
                if (dx != 0 || dy != 0) {
                    val newX = x + dx
                    val newY = y + dy
                    if (isInBounds(newX, newY)) {
                        moves.add(Move(x, y, newX, newY))
                    }
                }
            }
        }

        return moves
    }

    fun getQueenMoves(x: Int, y: Int): List<Move> {
        return getRookMoves(x, y) + getBishopMoves(x, y)
    }

    fun getRookMoves(x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()
        val directions = listOf(-1, 1)

        for (dirX in directions) {
            for (newX in generateSequence(x + dirX, { it + dirX }).takeWhile { it in 0..7 }) {
                moves.add(Move(x, y, newX, y))
            }
        }

        for (dirY in directions) {
            for (newY in generateSequence(y + dirY, { it + dirY }).takeWhile { it in 0..7 }) {
                moves.add(Move(x, y, x, newY))
            }
        }

        return moves
    }

    fun getBishopMoves(x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()
        val directions = listOf(-1, 1)

        for (dirX in directions) {
            for (dirY in directions) {
                for (newPos in generateSequence(Pair(x + dirX, y + dirY), { Pair(it.first + dirX, it.second + dirY) })
                    .takeWhile { (newX, newY) -> isInBounds(newX, newY) }) {
                    moves.add(Move(x, y, newPos.first, newPos.second))
                }
            }
        }

        return moves
    }

    fun getKnightMoves(x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()
        val offsets = listOf(-2, -1, 1, 2)

        for (dx in offsets) {
            for (dy in offsets) {
                if (kotlin.math.abs(dx) != kotlin.math.abs(dy)) {
                    val newX = x + dx
                    val newY = y + dy
                    if (isInBounds(newX, newY)) {
                        moves.add(Move(x, y, newX, newY))
                    }
                }
            }
        }

        return moves
    }
    fun getPawnMoves(x: Int, y: Int, isWhite: Boolean): List<Move> {
        val moves = mutableListOf<Move>()
        val direction = if (isWhite) 1 else -1

        // Standard one square forward move
        val forwardY = y + direction
        if (isInBounds(x, forwardY)) {
            moves.add(Move(x, y, x, forwardY))
        }

        // Double square forward move from the initial position
        if ((isWhite && y == 1) || (!isWhite && y == 6)) {
            val doubleForwardY = y + 2 * direction
            if (isInBounds(x, doubleForwardY)) {
                moves.add(Move(x, y, x, doubleForwardY))
            }
        }

        // Capture moves
        val captureXPositions = listOf(x - 1, x + 1)
        for (captureX in captureXPositions) {
            val captureY = y + direction
            if (isInBounds(captureX, captureY)) {
                moves.add(Move(x, y, captureX, captureY))
            }
        }

        return moves
    }

    fun isWhitePiece(piece: Piece): Boolean {
        return piece.name.startsWith('W')
    }

    fun isBlackPiece(piece: Piece): Boolean {
        return piece.name.startsWith('B')
    }

    fun isSquareOccupiedByColor(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): Boolean {
        val piece = board[y][x]
        return if (isWhite) isWhitePiece(piece) else isBlackPiece(piece)
    }

    fun generateLegalMoves(board: Array<Array<Piece>>, isWhiteToMove: Boolean): List<Move> {
        val legalMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]
                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
                    val possibleMoves = when (piece) {
                        Piece.WHITE_PAWN, Piece.BLACK_PAWN -> getPawnMoves(x, y, isWhiteToMove)
                        Piece.WHITE_KNIGHT, Piece.BLACK_KNIGHT -> getKnightMoves(x, y)
                        Piece.WHITE_BISHOP, Piece.BLACK_BISHOP -> getBishopMoves(x, y)
                        Piece.WHITE_ROOK, Piece.BLACK_ROOK -> getRookMoves(x, y)
                        Piece.WHITE_QUEEN, Piece.BLACK_QUEEN -> getQueenMoves(x, y)
                        Piece.WHITE_KING, Piece.BLACK_KING -> getKingMoves(x, y)
                        else -> emptyList()
                    }

                    for (move in possibleMoves) {
                        val destX = move.endX
                        val destY = move.endY
                        val destinationPiece = board[destY][destX]

                        // Filter out moves that would capture a piece of the same color
                        if (!isSquareOccupiedByColor(board, destX, destY, isWhiteToMove)) {
                            // You can add additional rules here, like checking for checks, captures, etc.
                            legalMoves.add(move)
                        }
                    }
                }
            }
        }

        return legalMoves
    }

    fun isMoveLegal(move: Move, legalMoves: List<Move>): Boolean {
        return move in legalMoves
    }

    fun makeMove(board: Array<Array<Piece>>, move: Move, isWhiteToMove: Boolean): Boolean {
        val legalMoves = generateLegalMoves(board, isWhiteToMove)
        if (isMoveLegal(move, legalMoves)) {
            val startX = move.startX
            val startY = move.startY
            val endX = move.endX
            val endY = move.endY

            // Move the piece
            board[endY][endX] = board[startY][startX]
            board[startY][startX] = Piece.EMPTY

            // Add any additional rules here, like handling castling, en passant, or pawn promotion.

            return true // Move was successful
        }
        return false // Move was not legal
    }

    fun main() {
        val chessboard = initializeChessboard()
        var isWhiteToMove = true

        while (true) {
            println(chessboardToString(chessboard))
            print("Enter move (startX startY endX endY) for ${if (isWhiteToMove) "White" else "Black"}: ")
            val input = readLine()
            if (input != null) {
                val moveValues = input.split(" ").mapNotNull { it.toIntOrNull() }
                if (moveValues.size == 4) {
                    val move = Move(moveValues[0], moveValues[1], moveValues[2], moveValues[3])
                    val moveSuccessful = makeMove(chessboard, move, isWhiteToMove)
                    if (moveSuccessful) {
                        isWhiteToMove = !isWhiteToMove
                    } else {
                        println("Invalid move. Please try again.")
                    }
                } else {
                    println("Invalid input. Please enter four integers separated by spaces.")
                }
            } else {
                println("Invalid input. Please try again.")
            }
        }
    }
}

