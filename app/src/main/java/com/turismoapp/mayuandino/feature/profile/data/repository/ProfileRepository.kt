package com.turismoapp.mayuandino.feature.profile.data.repository

import com.turismoapp.mayuandino.feature.profile.domain.model.ProfileModel
import com.turismoapp.mayuandino.feature.profile.domain.repository.IProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val firestore: FirebaseFirestore
) : IProfileRepository {

    private val profilesCollection = firestore.collection("profiles")

    // Obtener perfil (Lectura con mapeo manual robusto)
    override suspend fun fetchProfile(uid: String): Result<ProfileModel> = try {
        val documentSnapshot = profilesCollection.document(uid).get().await()
        if (documentSnapshot.exists()) {
            // Mapeo manual para evitar dependencias de toObject en data class
            val profile = ProfileModel(
                uid = documentSnapshot.getString("uid") ?: uid,
                email = documentSnapshot.getString("email") ?: "",
                name = documentSnapshot.getString("name") ?: "",
                cellphone = documentSnapshot.getString("cellphone") ?: "",
                summary = documentSnapshot.getString("summary") ?: "",
                pathUrl = documentSnapshot.getString("pathUrl") ?: ""
            )
            Result.success(profile)
        } else {
            Result.failure(NoSuchElementException("Perfil no encontrado para UID: $uid"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Guardar (Crear) Perfil
    override suspend fun saveProfile(profile: ProfileModel): Result<Unit> = try {
        // Escritura: Usar profile.uid para el documento y toMap() para los datos.
        profilesCollection.document(profile.uid).set(profile.toMap()).await()
        Result.success(Unit)
    } catch (e: Exception) {
        // Devolvemos el error de Firebase para que el ViewModel lo muestre (PERMISSION_DENIED)
        Result.failure(e)
    }

    // Actualizar Perfil
    override suspend fun updateProfile(profile: ProfileModel): Result<Unit> = try {
        // Escritura: Usar set() para sobrescribir/actualizar y toMap()
        profilesCollection.document(profile.uid).set(profile.toMap()).await()
        Result.success(Unit)
    } catch (e: Exception) {
        // Devolvemos el error de Firebase
        Result.failure(e)
    }

    // Eliminar Perfil
    override suspend fun deleteProfile(uid: String): Result<Unit> = try {
        profilesCollection.document(uid).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}