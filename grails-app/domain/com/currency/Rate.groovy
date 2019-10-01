package com.currency

import com.currency.converter.CurrencyConverter
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import grails.validation.Validateable

import javax.persistence.Convert
import java.math.RoundingMode

@Validateable
@JsonIgnoreProperties(["class","id", "currency"])
class Rate {

    @Convert(converter = CurrencyConverter.class)
    Currency currency
    BigDecimal value

    static mapping = {
        id name: 'currency'
        id generator: 'assigned'
        version false
    }

    static constraints = {
        currency size: 3..3, unique: true, blank: false
        value size: 1..6, blank: false
    }

    static maping = {
        sort currency: "desc"
    }

    def bindData(RateCommand rateCommand) {
        currency = Currency.getInstance(rateCommand.currency)
        value = new BigDecimal(rateCommand.value).setScale(4, RoundingMode.DOWN)
    }
}
