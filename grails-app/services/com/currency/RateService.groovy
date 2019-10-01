package com.currency

import grails.transaction.Transactional
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

import javax.xml.parsers.SAXParserFactory
import java.math.RoundingMode

@Transactional
class RateService {

    Rate createRate(Rate rate) {
        rate.save()
    }

    Rate getRate(String currency) {
        Rate.get(Currency.getInstance(currency))
    }

    void loadRateFromXml() {
        def handler = new XmlHandler()
        def factory = SAXParserFactory.newInstance()
        def reader = factory.newSAXParser().XMLReader
        reader.contentHandler = handler
        try {
            new URL('https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml').withInputStream { is ->
                reader.parse(new InputSource(is))
            }
            handler.rates << new Rate(currency: Currency.getInstance('EUR'),
                    value: new BigDecimal(1).setScale(4, RoundingMode.DOWN))
            User.saveAll(handler.rates)
        } catch (IOException ex) {
            log.info('failed to load data from server', ex)
        }
    }

    def getCrossRateValue(String currentCurrency, String targetCurrency) {
        Rate currency1 = Rate.get(Currency.getInstance(currentCurrency))
        Rate currency2 = Rate.get(Currency.getInstance(targetCurrency))

        currency2.value.divide(currency1.value, RoundingMode.HALF_DOWN)
    }
}

class XmlHandler extends DefaultHandler {
    def rates = []

    @Override
    void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName != 'Cube') return
        def currency = attributes.getValue('currency')
        def rate = attributes.getValue('rate')?.toBigDecimal()
        if (currency && rate) {
            rates << new Rate(currency: Currency.getInstance(currency),
                    value: rate.setScale(4, RoundingMode.DOWN))
        }
    }
}