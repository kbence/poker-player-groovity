package org.leanpoker.player

/**
 * Created by KZS on 2015.03.21..
 */
class CsvRanking {

	static def rankValues = ['2','3','4','5','6','7','8','9','10','J','Q','K','A']
	static def suitValues = ['spades','hearts','clubs','diamonds']

	static def downloadCsv(def cards) {
		def url = getCsvUrl(cards)

		def bas = new ByteArrayOutputStream()
		def ous = new BufferedOutputStream(bas)
		ous << new URL(url).openStream()
		return bas.toString()
	}

	static def getCsvUrl(def cards) {
		def c1 = getCardNumber(cards[0])
		def c2 = getCardNumber(cards[1])

        def card1 = [c1, c2].min()
        def card2 = [c1, c2].max()

		"http://holdem-odds.chrisbeaumont.org.s3-website-us-east-1.amazonaws.com/data/${card1}_${card2}.csv" as String
	}

	private static int getCardNumber(card) {
		rankValues.indexOf(card.rank) * 4 + suitValues.indexOf(card.suit)
	}

	static def getAvgWinChance(def cards) {
		def csvContent = downloadCsv(cards)
		def perc = []
		csvContent.eachLine {
			line ->
				def chance = line.substring(line.indexOf(',') + 1)
				if (chance.isNumber()) {
					perc << chance.toFloat()
				}
		}
		return perc.sum() / perc.size()
	}
}

