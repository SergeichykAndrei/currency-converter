package com.currency

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RateRestController)
@Mock([Rate])
class RateRestControllerSpec extends Specification {

    void setupSpec() {
        defineBeans {
            springSecurityService(SpringSecurityService)
        }
    }

    void "Getting the rate"() {
        given: "mock rate services"
        controller.rateService = Mock(RateService) {
            getRate('EUR') >> new Rate(currency: Currency.getInstance('EUR'), value: 4.7)
        }

        when: "Invoke getRate"
        controller.getRate('EUR')

        then: "I get expected rate as JSON"
        response.json.currency == 'EUR'
        response.json.value == '4.7'
    }

    void "Getting the rate invalid value"() {
        given: "mock rate services"
        controller.rateService = Mock(RateService) {
            getRate('EURR') >> { throw new IllegalArgumentException() }
        }

        when: "Invoke getRate with invalid currency"
        controller.getRate('EURR')

        then: "I get expected error message"
        response.json.error == 'invalid currency or currency value'
    }

    void "Getting the cross rate"() {
        given: "mock rate services"
        String current = 'EUR'
        String target = 'USD'
        controller.rateService = Mock(RateService) {
            getCrossRateValue(current, target) >> 1.5
        }

        when: "Invoke the getCrossRateValue action as JSON"
        controller.getCrossRate(current, target)

        then: "Get the expected cross rate"
        response.json.currentCurrency == 'EUR'
        response.json.targetCurrency == 'USD'
        response.json.rateValue == 1.5
    }

    void "Getting the cross rate invalid value"() {
    given: "mock rate services"
    controller.rateService = Mock(RateService) {
        getCrossRateValue('EURR', 'USDD') >> { throw new IllegalArgumentException() }
    }

    when: "Invoke getRate with invalid currency"
    controller.getCrossRate('EURR', 'USDD')

    then: "I get expected error message"
    response.json.error == 'invalid currency or currency value'
    }
}
