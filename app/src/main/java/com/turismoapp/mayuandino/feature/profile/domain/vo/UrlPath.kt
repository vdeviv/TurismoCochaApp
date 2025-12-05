package com.turismoapp.mayuandino.feature.profile.domain.vo

@JvmInline
value class UrlPath(val value: String) {
    init {
        require(value.length > 4) { "URL muy corta" }
        require(value.startsWith("http://") || value.startsWith("https://")) { "URL inválida" }
    }
    override fun toString() = value
    companion object { fun of(raw: String) = UrlPath(raw.trim()) }
}
