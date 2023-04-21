package com.poly.mobilenobles


enum class Piece {
    WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EM
}

fun initializeChessboard(): Array<Array<Piece>> {
    return arrayOf(
        arrayOf(Piece.BR, Piece.BN, Piece.BB, Piece.BQ, Piece.BK, Piece.BB, Piece.BN, Piece.BR),
        arrayOf(Piece.BP, Piece.BP, Piece.BP, Piece.BP, Piece.BP, Piece.BP, Piece.BP, Piece.BP),
        arrayOf(Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM),
        arrayOf(Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM),
        arrayOf(Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM),
        arrayOf(Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM, Piece.EM),
        arrayOf(Piece.WP, Piece.WP, Piece.WP, Piece.WP, Piece.WP, Piece.WP, Piece.WP, Piece.WP),
        arrayOf(Piece.WR, Piece.WN, Piece.WB, Piece.WQ, Piece.WK, Piece.WB, Piece.WN, Piece.WR)
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


class ChessMainLoop {

    override fun toString(): String {
        val chessboard = initializeChessboard()
        return chessboardToString(chessboard)
    }
}
