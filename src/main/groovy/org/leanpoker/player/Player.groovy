package org.leanpoker.player

import groovy.json.JsonSlurper

class Player {

    static final String VERSION = 'Groovity 2.0'

	static def winningChance = null

	static def allInPerc = 0.6
	static def raisePerc = 0.4
	static def callPerc = 0.3

	static def allInBetPerc = 0.0625
	static def raiseStackPerc = 0.1
	static def chickenPerc = 0.5

	static boolean confIsSetup = false

    static int betRequest(def gameState) {
		try {
			if (!confIsSetup) {
				setupConf()
			}

			def cards = us(gameState).hole_cards
			if (winningChance == null) {
				winningChance = CsvRanking.getAvgWinChance(cards)
			}
			println("cards: $cards - chance: $winningChance")

			def minimumChips = gameState.current_buy_in - us(gameState).bet
			def bet = 0

			if (winningChance >= allInPerc) {
				bet = minimumChips + [us(gameState).stack * allInBetPerc, gameState.minimum_raise].max()
			} else if (winningChance >= raisePerc) {
				def diff = winningChance - raisePerc
				def raise = diff * (1 / (raisePerc - callPerc)) * us(gameState).stack * raiseStackPerc
				println 'raise ' + raise
				bet = minimumChips + [gameState.minimum_raise, raise].max()
			} else if (winningChance >= callPerc) {
				bet = minimumChips
			}

			println 'bet ' + bet
			if (bet > us(gameState).stack * chickenPerc) {
				return minimumChips
			}
			Math.ceil(bet)
		} catch (Exception e) {
			println e
			0
		}
	}

    static void showdown(def gameState) {
		winningChance = null
		confIsSetup = false
    }

	static def us(def gameState) {
		gameState.players.find { it.name == 'Groovity' }
	}

	static void setupConf() {
		try {
			def json = CsvRanking.downloadContent('https://www.dropbox.com/s/t52u0g3d8zqmy0r/leanpoker.txt?raw=1')
			def confValues = new JsonSlurper().parseText(json)
			allInPerc = confValues.allInPerc
			raisePerc = confValues.raisePerc
			callPerc = confValues.callPerc
			allInBetPerc = confValues.allInBetPerc
			raiseStackPerc = confValues.raiseStackPerc
			chickenPerc = confValues.chickenPerc
			confIsSetup = true
		} catch (Exception e) {
			println e
		}
	}

}
