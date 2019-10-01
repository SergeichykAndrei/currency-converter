package com.currency

class RateController {

    static scaffold = true

    def rateService

    def rates() {
        if (!params.getProperty('offset')) params << ['offset': 0]
        if (!params.getProperty('max')) params << ['max': 5]

        [rates: Rate.list(params), rateCount: Rate.count()]
    }

    def create() {
    }

    def newRate(RateCommand rateCommand) {
        if (rateCommand.hasErrors()) {
            render view: "create", model: [rate: rateCommand]
        } else {
            Rate rate = new Rate()
            try {
                rate.bindData(rateCommand)
            } catch (IllegalArgumentException ex) {
                flash.message = "Invalid currency or currency value"
            }
            if (rate.validate() && rateService.createRate(rate)) {
                redirect(action: 'rates')
            } else {
                render view: "create", model: [rate: rateCommand]
            }
        }
    }
}

class RateCommand {
    String currency
    String value

    static constraints = {
        currency size: 3..3, blank: false
        value size: 1..6, blank: false
    }
}