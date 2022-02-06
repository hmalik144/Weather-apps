package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.appttude.h_mal.atlas_weather.data.location.LocationProviderImpl
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.CURRENT_LOCATION
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class WorldViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    lateinit var viewModel: WorldViewModel

    @MockK(relaxed = true)
    lateinit var repository: Repository

    @MockK
    lateinit var locationProvider: LocationProviderImpl


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = WorldViewModel(locationProvider, repository)

    }

    @Test
    fun fetchDataForSingleLocation_invalidLocation_invalidReturn() {
        val location = CURRENT_LOCATION

        viewModel.fetchDataForSingleLocation(location)

        assertEquals(viewModel.operationRefresh.getOrAwaitValue()?.getContentIfNotHandled(), false)
    }
}


fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}