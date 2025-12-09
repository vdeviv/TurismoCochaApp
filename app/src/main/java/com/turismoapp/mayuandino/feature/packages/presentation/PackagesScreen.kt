package com.turismoapp.mayuandino.feature.packages.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun PackagesScreen(
    viewModel: PackagesViewModel = koinViewModel(),
    onPackageClick: (String) -> Unit   // <--- NECESARIO
) {
    val state = viewModel.state.collectAsState().value

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text("Paquetes", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> CircularProgressIndicator()

            state.error != null ->
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)

            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.packages) { pkg ->
                    PackageCard(
                        pkg = pkg,
                        onClick = { onPackageClick(pkg.id) }   // <--- AQUÍ NAVEGAMOS
                    )
                }
            }
        }
    }
}
