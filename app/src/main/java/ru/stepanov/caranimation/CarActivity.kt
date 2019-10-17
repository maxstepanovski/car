package ru.stepanov.caranimation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * @author Maksim Stepanov on 16.10.2019
 */
class CarActivity : FragmentActivity() {
    lateinit var carView: CarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_activity)
        carView = findViewById(R.id.car_view)
    }

    override fun onResume() {
        super.onResume()
        carView.startParty()
    }

    override fun onPause() {
        super.onPause()
        carView.partyIsOver()
    }
}