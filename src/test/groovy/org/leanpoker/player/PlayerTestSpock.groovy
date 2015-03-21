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
		def bet = Player.betRequest(gameState)
		println bet

		expect:
		bet >= 0
	}

	def "download test"() {
		given:
		def bas = new ByteArrayOutputStream()
		def ous = new BufferedOutputStream(bas)
		ous << new URL('https://www.dropbox.com/s/t52u0g3d8zqmy0r/leanpoker.txt?raw=1').openStream()
		println bas.toString()

		expect:
		true
	}
}
