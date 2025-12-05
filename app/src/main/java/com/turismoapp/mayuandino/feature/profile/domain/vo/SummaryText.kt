package com.turismoapp.mayuandino.feature.profile.domain.vo

@JvmInline
value class SummaryText(val value: String) {
    init {
        require(value.trim().length > 4) { "Resumen muy corto" }
        require(Regex(".*\\p{L}+.*").containsMatchIn(value)) { "El resumen debe contener letras" }
    }
    override fun toString() = value
    companion object { fun of(raw: String) = SummaryText(raw.trim()) }
}
