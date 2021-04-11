package kz.logistics.model

data class Load(
    val originAddress: String = "",
    val destAddress: String = "",
    val loadingDate: String = "",
    val price: String = "",
    val good: String = "",
    val weight: String = "",
    val area: String = ""
)