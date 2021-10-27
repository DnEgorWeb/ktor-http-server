package com.jetbrains.handson.httpapi

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.serialization.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import User
import registerUserRoutes

val client = KMongo.createClient("mongodb+srv://User:popmUq-dowxix-xohcu5@cluster0.z3z0x.mongodb.net/myFirstDatabase?retryWrites=true&w=majority").coroutine
val database = client.getDatabase("test")
val userCollection = database.getCollection<User>()

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        json()
    }

    registerUserRoutes()

    // convenient function, "standard" way is to use this form: install(Routing) { ... }
    routing {
        get("/") {
            call.respondText("Ogram rules the world!")
        }
    }
}
