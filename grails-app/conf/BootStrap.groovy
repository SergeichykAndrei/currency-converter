import com.currency.Rate
import com.currency.Role
import com.currency.User
import com.currency.UserRole

import java.math.RoundingMode

class BootStrap {

    def init = { servletContext ->
        environments {
            development {
                if (!User.count()) createSampleData()
            }
            test {
                if (!User.count()) createSampleData()

                createRates()
            }
        }
    }

    private void createRates() {
        def EUR = newRate('EUR', 1.0)
        def USD = newRate('USD', 1.09)
        def PLN = newRate('PLN', 4.39)
        def TRY = newRate('TRY', 6.19)
        def BGN = newRate('JPY', 1.96)
        def CZK = newRate('CZK', 25.84)
        def DKK = newRate('DKK', 7.47)
        def GBP = newRate('GBP', 0.89)
        def HUF = newRate('HUF', 335.54)
        def RON = newRate('RON', 4.75)

        Rate.saveAll([EUR, USD, PLN,
                      TRY, BGN, RON,
                      CZK, DKK, GBP,
                      HUF])
    }

    def destroy = {
    }

    private createSampleData() {
        println "Create sample data"

        def roleAdmin = new Role('ROLE_ADMIN')
        def roleUser = new Role('ROLE_USER')
        roleAdmin.save()
        roleUser.save()

        def admin = new User(
                username: "admin",
                password: "admin123",
        )
        admin.save()

        new UserRole(user: admin, role: roleAdmin).save()

        def user = new User(
                username: "user",
                password: "user1234",
        )
        user.save()

        new UserRole(user: user, role: roleUser).save()
    }

    Rate newRate(String currency, BigDecimal value) {
        new Rate(
                currency: Currency.getInstance(currency),
                value: new BigDecimal(value).setScale(4, RoundingMode.DOWN))
    }
}
