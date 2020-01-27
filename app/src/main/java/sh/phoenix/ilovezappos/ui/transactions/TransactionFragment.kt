package sh.phoenix.ilovezappos.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.data.Transaction
import sh.phoenix.ilovezappos.service.BitStampServiceFactory

class TransactionFragment : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel =
            ViewModelProviders.of(this).get(TransactionViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_transaction, container, false)

        textView = root.findViewById(R.id.text_transaction)
        transactionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        loadTransactionHistory()

        return root
    }

    private fun loadTransactionHistory() {
        GlobalScope.launch(Dispatchers.Main) {
            val client = BitStampServiceFactory.BIT_STAMP_API_CLIENT
            val serviceResponse = client.getTransactionHistory("btcusd")

            if(serviceResponse.isSuccessful) {
                val transactions: List<Transaction>? = serviceResponse.body()

                val amt: String? = transactions?.get(0)?.amount

                textView.text = amt ?: ""
            } else {
                textView.text = "Error ${serviceResponse.code()}"
            }
        }
    }
}