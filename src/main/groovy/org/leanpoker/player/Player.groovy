package org.leanpoker.player

import groovy.json.JsonSlurper
import org.leanpoker.strategy.FlushStr
import org.leanpoker.strategy.FourOfAKindStr
import org.leanpoker.strategy.FullHouseStr
import org.leanpoker.strategy.HighCardStr
import org.leanpoker.strategy.OnePairStr
import org.leanpoker.strategy.RoyalFlushStr
import org.leanpoker.strategy.StraighFlushStr
import org.leanpoker.strategy.StraightStr
import org.leanpoker.strategy.ThreeOfAKindStr
import org.leanpoker.strategy.TwoPairStr

class Player {

    static final String VERSION = 'Groovity 2.0'

    static final long CONFIG_REFRESH_INTERVAL = 60

	static def winningChance = null

	static def allInPerc = 0.4
	static def raisePerc = 0.2
	static def callPerc = 0.1

	static def allInBetPerc = 0.6
	static def raiseStackPerc = 0.5
	static def chickenPerc = 1

	static boolean confIsSetup = false
	static def prevCards
    static long lastConfigDownload = 0

	static def apiCallCount = 1

	static def strategies = [
	        new HighCardStr(),
			new OnePairStr(),
			new TwoPairStr(),
			new ThreeOfAKindStr(),
			new StraightStr(),
			new FlushStr(),
			new FullHouseStr(),
			new FourOfAKindStr(),
			new StraighFlushStr(),
			new RoyalFlushStr()
	]

    static int betRequest(def gameState) {
        def bet = 0

		println 'API call #' + (apiCallCount++)

		try {
			if (!confIsSetup) {
				println '-' * 25 + ' UJ KOR ' + '-' * 25
				setupConf()
			}

			def cards = us(gameState).hole_cards
			def cardToken = "${cards[0].rank}${cards[0].suit}${cards[1].rank}${cards[1].suit}"
			if (winningChance == null || cardToken != prevCards) {
				println 'DOWNLOADING winning chance'
				winningChance = CsvRanking.getAvgWinChance(cards)
				prevCards = cardToken
			}
			println("cards: $cards - chance: $winningChance")

			def minimumChips = gameState.current_buy_in - us(gameState).bet

			if (winningChance >= allInPerc) {
				bet = minimumChips + [us(gameState).stack * allInBetPerc, gameState.minimum_raise].max()
				println 'all in'
			} else if (winningChance >= raisePerc) {
				def diff = winningChance - raisePerc
				def raise = diff * (1 / (raisePerc - callPerc)) * [us(gameState).stack, 1000].min() * raiseStackPerc
				println 'raise ' + raise
				bet = minimumChips + [gameState.minimum_raise, raise].max()
			} else if (winningChance >= callPerc) {
				if (minimumChips > us(gameState).stack * chickenPerc) {
					println 'bet too large, do not call'
					bet = 0
				} else {
					println 'call'
					bet = minimumChips
				}
			} else {
				println 'fold'
			}

			println 'bet ' + bet
			bet = Math.ceil(bet)
		} catch (Exception e) {
			println 'EXCEPTION ' + e
			bet = 0
		}

        bet
	}

    static void showdown(def gameState) {
		winningChance = null
        if (lastConfigDownload + CONFIG_REFRESH_INTERVAL * 1000 < System.currentTimeMillis()) {
            confIsSetup = false
        }
        println('=' * 78)
    }

	static def us(def gameState) {
		gameState.players.find { it.name == 'Groovity' }
	}

	static void setupConf() {
		try {
			println 'DOWNLOADING config'
			def json = CsvRanking.downloadContent('https://www.dropbox.com/s/t52u0g3d8zqmy0r/leanpoker.txt?raw=1')
			def confValues = new JsonSlurper().parseText(json)
			allInPerc = confValues.allInPerc
			raisePerc = confValues.raisePerc
			callPerc = confValues.callPerc
			allInBetPerc = confValues.allInBetPerc
			raiseStackPerc = confValues.raiseStackPerc
			chickenPerc = confValues.chickenPerc
			confIsSetup = true
            lastConfigDownload = System.currentTimeMillis()
		} catch (Exception e) {
			println e
		}
	}

	static def calculateMaxChances(def gameState) {
		strategies.collect {
			str ->
				def chance = str.calculateChance(gameState)
				def weight = str.getWeight()
				chance * weight
		}.max()
	}

}
