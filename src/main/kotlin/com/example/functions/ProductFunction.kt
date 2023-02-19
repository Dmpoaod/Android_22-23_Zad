package com.example.functions

import com.example.models.Product
import com.example.models.Products
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import isValid
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonNull.content
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

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
    val products = mutableListOf<Product>()
    runBlocking {
        val httpClient = HttpClient {
            install(ContentNegotiation) {
               gson()
            }
        }
        val response: HttpResponse = httpClient.get {
            url("https://example.com/products")
            contentType(ContentType.Application.Json)
        }
        val responseBody: String = response.bodyAsText()
        val productsResponse = Gson().fromJson(responseBody, Array<Product>::class.java)
        products.addAll(productsResponse)
    }
    for (i in products) {
        addProduct(i)
    }
}

