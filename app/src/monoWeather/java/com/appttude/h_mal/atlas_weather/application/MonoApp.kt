import com.appttude.h_mal.atlas_weather.application.AppClass
import com.appttude.h_mal.atlas_weather.application.ApplicationViewModelFactory
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

open class MonoApp : AppClass() {

    override val flavourModule = super.flavourModule.copy {
        bind() from provider {
            ApplicationViewModelFactory(
                this@MonoApp,
                instance(),
                instance(),
                instance(),
            )
        }
    }
}