package com.turismoapp.mayuandino.feature.profile.domain.model

data class ProfileModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val cellphone: String = "",
    val summary: String = "",
    val pathUrl: String = "", // URL del avatar en Firebase Storage
) {
    // Función de utilidad para mapear a Firestore
    fun toMap(): Map<String, Any> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "cellphone" to cellphone,
            "summary" to summary,
            "pathUrl" to pathUrl,
        )
    }
}
