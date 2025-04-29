package com.pankajkumarrout.bdmxa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pankajkumarrout.bdmxa.ui.home.MyApp
import com.pankajkumarrout.bdmxa.ui.login.LoginViewModel
import com.pankajkumarrout.bdmxa.ui.order.OrderViewModel
import com.pankajkumarrout.bdmxa.ui.theme.BdmxaTheme
import com.pankajkumarrout.bdmxa.ui.trip.TripsViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModel = module {
    single { LoginViewModel() }
    single { OrderViewModel() }
    single { TripsViewModel() }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(appModel)
        }

        enableEdgeToEdge()
        setContent {
            BdmxaTheme {
                MyApp()
            }
        }
    }
}
