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
		def gameState = new JsonSlurper().parseText('{\n' +
                '  "players":[\n' +
                '    {\n' +
                '      "name":"Player 1",\n' +
                '      "stack":1000,\n' +
                '      "status":"active",\n' +
                '      "bet":0,\n' +
                '      "hole_cards":[],\n' +
                '      "version":"Version name 1",\n' +
                '      "id":0\n' +
                '    },\n' +
                '    {\n' +
                '      "name":"Player 2",\n' +
                '      "stack":1000,\n' +
                '      "status":"active",\n' +
                '      "bet":0,\n' +
                '      "hole_cards":[],\n' +
                '      "version":"Version name 2",\n' +
                '      "id":1\n' +
                '    }\n' +
                '  ],\n' +
                '  "small_blind":10,\n' +
                '  "orbits":0,\n' +
                '  "dealer":0,\n' +
                '  "community_cards":[],\n' +
                '  "current_buy_in":0,\n' +
                '  "pot":0\n' +
                '}')

		expect:
		Player.betRequest(gameState) >= 0
	}
}
