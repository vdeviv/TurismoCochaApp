package com.example.turismoapp.feature.profile.data.repository

import com.example.turismoapp.feature.profile.domain.model.ProfileModel
import com.example.turismoapp.feature.profile.domain.repository.IProfileRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val firestore: FirebaseFirestore
) : IProfileRepository {

    private val profilesCollection = firestore.collection("profiles")

    override suspend fun fetchProfile(uid: String): Result<ProfileModel> {
        return try {
            val document = profilesCollection.document(uid).get().await()

            if (document.exists()) {
                val profile = ProfileModel(
                    uid = document.getString("uid") ?: uid,
                    pathUrl = document.getString("pathUrl") ?: "",
                    name = document.getString("name") ?: "",
                    email = document.getString("email") ?: "",
                    cellphone = document.getString("cellphone") ?: "",
                    summary = document.getString("summary") ?: ""
                )
                Result.success(profile)
            } else {
                Result.failure(Exception("Perfil no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener perfil: ${e.message}"))
        }
    }

    override suspend fun saveProfile(profile: ProfileModel): Result<Unit> {
        return try {
            val profileMap = hashMapOf(
                "uid" to profile.uid,
                "pathUrl" to profile.pathUrl,
                "name" to profile.name,
                "email" to profile.email,
                "cellphone" to profile.cellphone,
                "summary" to profile.summary
            )

            profilesCollection.document(profile.uid).set(profileMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al guardar perfil: ${e.message}"))
        }
    }

    override suspend fun updateProfile(profile: ProfileModel): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any>(
                "pathUrl" to profile.pathUrl,
                "name" to profile.name,
                "email" to profile.email,
                "cellphone" to profile.cellphone,
                "summary" to profile.summary
            )

            profilesCollection.document(profile.uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar perfil: ${e.message}"))
        }
    }

    override suspend fun deleteProfile(uid: String): Result<Unit> {
        return try {
            profilesCollection.document(uid).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al eliminar perfil: ${e.message}"))
        }
    }
}