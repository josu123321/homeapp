package com.example.home

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.home.ui.theme.HomeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.viewinterop.AndroidView


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize()
                    ) {
                        HomeScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    // State variables for live data
    var temperature by remember { mutableStateOf("--") }
    var humidity by remember { mutableStateOf("--") }

    val coroutineScope = rememberCoroutineScope()

    // API call to fetch live data
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val api = RetrofitClient.getInstance().create(ThingSpeakAPI::class.java)
            val call = api.getLatestFeeds()
            call.enqueue(object : Callback<ThingSpeakResponse> {
                override fun onResponse(
                    call: Call<ThingSpeakResponse>,
                    response: Response<ThingSpeakResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val latestFeed = response.body()!!.feeds[0]
                        temperature = latestFeed.field1 ?: "--"
                        humidity = latestFeed.field2 ?: "--"
                    }
                }

                override fun onFailure(call: Call<ThingSpeakResponse>, t: Throwable) {
                    temperature = "Error"
                    humidity = "Error"
                }
            })
        }
    }

    // UI layout with live data and charts
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display temperature and humidity
        Text(
            text = "Temperature: $temperature °C",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Humidity: $humidity %",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Temperature chart
        Text(
            text = "Temperature Chart",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        WebViewComposable(url = "https://thingspeak.mathworks.com/channels/2388701/charts/1?bgcolor=%23ffffff&color=%23000000&dynamic=true&results=35&title=Temperature&type=line&xaxis=Time&yaxis=Temperature+%28+%C2%B0C+%29")

        Spacer(modifier = Modifier.height(16.dp))

        // Humidity chart
        Text(
            text = "Humidity Chart",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        WebViewComposable(url = "https://thingspeak.mathworks.com/channels/2388701/charts/2?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=30&title=Humidity&type=spline&xaxis=Time&yaxis=Humidity+%28+%25+%29")
    }
}

@Composable
fun WebViewComposable(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient() // Keeps navigation within the WebView
                settings.javaScriptEnabled = true // Enable JavaScript for charts
                loadUrl(url) // Load the Thingspeak chart URL
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // Adjust height to fit the design
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeTheme {
        HomeScreen()
    }
}
