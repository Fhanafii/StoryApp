package com.fhanafi.storyapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.fhanafi.storyapp.data.StoryRepository
import com.fhanafi.storyapp.data.UserRepository
import com.fhanafi.storyapp.data.remote.response.ListStoryItem
import com.fhanafi.storyapp.ui.adapters.StoryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var homeViewModel: HomeViewModel

    @Test
    fun `when Get Stories Should Not Be Null and Return Data`() = runTest {
        // Arrange
        val dummyStories = DataDummy.generateDummyStories()
        val expectedPagingData = PagingData.from(dummyStories)
        Mockito.`when`(storyRepository.getPagedStories()).thenReturn(flowOf(expectedPagingData))

        homeViewModel = HomeViewModel(userRepository, storyRepository)

        // Act & Assert
        homeViewModel.stories.test {
            val result = awaitItem()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = Dispatchers.Main
            )
            differ.submitData(result)

            // Verify data is not null and matches expectations
            assertNotNull(differ.snapshot())
            assertEquals(dummyStories.size, differ.snapshot().size)
            assertEquals(dummyStories[0], differ.snapshot()[0])

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when No Stories Should Return Empty Data`() = runTest {
        // Arrange
        val expectedPagingData = PagingData.empty<ListStoryItem>()
        Mockito.`when`(storyRepository.getPagedStories()).thenReturn(flowOf(expectedPagingData))

        homeViewModel = HomeViewModel(userRepository, storyRepository)

        // Act & Assert
        homeViewModel.stories.test {
            val result = awaitItem()

            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = Dispatchers.Main
            )
            differ.submitData(result)

            // Verify data is empty
            assertEquals(0, differ.snapshot().size)

            cancelAndIgnoreRemainingEvents()
        }
    }
}

// Utility class to generate dummy data for testing
object DataDummy {
    fun generateDummyStories(): List<ListStoryItem> {
        val stories = mutableListOf<ListStoryItem>()
        for (i in 1..10) {
            val story = ListStoryItem(
                id = i.toString(),
                name = "Story $i",
                description = "Description for story $i",
                photoUrl = "https://example.com/story$i.jpg",
                createdAt = "2022-01-01T00:00:00Z",
                lat = -6.2,
                lon = 106.8
            )
            stories.add(story)
        }
        return stories
    }
}

// No-op ListUpdateCallback for AsyncPagingDataDiffer
val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

// MainDispatcherRule to set the main dispatcher for coroutine tests
@ExperimentalCoroutinesApi
class MainDispatcherRule : TestWatcher() {
    private val testDispatcher = UnconfinedTestDispatcher()

    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
