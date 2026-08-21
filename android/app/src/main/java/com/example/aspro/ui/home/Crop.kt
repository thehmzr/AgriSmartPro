package com.example.aspro.ui.home

data class Crop(
    val id: Int,
    val plotNumber: String,
    val landInPlot: String,
    val landUnit: String,
    val sowingDate: String,
    val seed: String,
    val seedQuantity: Double,
    val landPreparationExpenses: Double
)
