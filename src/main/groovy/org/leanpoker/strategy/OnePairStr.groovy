package org.leanpoker.strategy

/**
 * Created by KZS on 2015.03.21..
 */
class OnePairStr implements Strategy {

	@Override
	def calculateChance(def Object gameState) {
		1
	}

	@Override
	def getWeight() {
		5
	}
}
