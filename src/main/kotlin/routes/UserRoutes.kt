import com.jetbrains.handson.httpapi.userCollection
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.coroutines.*
import org.litote.kmongo.eq

fun Route.userRouting() {
    route("/user") {
        get {
            val deferredUsers: Deferred<List<User>> = async {
                userCollection.find().toList()
            }
            val users = deferredUsers.await()
            if (users.isNotEmpty()) {
                call.respond(users)
            } else {
                call.respondText("No users found", status = HttpStatusCode.NotFound)
            }
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val deferredUser: Deferred<User?> = async {
                userCollection.findOne(User::id eq id)
            }
            val user = deferredUser.await() ?: return@get call.respondText(
                "No user with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(user)
        }

        post {
            val user = call.receive<User>()
            val deferredInsert = async {
                userCollection.insertOne(user)
            }
            deferredInsert.await()
            call.respondText("User stored correctly", status = HttpStatusCode.Created)
        }

        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val defferedDeletion = async {
                userCollection.findOneAndDelete(User::id eq id)
            }
            val deletedUser = defferedDeletion.await() ?: call.respondText("Not Found", status = HttpStatusCode.NotFound)
            call.response.status(HttpStatusCode.Accepted)
            call.respond(deletedUser)
        }

    }
}

fun Application.registerUserRoutes() {
    routing {
        userRouting()
    }
}