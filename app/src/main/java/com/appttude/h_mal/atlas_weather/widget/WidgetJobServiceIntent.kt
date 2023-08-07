package com.appttude.h_mal.atlas_weather.widget

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.icu.text.SimpleDateFormat
import android.os.PowerManager
import android.widget.RemoteViews
import android.os.Build
import androidx.core.app.ActivityCompat.checkSelfPermission
import com.appttude.h_mal.atlas_weather.R
import com.appttude.h_mal.atlas_weather.widget.WidgetState.*
import com.appttude.h_mal.atlas_weather.widget.WidgetState.Companion.getWidgetState
import com.appttude.h_mal.atlas_weather.helper.ServicesHelper
import com.appttude.h_mal.atlas_weather.model.widget.InnerWidgetCellData
import com.appttude.h_mal.atlas_weather.model.widget.WidgetWeatherCollection
import com.appttude.h_mal.atlas_weather.ui.MainActivity
import com.appttude.h_mal.atlas_weather.utils.isInternetAvailable
import com.appttude.h_mal.atlas_weather.utils.tryOrNullSuspended
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import java.util.*


/**
 * Implementation of a JobIntentService used for home screen widget
 */
const val HALF_DAY = 43200000L
class WidgetJobServiceIntent : BaseWidgetServiceIntentClass<NewAppWidget>() {

    private val kodein = LateInitKodein()
    private val helper: ServicesHelper by kodein.instance()

    override fun onHandleWork(intent: Intent) {
        // We have received work to do.  The system or framework is already
        // holding a wake lock for us at this point, so we can just go.
        kodein.baseKodein = (applicationContext as KodeinAware).kodein
        executeWidgetUpdate()
    }

    private fun executeWidgetUpdate() {
        val componentName = ComponentName(this, NewAppWidget::class.java)
        initBaseWidget(componentName)

        initiateWidgetUpdate(getCurrentWidgetState())
    }

    private fun initiateWidgetUpdate(state: WidgetState) {
        when (state) {
            NO_LOCATION, SCREEN_ON_CONNECTION_UNAVAILABLE -> updateErrorWidget(state)
            SCREEN_ON_CONNECTION_AVAILABLE -> updateWidget(false)
            SCREEN_OFF_CONNECTION_AVAILABLE -> updateWidget(true)
            SCREEN_OFF_CONNECTION_UNAVAILABLE -> return
        }
    }

    private fun updateWidget(fromStorage: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = getWidgetWeather(fromStorage)
            appWidgetIds.forEach { id -> setupView(id, result) }
        }
    }

    private fun updateErrorWidget(state: WidgetState) {
        appWidgetIds.forEach { id -> setEmptyView(id, state) }
    }

    private fun getCurrentWidgetState(): WidgetState {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isInteractive
        val locationGranted =
                checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
        val internetAvailable = isInternetAvailable(this.applicationContext)

        return getWidgetState(locationGranted, isScreenOn, internetAvailable)
    }

    @SuppressLint("MissingPermission")
    suspend fun getWidgetWeather(storageOnly: Boolean): WidgetWeatherCollection? {
        return tryOrNullSuspended {
            if (!storageOnly) helper.fetchData()
            helper.getWidgetWeatherCollection()
        }
    }

    private fun setEmptyView(appWidgetId: Int, state: WidgetState) {
        val error = when (state) {
            NO_LOCATION -> "No Location Permission"
            SCREEN_ON_CONNECTION_UNAVAILABLE -> "No network available"
            else -> "No data"
        }

        val views = createRemoteView(R.layout.weather_app_widget)
        bindErrorView(appWidgetId, views, error)
    }

    private fun setupView(
            appWidgetId: Int,
            collection: WidgetWeatherCollection?
    ) {
        val views = createRemoteView(R.layout.weather_app_widget)
        setLastUpdated(views, collection?.widgetData?.timeStamp)
        views.setInt(R.id.whole_widget_view, "setBackgroundColor", helper.getWidgetBackground())

        if (collection != null) {
            bindView(appWidgetId, views, collection)
        } else {
            bindEmptyView(appWidgetId, views, "No weather available")
        }
    }

    override fun bindErrorView(
            widgetId: Int,
            views: RemoteViews,
            data: Any?
    ) {
        bindEmptyView(widgetId, views, data)
    }

    override fun bindEmptyView(
            widgetId: Int,
            views: RemoteViews,
            data: Any?
    ) {
        val clickUpdate = createUpdatePendingIntent(NewAppWidget::class.java, widgetId)

        views.apply {
            setTextViewText(R.id.widget_current_location, data as String)
            setImageViewResource(R.id.widget_current_icon, R.drawable.ic_baseline_cloud_off_24)
            setImageViewResource(R.id.location_icon, 0)

            setTextViewText(R.id.widget_main_temp, "")
            setTextViewText(R.id.widget_feel_temp, "")

            setOnClickPendingIntent(R.id.widget_current_icon, clickUpdate)
            setOnClickPendingIntent(R.id.widget_current_location, clickUpdate)
            appWidgetManager.updateAppWidget(widgetId, this)
        }
    }

    override fun bindView(widgetId: Int, views: RemoteViews, data: Any?) {
        val clickUpdate = createUpdatePendingIntent(NewAppWidget::class.java, widgetId)
        val clickToMain = createClickingPendingIntent(MainActivity::class.java)

        val collection = data as WidgetWeatherCollection
        val weather = collection.widgetData
        views.apply {
            setTextViewText(R.id.widget_main_temp, weather.currentTemp)
            setTextViewText(R.id.widget_feel_temp, "°C")
            setTextViewText(R.id.widget_current_location, weather.location)
            setImageViewResource(R.id.location_icon, R.drawable.location_flag)
            setImageView(weather.icon, this, R.id.widget_current_icon, widgetId)
            setOnClickPendingIntent(R.id.widget_current_icon, clickUpdate)
            setOnClickPendingIntent(R.id.widget_current_location, clickUpdate)

            loadCells(widgetId, this, collection.forecast, clickToMain)
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun loadCells(
        appWidgetId: Int,
        remoteViews: RemoteViews,
        weather: List<InnerWidgetCellData>,
        clickIntent: PendingIntent
    ) {
        (0..4).forEach { i ->
            val containerId: Int = resources.getIdentifier("widget_item_$i", "id", packageName)
            val dayId: Int = resources.getIdentifier("widget_item_day_$i", "id", packageName)
            val imageId: Int = resources.getIdentifier("widget_item_image_$i", "id", packageName)
            val tempId: Int = resources.getIdentifier("widget_item_temp_high_$i", "id", packageName)

            val it = weather[i]

            remoteViews.setTextViewText(dayId, it.date)
            remoteViews.setTextViewText(tempId, it.highTemp)
            setImageView(it.icon, remoteViews, imageId, appWidgetId)
            remoteViews.setOnClickPendingIntent(containerId, clickIntent)
        }
    }

    private fun setLastUpdated(views: RemoteViews, timeStamp: Long?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && timeStamp != null) {
            val difference = System.currentTimeMillis().minus(timeStamp)

            val status = if (difference > HALF_DAY) {
                "12hrs ago"
            } else {
                val date = Date(timeStamp)
                val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
                sdf.format(date)
            }

            views.setTextViewText(R.id.widget_current_status, "last updated: $status")
        }
    }

    companion object {
        /**
         * Unique job ID for this service.
         */
        private const val JOB_ID = 1000

        /**
         * Convenience method for enqueuing work in to this service.
         */
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, WidgetJobServiceIntent::class.java, JOB_ID, work)
        }
    }
}