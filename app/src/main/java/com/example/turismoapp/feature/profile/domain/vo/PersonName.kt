package com.example.turismoapp.feature.profile.domain.vo


@JvmInline
value class PersonName(val value: String) {
    init {
        require(value.trim().length > 4) { "Nombre muy corto" }
        require(Regex(".*\\p{L}+.*").containsMatchIn(value)) { "El nombre debe contener letras" }
    }
    override fun toString() = value
    companion object { fun of(raw: String) = PersonName(raw.trim()) }
}