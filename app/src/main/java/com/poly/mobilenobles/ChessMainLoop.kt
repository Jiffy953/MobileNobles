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

    /**
     * Initializes the chessboard with pieces in starting positions.
     * @return 8x8 array of Piece objects with the initial chessboard setup.
     */

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

    /**
     * Data class representing a chess move.
     * @property startX The starting x-coordinate of the move (0-7).
     * @property startY The starting y-coordinate of the move (0-7).
     * @property endX The ending x-coordinate of the move (0-7).
     * @property endY The ending y-coordinate of the move (0-7).
     */
    data class Move(val startX: Int, val startY: Int, val endX: Int, val endY: Int)

    /**
     * Checks if the given coordinates are within the bounds of a chessboard.
     * @param x The x-coordinate (0-7).
     * @param y The y-coordinate (0-7).
     * @return true if the coordinates are within bounds, false otherwise.
     */
    fun isInBounds(x: Int, y: Int): Boolean {
        return x in 0..7 && y in 0..7
    }

    /**
     * Computes the list of legal king moves from the given position (x, y).
     * @param x The x-coordinate of the king's current position.
     * @param y The y-coordinate of the king's current position.
     * @return A list of legal Move objects representing the king's available moves.
     */
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

    /**
     * Computes the list of legal rook moves from the given position (x, y) on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the rook's current position.
     * @param y The y-coordinate of the rook's current position.
     * @return A list of legal Move objects representing the rook's available moves.
     */
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

    /**
     * Computes the list of legal bishop moves from the given position (x, y) on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the bishop's current position.
     * @param y The y-coordinate of the bishop's current position.
     * @return A list of legal Move objects representing the bishop's available moves.
     */
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

    /**
     * Computes the list of legal queen moves from the given position (x, y) on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the queen's current position.
     * @param y The y-coordinate of the queen's current position.
     * @return A list of legal Move objects representing the queen's available moves.
     */
    fun getQueenMoves(board: Array<Array<Piece>>, x: Int, y: Int): List<Move> {
        // Queen's moves are combination of rook and bishop moves
        return getRookMoves(board, x, y) + getBishopMoves(board, x, y)
    }

    /**
     * Computes the list of legal knight moves from the given position (x, y) on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the knight's current position.
     * @param y The y-coordinate of the knight's current position.
     * @param isWhite A boolean indicating whether the knight is white or black.
     * @return A list of legal Move objects representing the knight's available moves.
     */
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

    /**
     * Computes the list of legal pawn moves from the given position (x, y) on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the pawn's current position.
     * @param y The y-coordinate of the pawn's current position.
     * @param isWhite A boolean indicating whether the pawn is white or black.
     * @return A list of legal Move objects representing the pawn's available moves.
     */
    fun getPawnMoves(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): List<Move> {
        val moves = mutableListOf<Move>()

        val direction = if (isWhite) -1 else 1
        val initialRank = if (isWhite) 6 else 1

        // One square forward
        if (y + direction in 0..7 && board[y + direction][x] == Piece.EMPTY) {
            moves.add(Move(x, y, x, y + direction))

            // Two squares forward if its the pawn's first move
            if (y == initialRank && board[y + 2 * direction][x] == Piece.EMPTY) {
                moves.add(Move(x, y, x, y + 2 * direction))
            }
        }

        // Capturing
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

    /**
     * Finds the position of the king of the specified color on the given board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param isWhite A boolean indicating whether to search for the white or black king.
     * @return A Pair object representing the (x, y) coordinates of the king, or null if not found.
     */
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

    /**
     * Determines whether the given position (x, y) on the specified board is attacked by any of the pieces of the opposite color.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the position to check.
     * @param y The y-coordinate of the position to check.
     * @param isWhite A boolean indicating whether the position is being attacked by white or black pieces.
     * @return True if the position is attacked, false otherwise.
     */
    fun isPositionAttacked(board: Array<Array<Piece>>, x: Int, y: Int, isWhite: Boolean): Boolean {
        val oppositeMoves = generateAttackingMoves(board, isWhite)
        return oppositeMoves.any { move -> move.endX == x && move.endY == y }
    }

    /**
    Switches the turn to the other player.
     */
    fun switchTurns() {
        isWhiteToMove = !isWhiteToMove
    }

    /**
     * Determines whether the specified piece is a white piece.
     * @param piece The piece to check.
     * @return True if the piece is white, false otherwise.
     */
    fun isWhitePiece(piece: Piece): Boolean {
        return piece.name.startsWith('W')
    }

    /**
     * Determines whether the specified piece is a white piece.
     * @param piece The piece to check.
     * @return True if the piece is white, false otherwise.
     */
    fun isBlackPiece(piece: Piece): Boolean {
        return piece.name.startsWith('B')
    }

    /**
     * Determines whether the specified square on the board is occupied by a piece of the specified color.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param x The x-coordinate of the square to check.
     * @param y The y-coordinate of the square to check.
     * @param isWhite A boolean indicating whether to check for a white or black piece.
     * @return True if the square is occupied by a piece of the specified color, false otherwise.
     */
    fun isSquareOccupiedByColor(
        board: Array<Array<Piece>>,
        x: Int,
        y: Int,
        isWhite: Boolean
    ): Boolean {
        val piece = board[y][x]
        return if (isWhite) isWhitePiece(piece) else isBlackPiece(piece)
    }
    /**
     * Generates a list of all attacking moves for the current player on the specified board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param isWhiteToMove A boolean indicating whether it is currently white's turn to move.
     * @return A list of all attacking Move objects for the current player.
     */
    fun generateAttackingMoves(board: Array<Array<Piece>>, isWhiteToMove: Boolean): List<Move> {
        val attackingMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]

                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
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

                        if (!isSquareOccupiedByColor(board, destX, destY, isWhiteToMove) || isSquareOccupiedByColor(board, destX, destY, !isWhiteToMove)) {
                            attackingMoves.add(move)
                        }
                    }
                }
            }
        }

        return attackingMoves
    }

    /**
     * Generates a list of all legal moves for the current player on the specified board, taking into account
     * whether the player's king is in check.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param isWhiteToMove A boolean indicating whether it is currently white's turn to move.
     * @return A list of all legal Move objects for the current player.
     */
    fun generateLegalMovesForCheck(board: Array<Array<Piece>>, isWhiteToMove: Boolean): List<Move> {
        val legalMoves = mutableListOf<Move>()

        for (y in 0..7) {
            for (x in 0..7) {
                val piece = board[y][x]

                if ((isWhiteToMove && isWhitePiece(piece)) || (!isWhiteToMove && isBlackPiece(piece))) {
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

    /**
     * Determines if the game has reached a checkmate state for the specified player on the given board.
     * @param board The game board as a two-dimensional array of Piece objects.
     * @param isWhiteToMove A boolean indicating whether it is currently white's turn to move.
     * @return A boolean indicating if the game has reached a checkmate state for the current player.
     */
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

    /**
     * Creates a deep copy of a two-dimensional array of Piece objects.
     * @return A new two-dimensional array with the same dimensions and elements as the original.
     */
    fun Array<Array<Piece>>.deepCopy(): Array<Array<Piece>> {
        return Array(this.size) { i ->
            Array(this[i].size) { j ->
                this[i][j]
            }
        }
    }

    /**
     * Determines if a given move is legal according to the list of legal moves for the current player.
     * @param move The move to be checked.
     * @param legalMoves The list of legal moves for the current player.
     * @return A boolean indicating if the move is legal.
     */
    fun isMoveLegal(move: Move, legalMoves: List<Move>): Boolean {
        return move in legalMoves
    }

    /**
    * Makes a given move on the chess board, if it is a legal move for the current player.
    * @param board The current state of the chess board.
    * @param move The move to be made.
    * @param isWhiteToMove A boolean indicating if it is currently white's turn to move.
    * @param checkKingSafety A boolean indicating if king safety should be checked after the move is made.
    * @return A boolean indicating if the move was successfully made.
     */
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

    /**
     * Make a move on the current chessboard.
     *
     * @param move The move to make.
     * @param isWhiteToMove True if it's white's turn to move, false if it's black's turn.
     * @return The result of the move - SUCCESS if the move was successful, CHECKMATE if the move resulted in checkmate, INVALID if the move was illegal.
     */
    fun makeMoveOnCurrentBoard(move: Move, isWhiteToMove: Boolean): MoveResult {
        val moveSuccessful = makeMove(chessboard, move, isWhiteToMove)
        if (moveSuccessful) {
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

