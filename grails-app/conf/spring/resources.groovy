import com.currency.converter.CurrencyConverter

// Place your Spring DSL code here
beans = {

    currencyConverter(CurrencyConverter)

    xmlns task: "http://www.springframework.org/schema/task"

    task.'scheduled-tasks' {
        task.scheduled(ref: 'rateService', method: 'loadRateFromXml', cron: '* 1 * * * *')
    }
}
