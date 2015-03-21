package org.leanpoker.player

/**
 * Created by KZS on 2015.03.21..
 */
class CsvRanking {

	static def rankValues = ['2','3','4','5','6','7','8','9','J','Q','K','A']
	static def suitValues = ['S','H','C','D']

	static void downloadCsv(def cards) {
		def url = getCsvUrl(cards)

		def bas = new ByteArrayOutputStream()
		def ous = new BufferedOutputStream(bas)
		ous << new URL(url).openStream()
		println bas.toString()
		ous.close()
	}

	static def getCsvUrl(def cards) {
		def card1 = getCardNumber(cards[0])
		def card2 = getCardNumber(cards[1])
		"http://holdem-odds.chrisbeaumont.org.s3-website-us-east-1.amazonaws.com/data/${card1}_${card2}.csv" as String
	}

	private static int getCardNumber(card) {
		rankValues.indexOf(card.rank) * 4 + suitValues.indexOf(card.suit)
	}



}
