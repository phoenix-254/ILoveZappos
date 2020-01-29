package sh.phoenix.ilovezappos

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_network_error.*
import sh.phoenix.ilovezappos.utility.Utility

class MainActivity : AppCompatActivity() {
    private lateinit var networkErrorDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.navHostFragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_transactions,
                R.id.navigation_order_book,
                R.id.navigation_alerts
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        initNetworkErrorDialog()
    }

    override fun onResume() {
        super.onResume()
        doNetworkCheck()
    }

    private fun initNetworkErrorDialog() {
        networkErrorDialog = Dialog(this)
        networkErrorDialog.setContentView(R.layout.view_network_error)
        networkErrorDialog.setCanceledOnTouchOutside(false)

        val tryAgainButton: MaterialButton = networkErrorDialog.tryAgainButton
        tryAgainButton.setOnClickListener {
            doNetworkCheck()
        }

        networkErrorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun doNetworkCheck() {
        if(!Utility.isConnectedToNetwork(this)) {
            showNetworkError()
        } else {
            hideNetworkError()
        }
    }

    private fun showNetworkError() {
        if (!networkErrorDialog.isShowing) networkErrorDialog.show()
    }

    private fun hideNetworkError() {
        if (networkErrorDialog.isShowing) networkErrorDialog.dismiss()
    }
}
