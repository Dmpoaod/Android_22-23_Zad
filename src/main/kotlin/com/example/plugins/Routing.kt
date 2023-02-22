
package com.example.plugins

import com.example.functions.PaymentData
import com.example.functions.mockPayment
import com.example.models.Product
import com.example.models.User
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import main.productList


fun Application.configureRouting() {

    routing {
        get("/") {
            val x : List<Product> = listOf(
                Product(1, "Product 1", 5, 12.45, "test"),
                Product(2, "Product 2", 10, 32.56, "test 2")
            )
            var gson = Gson()
            call.respondText(gson.toJson(x))
            call.respond(HttpStatusCode.OK, "I'm working just fine, thanks!")
        }

            get("/products") {
                call.respond(HttpStatusCode.OK, productList)
            }


        get("/pay") {
            val paymentData : PaymentData = (PaymentData("1234567890625142", "03/24", "111", 1546.00))
            val isPaymentSuccessful = mockPayment(paymentData)
            if (isPaymentSuccessful) {
                call.respondText("Payment successful!")
            } else {
                call.respondText("Payment failed.")
            }
        }

            authenticate {
                get("/authenticated") {
                    call.respond(HttpStatusCode.OK, "My name is ${call.principal<User>()?.name}, and I'm authenticated!")
                }
            }

    }
}

