package com.example.dessertclicker.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.R
import com.example.dessertclicker.data.Datasource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    private val _dessertStateFlow = MutableStateFlow(DessertUiState())
    val dessertStateFlow: StateFlow<DessertUiState> = _dessertStateFlow.asStateFlow()

    private var currentDessert = Datasource.dessertList.first()

    /**
     * Called when user click the dessert shown
     */
    fun clickDessert() {
        // Update the revenue
        _dessertStateFlow.update { currentState ->
            currentState.copy(
                dessertSold = dessertStateFlow.value.dessertSold + 1,
                currentRevenue = dessertStateFlow.value.currentRevenue + currentDessert.price
            )
        }
        // Decide which dessert to show next
        determineDessertToShow()
    }

    /**
     * Determine which dessert to show.
     */
    private fun determineDessertToShow() {
        val desserts = Datasource.dessertList

        currentDessert = desserts.first()
        for (dessert in desserts) {
            if (dessertStateFlow.value.dessertSold >= dessert.startProductionAmount) {
                currentDessert = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        _dessertStateFlow.update { currentState ->
            currentState.copy(currentDessert = currentDessert)
        }
    }

    /**
     * Share desserts sold information using ACTION_SEND intent
     */
    fun shareSoldDessertsInformation(intentContext: Context) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                intentContext.getString(
                    R.string.share_text,
                    dessertStateFlow.value.dessertSold,
                    dessertStateFlow.value.currentRevenue
                )
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)

        try {
            intentContext.startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                intentContext,
                intentContext.getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}