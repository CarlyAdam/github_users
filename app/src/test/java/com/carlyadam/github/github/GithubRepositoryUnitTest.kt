package com.carlyadam.github.github

import androidx.paging.PagingData
import com.carlyadam.github.data.model.User
import com.carlyadam.github.repository.GithubRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GithubRepositoryUnitTest {

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `testing github repository`() = runBlocking {

        // given
        val githubRepository = mockk<GithubRepository>()
        val fakeResponse = flowOf<PagingData<User>>()

        // when
        coEvery { githubRepository.users("test") } returns fakeResponse
        val result = githubRepository.users("test")

        // then
        assertEquals(fakeResponse, result)
    }
}
