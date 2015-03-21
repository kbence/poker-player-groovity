package org.leanpoker.player

class Player {

    static final String VERSION = 'Groovity 1.0'

	static def winningChance = null

	static def allInPerc = 0.6
	static def raisePerc = 0.4
	static def callPerc = 0.3

    static int betRequest(def gameState) {
		try {
			def cards = us(gameState).hole_cards
			if (winningChance == null) {
				winningChance = CsvRanking.getAvgWinChance(cards)
			}
			println("cards: $cards - chance: $winningChance")

			def minimumChips = gameState.current_buy_in - us(gameState).bet
			def bet = 0

			if (winningChance >= allInPerc) {
				bet = minimumChips + [us(gameState).stack / 16, gameState.minimum_raise].max()
			} else
			if (winningChance >= raisePerc) {
				def diff = winningChance - raisePerc
				def raise = diff * 5 * us(gameState).stack * 0.1
				println 'raise ' + raise
				bet = minimumChips + [gameState.minimum_raise, raise].max()
			} else
			if (winningChance >= callPerc) {
				bet = minimumChips
			}

			println 'bet ' + bet
			if (bet > us(gameState).stack / 2) {
				return minimumChips
			}
			bet
		} catch (Exception e) {
			println e
			0
		}
	}

    static void showdown(def gameState) {
		winningChance = null
    }

	static def us(def gameState) {
		gameState.players.find { it.name == 'Groovity' }
	}

}
