package com.appttude.h_mal.atlas_weather.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.appttude.h_mal.atlas_weather.data.location.LocationProvider
import com.appttude.h_mal.atlas_weather.data.network.response.forecast.WeatherResponse
import com.appttude.h_mal.atlas_weather.data.repository.Repository
import com.appttude.h_mal.atlas_weather.data.room.entity.EntityItem
import com.appttude.h_mal.atlas_weather.utils.Event
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class MainViewModelTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var mainViewModel: MainViewModel

    @MockK(relaxed = true)
    lateinit var repository: Repository

    @MockK
    lateinit var locationProvider: LocationProvider

    @RelaxedMockK
    lateinit var mockedLiveData: LiveData<EntityItem>


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mainViewModel = MainViewModel(locationProvider, repository)
        every { repository.loadCurrentWeatherFromRoom("CurrentLocation") } answers { mockedLiveData }
    }

    @Test
    fun fetchData_workingLocation_successfulOperation() = runBlocking{
        val observer = mockk<Observer<Event<Boolean>>>(relaxed = true)
        val mockResponse = mockk<WeatherResponse>()
        mainViewModel.operationState.observeForever(observer)

        // Act
        every { observer.onChanged(Event(false)) }
        every { locationProvider.getLatLong() } returns Pair(0.00, 0.00)
        coEvery { repository.getWeatherFromApi("0.00", "0.00") } returns mockResponse


        // Assert
        mainViewModel.fetchData()
        delay(50)
        assertEquals(mainViewModel.operationState.value?.getContentIfNotHandled(), false)
    }
}