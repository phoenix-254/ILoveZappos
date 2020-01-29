package sh.phoenix.ilovezappos.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_alerts.*
import sh.phoenix.ilovezappos.R

class AlertsFragment : Fragment() {
    private lateinit var alertsViewModel: AlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertsViewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_alerts, container, false)

        alertsViewModel.text.observe(this, Observer {
            textAlerts.text = it
        })

        return root
    }
}