package com.appttude.h_mal.atlas_weather.base.baseViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appttude.h_mal.atlas_weather.model.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.util.concurrent.CancellationException

open class BaseViewModel : ViewModel() {

    private val _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState> = _uiState


    fun onStart() {
        _uiState.postValue(ViewState.HasStarted)
    }

    fun <T : Any> onSuccess(result: T) {
        _uiState.postValue(ViewState.HasData(result))
    }

    protected fun <E : Any> onError(error: E) {
        _uiState.postValue(ViewState.HasError(error))
    }

    protected var job: Job? = null

    fun cancelOperation() {
        CoroutineScope(Dispatchers.IO).launch {
            job?.run {
                cancelAndJoin()
                onSuccess(Unit)
            }
        }
    }
}