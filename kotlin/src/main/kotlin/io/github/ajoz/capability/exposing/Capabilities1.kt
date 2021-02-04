package io.github.ajoz.capability.exposing

class Card {

}

class Player {

}

class GameResult {

}

class GameState {
    // Returns all possible move capabilities for the given state
    fun getAvailableCapabilities(): List<MoveCapabilities> =
            TODO()

    // Uses one of the capabilities for a given player
    fun run(player: Player, capability: MoveCapabilities) {
    }

    // Check the status of the game
    fun getGameResult(): GameResult =
            TODO()
}

sealed class MoveCapabilities {
    data class PLAY_CARDS(val cards: List<Card>) : MoveCapabilities()
    data class DRAW_CARDS(val amount: Int) : MoveCapabilities()
    object END_TURN : MoveCapabilities()
    object FORFEIT_GAME : MoveCapabilities()
}