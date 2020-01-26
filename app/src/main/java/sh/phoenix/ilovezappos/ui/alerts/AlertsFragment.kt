package sh.phoenix.ilovezappos.ui.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import sh.phoenix.ilovezappos.R

class AlertsFragment : Fragment() {
    private lateinit var alertsViewModel: AlertsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertsViewModel =
            ViewModelProviders.of(this).get(AlertsViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_alerts, container, false)

        val textView: TextView = root.findViewById(R.id.text_alerts)
        alertsViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}