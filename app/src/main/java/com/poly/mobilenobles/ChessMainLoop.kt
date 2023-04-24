package com.poly.mobilenobles

import android.util.Log


class ChessMainLoop {
    val chessboard: Array<Array<Piece>> = initializeChessboard()
    var isWhiteToMove = true

    enum class Piece(val symbol: String) {
        EMPTY(" "),
        WHITE_PAWN("P"), WHITE_ROOK("R"), WHITE_KNIGHT("N"), WHITE_BISHOP("B"), WHITE_QUEEN("Q"), WHITE_KING(
            "K"
        ),
        BLACK_PAWN("p"), BLACK_ROOK("r"), BLACK_KNIGHT("n"), BLACK_BISHOP("b"), BLACK_QUEEN("q"), BLACK_KING(
            "k"
        )
    }


    fun initializeChessboard(): Array<Array<Piece>> {
        return arrayOf(
            arrayOf(
                Piece.BLACK_ROOK,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_BISHOP,
                Piece.BLACK_QUEEN,
                Piece.BLACK_KING,
                Piece.BLACK_BISHOP,
                Piece.BLACK_KNIGHT,
                Piece.BLACK_ROOK
            ),
            arrayOf(
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN,
                Piece.BLACK_PAWN
            ),
            arrayOf(
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY
            ),
            arrayOf(
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY
            ),
            arrayOf(
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY
            ),
            arrayOf(
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY,
                Piece.EMPTY
            ),
            arrayOf(
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN,
                Piece.WHITE_PAWN
            ),
            arrayOf(
                Piece.WHITE_ROOK,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_BISHOP,
                Piece.WHITE_QUEEN,
                Piece.WHITE_KING,
                Piece.WHITE_BISHOP,
                Piece.WHITE_KNIGHT,
                Piece.WHITE_ROOK
            )
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

    fun getQueenMoves(board: Array<Array<Piece>>, x: Int, y: Int): List<Move> {
        return getRookMoves(board, x, y) + getBishopMoves(board, x, y)
    }


    fun getRookMoves(board: Array<Array<Piece>>, x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()
        val directions = listOf(-1, 1)

        for (dirX in directions) {
            for (newX in generateSequence(x + dirX, { it + dirX }).takeWhile { it in 0..7 }) {
                if (board[y][newX] != Piece.EMPTY) {
                    if (isSquareOccupiedByColor(board, newX, y, !isWhitePiece(board[y][x]))) {
                        moves.add(Move(x, y, newX, y))
                    }
                    break
                }
                moves.add(Move(x, y, newX, y))
            }
        }

        for (dirY in directions) {
            for (newY in generateSequence(y + dirY, { it + dirY }).takeWhile { it in 0..7 }) {
                if (board[newY][x] != Piece.EMPTY) {
                    if (isSquareOccupiedByColor(board, x, newY, !isWhitePiece(board[y][x]))) {
                        moves.add(Move(x, y, x, newY))
                    }
                    break
                }
                moves.add(Move(x, y, x, newY))
            }
        }

        return moves
    }


    fun getBishopMoves(board: Array<Array<Piece>>, x: Int, y: Int): List<Move> {
        val moves = mutableListOf<Move>()

        val directions = listOf(
            Pair(-1, -1), Pair(-1, 1),
            Pair(1, -1), Pair(1, 1)
        )

        val isWhite = isWhitePiece(board[y][x])

        for (direction in directions) {
            var newX = x + direction.first
            var newY = y + direction.second

            while (newX in 0..7 && newY in 0..7) {
                val destinationPiece = board[newY][newX]

                if (destinationPiece == Piece.EMPTY) {
                    moves.add(Move(x, y, newX, newY))
                } else {
                    if (isSquareOccupiedByColor(board, newX, newY, !isWhite)) {
                        moves.add(Move(x, y, newX, newY))
                    }
                    break
                }

                newX += direction.first
                newY += direction.second
            }
        }

        return moves
    }



    fun getKnightMoves(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): List<Move> {
        val moves = mutableListOf<Move>()
        val offsets = listOf(-2, -1, 1, 2)

        for (dx in offsets) {
            for (dy in offsets) {
                if (kotlin.math.abs(dx) != kotlin.math.abs(dy)) {
                    val newX = x + dx
                    val newY = y + dy
                    if (isInBounds(newX, newY) && (!isSquareOccupiedByColor(board, newX, newY, isWhite))) {
                        moves.add(Move(x, y, newX, newY))
                    }
                }
            }
        }
        return moves
    }


    fun getPawnMoves(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): List<Move> {
        val moves = mutableListOf<Move>()

        val direction = if (isWhite) -1 else 1
        val initialRank = if (isWhite) 6 else 1

        // One square forward
        if (y + direction in 0..7 && board[y + direction][x] == Piece.EMPTY) {
            moves.add(Move(x, y, x, y + direction))

            // Two squares forward for the pawn's first move
            if (y == initialRank && board[y + 2 * direction][x] == Piece.EMPTY) {
                moves.add(Move(x, y, x, y + 2 * direction))
            }
        }

        // Capturing diagonally
        val captureOffsets = listOf(-1, 1)
        for (offset in captureOffsets) {
            val newX = x + offset
            if (newX in 0..7 && y + direction in 0..7) {
                val destinationPiece = board[y + direction][newX]
                if (isWhite && isBlackPiece(destinationPiece) || !isWhite && isWhitePiece(destinationPiece)) {
                    moves.add(Move(x, y, newX, y + direction))
                }
            }
        }

        return moves
    }


    fun switchTurns() {
        isWhiteToMove = !isWhiteToMove
    }

    fun isWhitePiece(piece: Piece): Boolean {
        return piece.name.startsWith('W')
    }

    fun isBlackPiece(piece: Piece): Boolean {
        return piece.name.startsWith('B')
    }

    fun isSquareOccupiedByColor(
        board: Array<Array<Piece>>,
        x: Int,
        y: Int,
        isWhite: Boolean
    ): Boolean {
        val piece = board[y][x]
        return if (isWhite) isWhitePiece(piece) else isBlackPiece(piece)
    }


    fun generateLegalMoves(
        board: Array<Array<Piece>>,
        isWhiteToMove: Boolean,
        selectedX: Int,
        selectedY: Int
    ): List<Move> {
        val legalMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]
                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
                    if (x == selectedX && y == selectedY) { // Add this check
                        val possibleMoves = when (piece) {
                            Piece.WHITE_PAWN -> getPawnMoves(board, x, y, true)
                            Piece.BLACK_PAWN -> getPawnMoves(board, x, y, false)
                            Piece.WHITE_KNIGHT -> getKnightMoves(board, x, y, true)
                            Piece.BLACK_KNIGHT -> getKnightMoves(board, x, y, false)
                            Piece.WHITE_BISHOP, Piece.BLACK_BISHOP -> getBishopMoves(board, x, y)
                            Piece.WHITE_ROOK, Piece.BLACK_ROOK -> getRookMoves(board, x, y)
                            Piece.WHITE_QUEEN, Piece.BLACK_QUEEN -> getQueenMoves(board, x, y)
                            Piece.WHITE_KING, Piece.BLACK_KING -> getKingMoves(x, y)
                            else -> emptyList()
                        }

                        for (move in possibleMoves) {
                            val destX = move.endX
                            val destY = move.endY
                            val destinationPiece = board[destY][destX]

                            if (!isSquareOccupiedByColor(board, destX, destY, isWhiteToMove) || isSquareOccupiedByColor(board, destX, destY, !isWhiteToMove)) {
                                Log.d("generateLegalMoves", "Piece: $piece, startX: $x, startY: $y, Adding legal move: $move")
                                //Other rules here
                                legalMoves.add(move)
                            }
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
        val startX = move.startX
        val startY = move.startY
        val legalMoves = generateLegalMoves(board, isWhiteToMove, startX, startY)

        Log.d("makeMove", "Legal moves: $legalMoves")

        if (isMoveLegal(move, legalMoves)) {
            val endX = move.endX
            val endY = move.endY

            // Move the piece
            board[endY][endX] = board[startY][startX]
            board[startY][startX] = Piece.EMPTY

            // TODO: Castling, En passant, Pawn promotion, King in check

            return true // Move was successful
        }
        Log.d("makeMove", "Move is illegal: $move, Legal moves: $legalMoves")
        return false // Move was not legal
    }


    fun makeMoveOnCurrentBoard(move: Move, isWhiteToMove: Boolean): Boolean {
        return makeMove(chessboard, move, isWhiteToMove)
    }
}

