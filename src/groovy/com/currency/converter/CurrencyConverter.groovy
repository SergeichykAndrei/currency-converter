package com.currency.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CurrencyConverter implements AttributeConverter<Currency, String> {

    @Override
    String convertToDatabaseColumn(Currency currency) {
        currency ? null : currency.getCurrencyCode()
    }

    @Override
    Currency convertToEntityAttribute(String dbCurrency) {
        dbCurrency ? null : Currency.getInstance(dbCurrency)
    }
}
