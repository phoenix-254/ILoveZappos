package sh.phoenix.ilovezappos.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sh.phoenix.ilovezappos.AppConstants
import sh.phoenix.ilovezappos.NetworkException
import sh.phoenix.ilovezappos.service.data.common.Error
import sh.phoenix.ilovezappos.service.data.common.ErrorType
import sh.phoenix.ilovezappos.service.BitStampApiService
import sh.phoenix.ilovezappos.service.data.Transaction
import sh.phoenix.ilovezappos.service.data.TransactionResponse
import sh.phoenix.ilovezappos.service.data.common.Response
import java.lang.Exception

class TransactionViewModel(private val service: BitStampApiService) : ViewModel() {
    private val _response = MutableLiveData<Response>()
    val response: LiveData<Response> get() = _response

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> get() = _loadingState

    fun getTransactionsData() {
        _loadingState.value = true

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val serviceResponse = service.getTransactionHistory(AppConstants.CURRENCY_PAIR)

                _loadingState.value = false
                if(serviceResponse.isSuccessful) {
                    _response.value = Response(serviceResponse.code(), true, TransactionResponse(serviceResponse.body()), null)
                } else {
                    handleError(null)
                }
            }
            catch (e : Exception) {
                _loadingState.value = false
                handleError(e)
            }
        }
    }

    private fun handleError(e : Exception?) {
        val message = e?.message.toString()
        val type = if(e != null && e is NetworkException) ErrorType.NETWORK_ERROR else ErrorType.SERVER_ERROR
        _response.value = Response(400, false, null, Error(type, message))
    }
}