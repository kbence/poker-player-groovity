package org.leanpoker.player

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Java/Groovy unit test using the Spock framework.
 * https://code.google.com/p/spock/
 */
class PlayerTestSpock extends Specification {

	def "Player should return valid value"() {
		given:
		def gameState = new JsonSlurper().parseText(new File('src/test/resources/gamestate.json').text)

		expect:
		Player.betRequest(gameState) >= 0
	}
}
