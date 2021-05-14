package fr.nicopico.pokedex.feature.pokemon.list.api.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import java.nio.charset.StandardCharsets

open class BaseApiClientTest {

    private lateinit var mockWebServer: MockWebServer

    val endpointUrl: String
        get() = mockWebServer.url("/").toString()

    @Before
    fun mockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream("api-response/$fileName")
        val source = inputStream!!.source().buffer()
        val mockResponse = MockResponse()
        headers.forEach { (key, value) -> mockResponse.addHeader(key, value) }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)))
    }

    fun waitForRequest() {
        mockWebServer.takeRequest()
    }
}
