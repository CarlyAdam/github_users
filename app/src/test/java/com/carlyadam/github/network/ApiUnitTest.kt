package com.carlyadam.github.network

import com.carlyadam.github.utils.MockResponseFileReader
import com.carlyadam.github.data.api.ApiService
import com.carlyadam.github.data.api.ApiService.Companion.API_KEY
import com.carlyadam.github.data.responses.GithubResponse
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class ApiUnitTest {

    private var mockWebServer = MockWebServer()
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Test
    fun `testing retrofit service`() = runBlocking {

        // given
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("test.json").content)
        mockWebServer.enqueue(response)

        var userList =
            Gson().fromJson(response.getBody()!!.readUtf8(), GithubResponse::class.java)

        // when
        val listRepo = apiService.users(0, 25, "carlyadam", API_KEY)

        // then
        assertEquals(
            listRepo.body()!!,
            userList
        )
        assertEquals(
            listRepo.body()!!.items[0],
            userList.items[0]
        )
        assertEquals(
            userList.total_count,
            4

        )
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }
}
