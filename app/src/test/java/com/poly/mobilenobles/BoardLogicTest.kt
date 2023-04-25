package com.poly.mobilenobles

import org.junit.Test
import org.junit.Assert.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import com.poly.mobilenobles.ChessMainLoop.Move


class BoardLogicTest {
    private val chessMainLoop = ChessMainLoop()

    @Test // Test valid board coordinates
    fun testisInBounds_True() {
        val validCoordinates = listOf(
            Pair(0, 0),
            Pair(7, 0),
            Pair(0, 7),
            Pair(7, 7),
            Pair(3, 4),
            Pair(6, 2)
        )

        for (coordinate in validCoordinates) {
            val (x, y) = coordinate
            assertTrue("($x, $y) should be in bounds", chessMainLoop.isInBounds(x, y))
        }
    }

    @Test // Test invalid board coordinates
    fun testisInBounds_False() {
        val invalidCoordinates = listOf(
            Pair(-1, 0),
            Pair(0, -1),
            Pair(-1, -1),
            Pair(8, 0),
            Pair(0, 8),
            Pair(8, 8),
            Pair(9, 9)
        )

        for (coordinate in invalidCoordinates) {
            val (x, y) = coordinate
            assertFalse("($x, $y) should be out of bounds", chessMainLoop.isInBounds(x, y))
        }
    }

    @Test // Put king in middle of board, should be able to move in any position
    fun testGetKingMoves() {
        val kingPosition = Pair(4, 4)
        val expectedMoves = setOf(
            Move(4, 4, 3, 3), Move(4, 4, 4, 3), Move(4, 4, 5, 3),
            Move(4, 4, 3, 4), /* King Here */ Move(4, 4, 5, 4),
            Move(4, 4, 3, 5), Move(4, 4, 4, 5), Move(4, 4, 5, 5)
        )

        val kingMoves = chessMainLoop.getKingMoves(kingPosition.first, kingPosition.second).toSet()

        assertEquals(expectedMoves, kingMoves)
    }

    @Test //Covers both rook and bishop
    fun testGetQueenMoves() {
        // Create a chessboard with a white queen at position (3, 3)
        val board: Array<Array<ChessMainLoop.Piece>> = Array(8) { Array(8) { ChessMainLoop.Piece.EMPTY } }
        board[3][3] = ChessMainLoop.Piece.WHITE_QUEEN

        // Get the moves for the queen
        val moves = chessMainLoop.getQueenMoves(board, 3, 3)

        // Expected move positions
        val expectedMoves = setOf(
            // Rook moves
            Pair(0, 3), Pair(1, 3), Pair(2, 3),
            Pair(4, 3), Pair(5, 3), Pair(6, 3), Pair(7, 3),
            Pair(3, 0), Pair(3, 1), Pair(3, 2),
            Pair(3, 4), Pair(3, 5), Pair(3, 6), Pair(3, 7),
            // Bishop moves
            Pair(0, 0), Pair(1, 1), Pair(2, 2),
            Pair(4, 4), Pair(5, 5), Pair(6, 6), Pair(7, 7),
            Pair(6, 0), Pair(5, 1), Pair(4, 2),
            Pair(2, 4), Pair(1, 5), Pair(0, 6)
        )

        // Validate the moves
        for (move in moves) {
            val position = Pair(move.endX, move.endY)
            assertTrue("Move $move is not in the expectedMoves set", expectedMoves.contains(position))
        }
        assertEquals("Size of the generated moves list does not match the size of the expected moves set", expectedMoves.size, moves.size)
    }
    @Test
    fun testGetKnightMoves() {
        // Create a chessboard with a white knight at position (3, 3)
        val board: Array<Array<ChessMainLoop.Piece>> = Array(8) { Array(8) { ChessMainLoop.Piece.EMPTY } }
        board[3][3] = ChessMainLoop.Piece.WHITE_KNIGHT

        // Get the moves for the knight
        val moves = chessMainLoop.getKnightMoves(board, 3, 3, true)

        // Expected move positions
        val expectedMoves = setOf(
            Pair(1, 2), Pair(2, 1), Pair(4, 1), Pair(5, 2),
            Pair(5, 4), Pair(4, 5), Pair(2, 5), Pair(1, 4)
        )

        // Validate the moves
        for (move in moves) {
            val position = Pair(move.endX, move.endY)
            assertTrue("Move $move is not in the expectedMoves set", expectedMoves.contains(position))
        }

        assertEquals("Size of the generated moves list does not match the size of the expected moves set", expectedMoves.size, moves.size)
    }

    @Test
    fun testGetPawnMoves() {
        // Create board with a white pawn at position (4, 3) and black pawn at position (5, 4)
        val board: Array<Array<ChessMainLoop.Piece>> = Array(8) { Array(8) { ChessMainLoop.Piece.EMPTY } }
        board[3][4] = ChessMainLoop.Piece.WHITE_PAWN
        board[4][5] = ChessMainLoop.Piece.BLACK_PAWN

        // Get the moves for the white pawn
        val whitePawnMoves = chessMainLoop.getPawnMoves(board, 4, 3, true)

        // Validate the moves for the white pawn
        val expectedWhitePawnMoves = setOf(
            Pair(4, 2) // Forward
        )

        for (move in whitePawnMoves) {
            val position = Pair(move.endX, move.endY)
            assertTrue("Move $move is not in the expectedWhitePawnMoves set", expectedWhitePawnMoves.contains(position))
        }

        assertEquals("Size of the generated whitePawnMoves list does not match the size of the expectedWhitePawnMoves set", expectedWhitePawnMoves.size, whitePawnMoves.size)

        // Get the moves for the black pawn
        val blackPawnMoves = chessMainLoop.getPawnMoves(board, 5, 4, false)

        // Print black pawn moves
        println("Generated blackPawnMoves:")
        blackPawnMoves.forEach { println(it) }

        // Validate the moves for the black pawn
        val expectedBlackPawnMoves = setOf(
            Pair(5, 5), // Forward
            Pair(4, 5)  // Capture
        )

        for (move in blackPawnMoves) {
            val position = Pair(move.endX, move.endY)
            assertTrue("Move $move is not in the expectedBlackPawnMoves set", expectedBlackPawnMoves.contains(position))
        }

        assertEquals("Size of the generated blackPawnMoves list does not match the size of the expectedBlackPawnMoves set", expectedBlackPawnMoves.size, blackPawnMoves.size)
    }
}
