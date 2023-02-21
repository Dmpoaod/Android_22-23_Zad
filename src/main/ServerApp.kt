package main


import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


data class Product(
    val id: Int? = null,
    val name: String,
    val qty: Int,
    val price: Double,
    val type: String
)


val productList = listOf(Product(1, "Product 1", 10, 15.0, "Type 1"), Product(2, "Product 2", 20, 25.0, "Type 2"))

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            gson()
        }
        routing {
            get("/products") {
                call.respond(HttpStatusCode.OK, productList)
            }
        }
    }.start(wait = true)
}

