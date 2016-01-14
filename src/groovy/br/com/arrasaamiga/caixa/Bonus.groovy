package br.com.arrasaamiga.caixa

import groovy.transform.ToString

@ToString(includeNames=true)
class Bonus {

	Date weekStart, weekEnd
	List<Date> strikeDates

}