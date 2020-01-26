package sh.phoenix.ilovezappos.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import sh.phoenix.ilovezappos.R

class TransactionFragment : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        val textView: TextView = root.findViewById(R.id.text_transaction)
        transactionViewModel.text.observe(this, Observer {
            textView.text = it
        })

        return root
    }
}