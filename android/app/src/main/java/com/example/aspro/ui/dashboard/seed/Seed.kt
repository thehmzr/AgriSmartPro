package com.example.aspro.ui.dashboard.seed


data class Seed(
    val id: Int,
    val name: String,
    val variety: String,
    val packingWeight: Double,
    val numberOfPackets: Int,
    val pricePerPacking: Double
)
