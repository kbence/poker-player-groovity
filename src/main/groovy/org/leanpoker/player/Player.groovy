package org.leanpoker.player

class Player {

    static final String VERSION = 'Groovity 1.0'

    static int betRequest(def gameState) {
		try {
			def cards = us(gameState).hole_cards
			if (cards[0].rank == cards[1].rank) {
				return gameState.current_buy_in - us(gameState).bet + gameState.minimum_raise
			}
			print gameState
			Math.floor(Math.random() * 1000) as Integer
		} catch (Exception e) {
			0
		}
	}

    static void showdown(def gameState) {
    }

	static def us(def gameState) {
		gameState.players.find { it.name == 'Groovity' }
	}

}
