package sh.phoenix.ilovezappos.ui.alerts

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.alert_detail_view.*
import kotlinx.android.synthetic.main.fragment_alerts.*
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertItemEvent
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertListAdapter

class AlertsFragment : Fragment() {
    private lateinit var mContext: Context

    private lateinit var viewModel: AlertsViewModel

    private lateinit var adapter: AlertListAdapter

    private lateinit var alertViewModal: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_alerts, container, false)

        viewModel = ViewModelProvider(this).get(AlertsViewModel::class.java)

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()

        initAlertViewModal()

        addAlertButton.setOnClickListener {
            alertViewModal.show()
        }

        setupAdapter()

        observeViewModel()

        viewModel.handleEvent(AlertItemEvent.OnStart)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        alertList.adapter = null
    }

    private fun initAlertViewModal() {
        alertViewModal = Dialog(mContext)
        alertViewModal.setContentView(R.layout.alert_detail_view)
        alertViewModal.setCanceledOnTouchOutside(false)

        alertViewModal.deleteButton.setOnClickListener {
            // Delete from database.
            alertViewModal.hide()
        }

        ArrayAdapter.createFromResource(
            mContext,
            R.array.operators_array,
            R.layout.operator_dropdown_menu_item
        ).also { adapter ->
            alertViewModal.operatorDropdown.setAdapter(adapter)
        }

        alertViewModal.operatorDropdown.error = "Must select an operator."
        alertViewModal.priceEditText.error = "Must specify a price."
        alertViewModal.alertNameEditText.error = "Must provide a name for your alert."

        alertViewModal.cancelButton.setOnClickListener {
            clearAlertInfo()
            alertViewModal.hide()
        }

        alertViewModal.saveButton.setOnClickListener {
            if(isValidAlertInfo()) {
                // Save to database.
                alertViewModal.hide()
            }
        }

        alertViewModal.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun clearAlertInfo() {
        alertViewModal.operatorDropdown.clearListSelection()
        alertViewModal.priceEditText.text?.clear()
        alertViewModal.alertNameEditText.text?.clear()
    }

    private fun isValidAlertInfo() : Boolean {
        var isValid = true
        if(!alertViewModal.operatorDropdown.isSelected) {
            alertViewModal.operatorDropdownContainer.isErrorEnabled = true
            isValid = false
        }

        if(alertViewModal.priceEditText.text.isNullOrBlank()) {
            alertViewModal.priceEditTextContainer.isErrorEnabled = true
            isValid = false
        }

        if(!alertViewModal.alertNameEditText.text.isNullOrBlank()) {
            alertViewModal.alertNameEditTextContainer.isErrorEnabled = true
            isValid = false
        }

        return isValid
    }

    private fun setupAdapter() {
        adapter = AlertListAdapter(mContext)

        adapter.event.observe(this, Observer {
            // Show option to EDIT / DELETE.
            when (it) {
                is AlertItemEvent.OnClick -> it.position
            }
        })

        alertList.adapter = adapter
    }

    private fun handleClickEvent() {

    }

    private fun observeViewModel() {
        viewModel.errorState.observe(this, Observer {
            if(it) {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.alertList.observe(this, Observer {
            if(it.isNotEmpty()) {
                adapter.submitList(it)
                infoText.visibility = View.INVISIBLE
                alertList.visibility = View.VISIBLE
            } else {
                alertList.visibility = View.INVISIBLE
                infoText.visibility = View.VISIBLE
            }
        })
    }
}