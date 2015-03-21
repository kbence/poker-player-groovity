package org.leanpoker.player

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Created by KZS on 2015.03.21..
 */
class CsvRankingTest extends Specification {
	def "DownloadCsv"() {
		given:
		def gameState = new JsonSlurper().parseText(new File('src/test/resources/gamestate.json').text)

		expect:
		print CsvRanking.getAvgWinChance(gameState.players.find { it.name == 'Groovity' }.hole_cards)
	}
}
