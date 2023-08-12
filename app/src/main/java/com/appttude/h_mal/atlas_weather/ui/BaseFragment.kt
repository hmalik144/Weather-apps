package com.appttude.h_mal.atlas_weather.ui

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.appttude.h_mal.atlas_weather.application.LOCATION_PERMISSION_REQUEST
import com.appttude.h_mal.atlas_weather.base.BaseActivity
import com.appttude.h_mal.atlas_weather.helper.GenericsHelper.getGenericClassAt
import com.appttude.h_mal.atlas_weather.model.ViewState
import com.appttude.h_mal.atlas_weather.utils.Event
import com.appttude.h_mal.atlas_weather.utils.displayToast
import com.appttude.h_mal.atlas_weather.viewmodel.ApplicationViewModelFactory
import com.appttude.h_mal.atlas_weather.viewmodel.baseViewModels.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import kotlin.properties.Delegates

@Suppress("EmptyMethod", "EmptyMethod")
abstract class BaseFragment<V : BaseViewModel>(@LayoutRes contentLayoutId: Int) :
    Fragment(contentLayoutId),
    KodeinAware {

    override val kodein by kodein()
    val factory by instance<ApplicationViewModelFactory>()

    val viewModel: V by getFragmentViewModel()

    var mActivity: BaseActivity? = null
    private fun getFragmentViewModel(): Lazy<V> =
        createViewModelLazy(getGenericClassAt(0), { viewModelStore }, factoryProducer = { factory })

    private var shortAnimationDuration by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mActivity = activity as BaseActivity
        configureObserver()
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

    /**
     * Request a permission for
     * @param permission with
     * @param permissionCode
     * Callback if is already permission granted
     * @param permissionGranted
     */
    fun getPermissionResult(
        permission: String,
        permissionCode: Int,
        permissionGranted: () -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(permission), permissionCode)
            return
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                permissionGranted.invoke()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                permissionsGranted()
                displayToast("Permission granted")
            } else {
                permissionsRefused()
                displayToast("Permission denied")
            }
        }
    }

    open fun permissionsGranted() {}
    open fun permissionsRefused() {}
}