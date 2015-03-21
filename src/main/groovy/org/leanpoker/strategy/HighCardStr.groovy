package org.leanpoker.strategy

/**
 * Created by KZS on 2015.03.21..
 */
class HighCardStr implements Strategy {

	@Override
	def calculateChance(def gameState) {
		1
	}

	@Override
	def getWeight() {
		1
	}
}
