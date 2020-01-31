package sh.phoenix.ilovezappos.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.error_view.*
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.service.data.common.ErrorType

abstract class BaseFragment : Fragment() {
    private lateinit var errorDialog: Dialog

    private lateinit var mContext: Context

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return onCreateViewBase(inflater, container, savedInstanceState)
    }

    abstract fun onCreateViewBase(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?

    abstract fun onRetryClick()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()

        setErrorDialogView()
    }

    private fun setErrorDialogView() {
        errorDialog = Dialog(mContext)
        errorDialog.setContentView(R.layout.error_view)
        errorDialog.setCanceledOnTouchOutside(false)

        errorDialog.retryButton.setOnClickListener {
            hideErrorDialog()
            onRetryClick()
        }

        errorDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        errorDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun showErrorDialog(type: ErrorType?) {
        if(type != null && type == ErrorType.NETWORK_ERROR) {
            errorDialog.errorTitle.text = resources.getString(R.string.network_error_title)
            errorDialog.errorInfo.text = resources.getString(R.string.network_error_message)
            errorDialog.errorImage1.visibility = View.VISIBLE
            errorDialog.errorImage2.setImageResource(R.drawable.ic_wifi_off)
        } else {
            errorDialog.errorTitle.text = resources.getString(R.string.server_error_title)
            errorDialog.errorInfo.text = resources.getString(R.string.server_error_message)
            errorDialog.errorImage1.visibility = View.GONE
            errorDialog.errorImage2.setImageResource(R.drawable.ic_server_down)
        }

        errorDialog.show()
    }

    private fun hideErrorDialog() {
        if (errorDialog.isShowing) errorDialog.dismiss()
    }
}