package org.currency

import com.currency.Rate
import grails.test.spock.IntegrationSpec
import spock.lang.Unroll

class RateIntegrationSpec extends IntegrationSpec {

    @Unroll
    void "Get list of rates"() {
        when: "invoke list with params"
        def params = [:]
        params << ['offset': offset]
        params << ['max': max]
        def rates = Rate.list(params)

        then: "The list of rates returned"
        rates.size() == expextedListSize

        where:
        max | offset | expextedListSize
        2   | 0      | 2
        4   | 1      | 4
        4   | 7      | 3
    }

    void "Create rate"() {
        given: "a new rate"
        def NGN = new Rate(currency: Currency.getInstance('NGN'), value: 123.5)

        when: "the rate is saved"
        NGN.save()

        then: "it saved successful and can be found in the db"
        NGN.errors.errorCount == 0
        Rate.get(NGN.currency).value == NGN.value
    }
}
