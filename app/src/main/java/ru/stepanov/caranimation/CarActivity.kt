package ru.stepanov.caranimation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 *
 *
 * @author Maksim Stepanov on 16.10.2019
 */
class CarActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_activity)
    }
}