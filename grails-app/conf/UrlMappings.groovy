class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/rates" {
            controller = "rate"
            action = "rates"
        }

        "/api/v1/$currency" {
            controller = "rateRest"
            action = "getRate"
        }

        "/api/v1/$currentCurrency/$targetCurrency" {
            controller = "rateRest"
            action = "getCrossRate"
        }

        "/"(view: "/index")
        "500"(view: '/error')
    }
}
