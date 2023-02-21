package com.example.functions

import com.example.models.Product
import com.example.models.Products
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import isValid
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import io.ktor.serialization.gson.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.server.plugins.contentnegotiation.*


fun getProduct(id: Int): Product? {
    val product = transaction {
        Products.select { Products.id eq id }.map { Products.toProduct(it) }
    }
    return if (product.isEmpty()) null else product[0]
}

fun getAllProducts(): List<Product> {
    val products = transaction {
        Products.selectAll().map { Products.toProduct(it) }
    }
    return products
}

fun addProduct(product: Product) {
    if (!product.isValid()) return
    transaction {
        Products.insert {
            it[Products.name] = product.name
            it[Products.qty] = product.qty
            it[Products.price] = product.price
        }
    }
}

fun changeProduct(product: Product, id: Int) {
    transaction {
        Products.update({ Products.id eq id }) {
            it[Products.name] = product.name
            it[Products.qty] = product.qty
            it[Products.price] = product.price
        }
    }
}

fun deleteProduct(id: Int) {
    transaction {
        Products.deleteWhere { Products.id eq id }
    }
}

//Zad 7
suspend fun downloadProduct() {

    val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    val products: List<Product> = client.get("http://localhost:8080/products")

            for (i in products) {
                addProduct(i)
            }

            client.close()




}

