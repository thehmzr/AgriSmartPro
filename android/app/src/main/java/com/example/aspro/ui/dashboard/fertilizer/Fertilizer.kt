package com.example.aspro.ui.dashboard.fertilizer

data class Fertilizer(
    val id: Int,
    val name: String,
    val company: String,
    val packingWeight: Double,
    val numberOfPackets: Int,
    val pricePerPacking: Double
)
