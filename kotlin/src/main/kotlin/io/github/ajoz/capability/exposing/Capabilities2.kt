package io.github.ajoz.capability.exposing

class BetterGameState {
    // Returns all possible move capabilities for the given state
    fun getAvailableCapabilities(): List<Capabilities> =
            TODO()

    // Check the status of the game
    fun getGameResult(): GameResult =
            TODO()

    fun getCurrentPlayer(): Player =
            TODO()
}

sealed class Capabilities(val move: () -> BetterGameState) {
    class PLAY_CARDS(
            val cards: List<Card>,
            move: () -> BetterGameState
    ) : Capabilities(move)

    class DRAW_CARDS(
            val amount: Int,
            move: () -> BetterGameState
    ) : Capabilities(move)

    class END_TURN(
            move: () -> BetterGameState
    ) : Capabilities(move)

    class FORFEIT_GAME(
            move: () -> BetterGameState
    ) : Capabilities(move)
}

fun main() {
    val game = BetterGameState()
    val capabilities = game.getAvailableCapabilities()
    // we can present the options to the player
    // let's assume the player picked one of the capabilities:
    capabilities[0].move() // <-- running the capability
    // player info is baked in, we cannot do an invalid move
}