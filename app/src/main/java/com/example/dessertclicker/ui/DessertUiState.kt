package com.example.dessertclicker.ui

import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert

/**
 * This class contains all the relevant data used by the UI
 * packed in one place. It is also possible to put all these properties
 * in the ViewModel, but having them in one class instead of putting
 * them in multiple States makes it more concise
 */
data class DessertUiState(
    val currentDessert: Dessert = Datasource.dessertList.first(),
    val dessertSold: Int = 0,
    val currentRevenue: Int = 0,
    val currentDessertIndex: Int = 0
)