package page.shellcore.tech.android.viewmodellivedatakotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_view_model.*
import page.shellcore.tech.android.viewmodellivedatakotlin.R
import page.shellcore.tech.android.viewmodellivedatakotlin.utils.Sumar

class ViewModelActivity : AppCompatActivity() {

    private var res: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_model)
        Log.d("MVVM", "onCreate()")

        setupView()
    }

    private fun setupView() {
        txtSumar.text = "$res"

        btnSumar.setOnClickListener {
            res = Sumar.sumar(res)
            txtSumar.text = "$res"
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MVVM", "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MVVM", "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MVVM", "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MVVM", "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MVVM", "onDestroy()")
    }
}
