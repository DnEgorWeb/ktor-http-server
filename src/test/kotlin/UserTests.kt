import com.jetbrains.handson.httpapi.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class UserRouteTests {
    @Test
    fun testGetUser() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/user/100").apply {
                assertEquals(
                    "No user with id 100",
                    response.content
                )
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
        }
    }
}