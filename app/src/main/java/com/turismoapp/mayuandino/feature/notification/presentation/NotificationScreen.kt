package com.turismoapp.mayuandino.feature.notification.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.turismoapp.mayuandino.feature.notification.data.model.LocalNotification
import com.turismoapp.mayuandino.ui.theme.RedMayu
import com.turismoapp.mayuandino.ui.theme.PurpleMayu
import com.turismoapp.mayuandino.ui.theme.TextBlack
import com.turismoapp.mayuandino.ui.theme.GrayText
import com.turismoapp.mayuandino.ui.theme.WhiteBackground

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel, // El ViewModel se pasa desde el NavHost
    onBackClick: () -> Unit
) {
    val notificationList by viewModel.notifications.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteBackground)
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = TextBlack,
                modifier = Modifier.clickable { onBackClick() }
            )
            Text("Notificaciones", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // --- BOTÓN ELIMINAR HISTORIAL ---
            Text(
                "Borrar todo",
                color = RedMayu,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    viewModel.clearAllNotifications()
                    Toast.makeText(context, "Historial borrado", Toast.LENGTH_SHORT).show()
                }
            )
        }

        HorizontalDivider(color = GrayText.copy(alpha = 0.3f), thickness = 1.dp)

        // --- LISTA DE NOTIFICACIONES ---
        if (notificationList.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay notificaciones recientes.", color = GrayText) // ⬅️ Deberías ver este texto si la lista está vacía
            }
        } else {
            // ⬅️ Si la lista no está vacía, se ejecuta este bloque
            LazyColumn {
                item { Text("Reciente", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.SemiBold) }
                items(notificationList) { noti ->
                    NotificationItem(noti = noti) // ⬅️ Y DEBE LLAMAR A CADA ITEM
                }
            }
        }
    }
}

@Composable
fun NotificationItem(noti: LocalNotification) {
    // Simulación del diseño con avatares (puedes reemplazar el icono por tu avatar real)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .background(PurpleMayu.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar/Icono de Notificación
        Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = PurpleMayu,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PurpleMayu.copy(alpha = 0.2f))
                .padding(8.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(noti.title, fontWeight = FontWeight.SemiBold, color = TextBlack)
            Text(
                noti.body,
                color = GrayText,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Simular la hora de la notificación (formato de ejemplo)
        Text(
            text = "Ahora", // Deberías formatear noti.timestamp aquí
            color = GrayText,
            fontSize = 12.sp
        )
    }
}