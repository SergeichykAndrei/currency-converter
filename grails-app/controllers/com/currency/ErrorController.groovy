package com.currency

class ErrorController {

    def internalServer() {
        def ex = request.exception.cause ?: request.exception

        respond new ErrorDetails(type: ex.class.name, message: ex.message), view: "/error"
    }

    def notFound() {
        respond new ErrorDetails(type: "", message: "Page not found"), view: "/error"
    }
}

class ErrorDetails {
    String type
    String message
}