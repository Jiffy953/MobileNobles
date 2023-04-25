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


    var lastMove: Move? = null
    fun getPawnMoves(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean, lastMove: Move?): List<Move> {
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

        // Add en passant capture moves
        if (isWhite && y == 3 && lastMove != null) {
            val lastMovePiece = board[lastMove.startY][lastMove.startX]
            if (lastMovePiece == Piece.BLACK_PAWN && lastMove.startY == 6 && lastMove.endY == 4) {
                if (lastMove.endX == x - 1 || lastMove.endX == x + 1) {
                    moves.add(Move(x, y, lastMove.endX, y + direction))
                }
            }
        } else if (!isWhite && y == 4 && lastMove != null) {
            val lastMovePiece = board[lastMove.startY][lastMove.startX]
            if (lastMovePiece == Piece.WHITE_PAWN && lastMove.startY == 1 && lastMove.endY == 3) {
                if (lastMove.endX == x - 1 || lastMove.endX == x + 1) {
                    moves.add(Move(x, y, lastMove.endX, y + direction))
                }
            }
        }

        return moves
    }


    fun findKing(board: Array<Array<Piece>>, isWhite: Boolean): Pair<Int, Int>? {
        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]
                if ((isWhite && piece == Piece.WHITE_KING) || (!isWhite && piece == Piece.BLACK_KING)) {
                    return Pair(x, y)
                }
            }
        }
        return null
    }

    fun isPositionAttacked(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): Boolean {
        val oppositeMoves = generateAttackingMoves(board, isWhite, lastMove)
        return oppositeMoves.any { move -> move.endX == x && move.endY == y }
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

    fun generateAttackingMoves(board: Array<Array<Piece>>, isWhiteToMove: Boolean, lastMove: Move?): List<Move> {
        val attackingMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]

                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
                    val possibleMoves = when (piece) {
                        Piece.WHITE_PAWN -> getPawnMoves(board, x, y, true, lastMove)
                        Piece.BLACK_PAWN -> getPawnMoves(board, x, y, false, lastMove)
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
                            attackingMoves.add(move)
                        }
                    }
                }
            }
        }

        return attackingMoves
    }



    fun generateLegalMovesForCheck(board: Array<Array<Piece>>, isWhiteToMove: Boolean): List<Move> {
        val legalMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]

                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
                    val possibleMoves = when (piece) {
                        Piece.WHITE_PAWN -> getPawnMoves(board, x, y, true, lastMove)
                        Piece.BLACK_PAWN -> getPawnMoves(board, x, y, false, lastMove)
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
                            val tempBoard = board.deepCopy()
                            tempBoard[destY][destX] = tempBoard[y][x]
                            tempBoard[y][x] = Piece.EMPTY
                            val kingPosition = findKing(tempBoard, isWhiteToMove)
                            if (kingPosition != null) {
                                val (kingX, kingY) = kingPosition
                                Log.d("generateLegalMovesForCheck", "Checking king's safety at position: ($kingX, $kingY)")
                                if (!isPositionAttacked(tempBoard, kingX, kingY, !isWhiteToMove)) {
                                    legalMoves.add(move)
                                } else {
                                    Log.d("generateLegalMovesForCheck", "King is in check after move: $move")
                                }
                            }
                        }
                    }
                }
            }
        }

        return legalMoves
    }

    fun isCheckmate(board: Array<Array<Piece>>, isWhiteToMove: Boolean): Boolean {
        val kingPosition = findKing(board, isWhiteToMove)
        if (kingPosition != null) {
            val (kingX, kingY) = kingPosition
            if (isPositionAttacked(board, kingX, kingY, !isWhiteToMove)) {
                val legalMoves = generateLegalMovesForCheck(board, isWhiteToMove)
                if (legalMoves.isEmpty()) {
                    return true
                }
            }
        }
        return false
    }


    fun Array<Array<Piece>>.deepCopy(): Array<Array<Piece>> {
        return Array(this.size) { i ->
            Array(this[i].size) { j ->
                this[i][j]
            }
        }
    }


    fun isMoveLegal(move: Move, legalMoves: List<Move>): Boolean {
        return move in legalMoves
    }

    fun makeMove(board: Array<Array<Piece>>, move: Move, isWhiteToMove: Boolean, checkKingSafety: Boolean = true): Boolean {
        val startX = move.startX
        val startY = move.startY

        val legalMoves = generateLegalMovesForCheck(board, isWhiteToMove)
        Log.d("makeMove", "Legal moves for all pieces: $legalMoves")

        if (isMoveLegal(move, legalMoves)) {
            val endX = move.endX
            val endY = move.endY
            // Move the piece
            board[endY][endX] = board[startY][startX]
            board[startY][startX] = Piece.EMPTY
            // TODO: Castling, En passant, Pawn promotion
            return true // Move was successful
        }

        Log.d("makeMove", "Move is illegal: $move, Legal moves: $legalMoves")
        return false // Move was not legal
    }

    enum class MoveResult {
        SUCCESS,
        CHECKMATE,
        INVALID
    }



    fun makeMoveOnCurrentBoard(move: Move, isWhiteToMove: Boolean): MoveResult {
        val moveSuccessful = makeMove(chessboard, move, isWhiteToMove)
        if (moveSuccessful) {
            lastMove = move
            if (isCheckmate(chessboard, !isWhiteToMove)) {
                Log.d("makeMoveOnCurrentBoard", "Checkmate! ${if (isWhiteToMove) "White" else "Black"} wins!")
                // Add here for any messages after the game is complete
                return MoveResult.CHECKMATE
            } else {
                return MoveResult.SUCCESS
            }
        } else {
            return MoveResult.INVALID
        }
    }
}

