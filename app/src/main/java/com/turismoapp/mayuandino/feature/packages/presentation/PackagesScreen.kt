package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.turismoapp.mayuandino.ui.theme.WhiteBackground
import com.turismoapp.mayuandino.ui.theme.TextBlack
import org.koin.androidx.compose.koinViewModel

@Composable
fun PackagesScreen(
    viewModel: PackagesViewModel = koinViewModel(),
    onPackageClick: (String) -> Unit
) {
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Todos los paquetes populares",
            style = MaterialTheme.typography.headlineSmall.copy(color = TextBlack)
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading ->
                CircularProgressIndicator()

            state.error != null ->
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error
                )

            else ->
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    items(state.packages) { pkg ->
                        PackageCard(
                            pkg = pkg,
                            onClick = { onPackageClick(pkg.id) }
                        )
                    }
                }
        }
    }
}
