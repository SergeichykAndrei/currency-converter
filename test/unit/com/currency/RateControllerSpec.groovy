package com.currency

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import java.math.RoundingMode

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(RateController)
@Mock([Rate])
class RateControllerSpec extends Specification {

    void "Getting a list of rates"() {
        given: "List of exchange rates in the db"
        def SEK = newRate('SEK', 10.66)
        def SGD = newRate('SGD', 1.51)
        def THB = newRate('THB', 4.39)
        def PHP = newRate('PHP', 6.19)

        List<Rate> rates = [SEK, SGD, THB, PHP]
        Rate.saveAll(rates)

        when: "the rates is invoked"
        def model = controller.rates()

        then: "the rates is in the returned model"
        model.rates.size() == 4
        model.rates*.currency.currencyCode.containsAll(['SEK', 'SGD', 'THB', 'PHP'])
    }

    void "Create new rate"() {
        given: "a mocked rate service"
        def mockRateService = Mock(RateService)
        mockRateService.createRate(_) >> newRate('NGN', 45.0)
        controller.rateService = mockRateService

        when: "controller is invoked"
        controller.newRate(new RateCommand(currency: 'NGN', value: 45.0))

        then: "redirected to rates"
        response.redirectedUrl == '/rate/rates'
    }

    void "Create new rate with invalid currency"() {
        when: "controller is invoked with invalid currency"
        controller.newRate(new RateCommand(currency: 'currency', value: 45.0))

        then: "the message is in the returned model"
        flash.message ==~ /Invalid currency or currency value/
    }

    void "Create new rate with invalid currency value"() {
        when: "controller is invoked with invalid currency"
        controller.newRate(new RateCommand(currency: 'NGN', value: 'value'))

        then: "the message is in the returned model"
        flash.message ==~ /Invalid currency or currency value/
    }

    @Unroll
    void "RateCommand object for validate params correctly"() {
        given: "a mocked command object"
        def rc = mockCommandObject(RateCommand)

        and: "a set of initial values from the spock test"
        rc.currency = currency
        rc.value = value

        when: "the validator is invoked"
        def isValidRate = rc.validate()

        then: "the appropriate fields are flagged as errors"
        isValidRate == anticipatedValid
        rc.errors.getFieldError(fieldInError)?.code == errorCode

        where:
        currency | value      | anticipatedValid | fieldInError | errorCode
        "USD"    | "1.45"     | true             | null         | null
        "USDD"   | "1.45"     | false            | "currency"   | "size.toobig"
        "USD"    | "1.912321" | false            | "value"      | "size.toobig"
        ""       | "1.45"     | false            | "currency"   | "blank"
        "USD"    | ""         | false            | "value"      | "blank"
    }

    Rate newRate(String currency, BigDecimal value) {
        new Rate(
                currency: Currency.getInstance(currency),
                value: new BigDecimal(value).setScale(4, RoundingMode.DOWN))
    }
}
