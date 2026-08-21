package com.example.aspro.ui.dashboard.pesticide

data class Pesticide(
    val id: Int,
    val name: String,
    val company: String,
    val literKilogram: Double,
    val numberOfPackets: Int,
    val pricePerPacking: Double
)
