package com.example.turismoapp.feature.profile.domain.vo

@JvmInline
value class PhoneNumber(val value: String) {
    init {
        val digits = value.filter { it.isDigit() }
        require(digits.length >= 5) { "Teléfono muy corto" }
    }
    override fun toString() = value
    companion object { fun of(raw: String) = PhoneNumber(raw.trim()) }
}
