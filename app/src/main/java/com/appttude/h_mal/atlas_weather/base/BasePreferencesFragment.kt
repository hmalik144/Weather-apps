package com.appttude.h_mal.atlas_weather.base

import android.os.Bundle
import android.view.View
import androidx.annotation.XmlRes
import androidx.fragment.app.createViewModelLazy
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.appttude.h_mal.atlas_weather.base.baseViewModels.BaseAndroidViewModel
import com.appttude.h_mal.atlas_weather.helper.GenericsHelper.getGenericClassAt
import com.appttude.h_mal.atlas_weather.model.ViewState
import com.appttude.h_mal.atlas_weather.application.ApplicationViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.properties.Delegates

@Suppress("EmptyMethod", "EmptyMethod")
abstract class BasePreferencesFragment<V : BaseAndroidViewModel>(@XmlRes private val preferencesResId: Int) :
    PreferenceFragmentCompat(),
    KodeinAware {

    override val kodein by kodein()
    private val factory by instance<ApplicationViewModelFactory>()

    val viewModel: V by getFragmentViewModel()

    var mActivity: BaseActivity? = null
    private fun getFragmentViewModel(): Lazy<V> =
        createViewModelLazy(getGenericClassAt(0), { viewModelStore }, factoryProducer = { factory })

    private var shortAnimationDuration by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as BaseActivity
        configureObserver()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(preferencesResId, rootKey)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        prefs.registerOnSharedPreferenceChangeListener { _, s ->
            s?.let { preferenceChanged(s) }
        }
    }

    private fun configureObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.HasStarted -> onStarted()
                is ViewState.HasData<*> -> onSuccess(it.data)
                is ViewState.HasError<*> -> onFailure(it.error)
            }
        }
    }

    open fun preferenceChanged(key: String) { }

    /**
     *  Called in case of starting operation liveData in viewModel
     */
    open fun onStarted() {
        mActivity?.onStarted()
    }

    /**
     *  Called in case of success or some data emitted from the liveData in viewModel
     */
    open fun onSuccess(data: Any?) {
        mActivity?.onSuccess(data)
    }

    /**
     *  Called in case of failure or some error emitted from the liveData in viewModel
     */
    open fun onFailure(error: Any?) {
        mActivity?.onFailure(error)
    }
}