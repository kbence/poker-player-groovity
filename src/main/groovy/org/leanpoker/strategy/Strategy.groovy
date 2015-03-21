package org.leanpoker.strategy

interface Strategy {

	def calculateChance(def gameState)

	def getWeight()

}