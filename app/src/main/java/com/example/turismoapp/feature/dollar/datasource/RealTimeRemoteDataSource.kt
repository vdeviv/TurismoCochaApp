package com.example.turismoapp.feature.dollar.datasource

import com.example.turismoapp.feature.dollar.domain.model.DollarModel
import kotlinx.coroutines.flow.Flow
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class RealTimeRemoteDataSource {

    suspend fun getDollarUpdates(): Flow<DollarModel> = callbackFlow {
        val callback = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                close(p0.toException()) // Aquí cierras el flujo si hay error
            }

            override fun onDataChange(p0: DataSnapshot) {
                val value = p0.getValue(DollarModel::class.java)
                value?.let {
                    trySend(it) // Enviar los datos si no es null
                }
            }
        }

        val database = Firebase.database
        val myRef = database.getReference("dollar")
        myRef.addValueEventListener(callback)

        // Se asegura de remover el listener al cerrar el flujo
        awaitClose {
            myRef.removeEventListener(callback)
        }
    }
}

