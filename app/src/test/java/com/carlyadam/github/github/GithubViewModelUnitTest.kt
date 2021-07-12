package com.carlyadam.github.github

import androidx.paging.PagingData
import com.carlyadam.github.data.api.model.User
import com.carlyadam.github.ui.github.GithubViewModel
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
class GithubViewModelUnitTest {

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `testing giphy viewModel`() = runBlocking {

        // given
        val githubViewModel = mockk<GithubViewModel>()
        val fakeResponse = flowOf<PagingData<User>>()

        // when
        coEvery { githubViewModel.users("test") } returns fakeResponse
        val result = githubViewModel.users("test")

        // then
        assertEquals(fakeResponse, result)
    }
}
