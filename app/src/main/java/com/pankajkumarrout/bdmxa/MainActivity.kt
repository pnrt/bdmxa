package com.pankajkumarrout.bdmxa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pankajkumarrout.bdmxa.ui.home.MyApp
import com.pankajkumarrout.bdmxa.ui.login.LoginScreen
import com.pankajkumarrout.bdmxa.ui.login.LoginViewModel
import com.pankajkumarrout.bdmxa.ui.order.OrderViewModel
import com.pankajkumarrout.bdmxa.ui.theme.BdmxaTheme
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModel = module {
    single { LoginViewModel() }
    single { OrderViewModel() }
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
