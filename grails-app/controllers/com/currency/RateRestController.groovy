package com.currency

import grails.converters.JSON

class RateRestController {

    def rateService

    static responseFormats = ["json"]

    def index() {
        respond Rate.list()
    }

    def getRate(String currency) {
        Rate rate = rateService.getRate(currency)
        def rateResponse = new RateResponse()
        rateResponse.bindData(rate)

        respond rateResponse
    }

    def getCrossRate(String currentCurrency, String targetCurrency) {
        BigDecimal exchangeRate = rateService.getCrossRateValue(currentCurrency, targetCurrency)
        respond(new CrossCurrencyRate(
                currentCurrency: currentCurrency,
                targetCurrency: targetCurrency,
                rateValue: exchangeRate))
    }

    def handleIllegalArgumentException(IllegalArgumentException ex){
        respond (['error':'invalid currency or currency value']) as JSON
    }
}

class RateResponse {
    String currency
    String value

    def bindData(Rate rate) {
        currency = rate.currency.toString()
        value = rate.value.toString()
    }
}

class CrossCurrencyRate {
    String currentCurrency
    String targetCurrency
    BigDecimal rateValue
}