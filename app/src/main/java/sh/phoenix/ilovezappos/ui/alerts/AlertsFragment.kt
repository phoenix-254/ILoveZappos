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
import kotlinx.android.synthetic.main.alert_detail_view.*
import kotlinx.android.synthetic.main.fragment_alerts.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import sh.phoenix.ilovezappos.R
import sh.phoenix.ilovezappos.model.AlertItem
import sh.phoenix.ilovezappos.model.AlertType
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertItemEvent
import sh.phoenix.ilovezappos.ui.alerts.alertlist.AlertListAdapter
import java.text.SimpleDateFormat
import java.util.*

class AlertsFragment : Fragment(), KodeinAware {
    override val kodein by closestKodein()

    private lateinit var mContext: Context

    private lateinit var adapter: AlertListAdapter

    private lateinit var alertViewModal: Dialog

    private lateinit var operatorOptions: Array<String>

    private var alertItems: List<AlertItem>? = null
    private var selectedItemCreatedDate: String? = null

    private val viewModel: AlertsViewModel by instance()

    private enum class AlertModalType {
        ADD, EDIT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onStart() {
        super.onStart()

        operatorOptions = resources.getStringArray(R.array.operators_array)

        initAlertViewModal()

        setupAdapter()

        observeViewModel()

        addAlertButton.setOnClickListener {
            showAlertModal(AlertModalType.ADD)
        }

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
            if(selectedItemCreatedDate != null) {
                val item = alertItems?.find { it.createdDate == selectedItemCreatedDate }
                if(item != null) viewModel.deleteAlertFromDatabase(item)
            }
            hideAlertModal()
        }

        ArrayAdapter.createFromResource(
            mContext,
            R.array.operators_array,
            R.layout.operator_dropdown_menu_item
        ).also { adapter ->
            alertViewModal.operatorDropdown.setAdapter(adapter)
        }

        alertViewModal.cancelButton.setOnClickListener {
            hideAlertModal()
            clearAlertInfo()
        }

        alertViewModal.saveButton.setOnClickListener {
            if(isValidAlertInfo()) {
                clearAlertErrors()

                val createdDate: String
                var price = alertViewModal.priceEditText.text.toString()
                val type = getAlertType(alertViewModal.operatorDropdown.text.toString())

                if(selectedItemCreatedDate != null) {
                    createdDate = selectedItemCreatedDate as String
                } else {
                    createdDate = getCurrentCalenderTime()
                }

                if(!price.startsWith("$")) price = "$$price"

                val item = AlertItem(createdDate, alertViewModal.alertNameEditText.text.toString(),
                    getAlertDescriptionText(price, type), price, type)

                viewModel.insertAlertToDatabase(item)

                hideAlertModal()
            }
        }

        alertViewModal.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun clearAlertInfo() {
        alertViewModal.operatorDropdown.text?.clear()
        alertViewModal.priceEditText.text?.clear()
        alertViewModal.alertNameEditText.text?.clear()
    }

    private fun clearAlertErrors() {
        alertViewModal.errorText.visibility = View.GONE
    }

    private fun isValidAlertInfo() : Boolean {
        if(alertViewModal.operatorDropdown.text.toString().isEmpty() ||
            alertViewModal.priceEditText.text.toString().isEmpty() ||
            alertViewModal.alertNameEditText.text.toString().isEmpty())
        {
            alertViewModal.errorText.visibility = View.VISIBLE
            return false
        }

        return true
    }

    private fun showAlertModal(type: AlertModalType) {
        if(type == AlertModalType.ADD) {
            alertViewModal.viewTitle.text = resources.getString(R.string.add_alert_view_title)
            alertViewModal.deleteButton.visibility = View.INVISIBLE
        } else if(type == AlertModalType.EDIT) {
            alertViewModal.viewTitle.text = resources.getString(R.string.edit_alert_view_title)

            // Fill modal with existing info
            if(selectedItemCreatedDate != null) {
                val alert = alertItems?.find { it.createdDate == selectedItemCreatedDate }
                if(alert != null) {
                    if(alert.alertType == AlertType.BUY) {
                        alertViewModal.operatorDropdown.setText(operatorOptions[0])
                    } else if(alert.alertType == AlertType.SELL) {
                        alertViewModal.operatorDropdown.setText(operatorOptions[1])
                    }

                    alertViewModal.priceEditText.setText(alert.triggerPrice)
                    alertViewModal.alertNameEditText.setText(alert.title)
                }
            }

            alertViewModal.deleteButton.visibility = View.VISIBLE
        }

        clearAlertErrors()

        alertViewModal.show()
    }

    private fun hideAlertModal() {
        selectedItemCreatedDate = null
        alertViewModal.hide()
    }

    private fun setupAdapter() {
        adapter = AlertListAdapter(mContext)

        adapter.event.observe(this, Observer {
            when (it) {
                is AlertItemEvent.OnClick -> handleClickEvent(it.createdDate)
            }
        })

        alertList.adapter = adapter
    }

    private fun handleClickEvent(createdDate: String) {
        selectedItemCreatedDate = createdDate
        showAlertModal(AlertModalType.EDIT)
    }

    private fun observeViewModel() {
        viewModel.errorState.observe(this, Observer {
            if(it) {
                Toast.makeText(mContext, "Error!", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.alertList.observe(this, Observer {
            alertItems = it
            if(!it.isNullOrEmpty()) {
                adapter.submitList(it)
                infoText.visibility = View.INVISIBLE
                alertList.visibility = View.VISIBLE
            } else {
                alertList.visibility = View.INVISIBLE
                infoText.visibility = View.VISIBLE
            }
        })
    }

    private fun getAlertType(operator: String): AlertType {
        return if(operator == operatorOptions[0]) AlertType.BUY else AlertType.SELL
    }

    private fun getCurrentCalenderTime(): String {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val format = SimpleDateFormat("d MMM yyyy HH:mm:ss Z", Locale.US)
        format.timeZone = cal.timeZone
        return format.format(cal.time)
    }

    private fun getAlertDescriptionText(price: String, type: AlertType): String {
        var descriptionText = resources.getString(R.string.alert_description_prefix)

        if(type == AlertType.BUY) {
            descriptionText = "$descriptionText below"
        } else if(type == AlertType.SELL) {
            descriptionText = "$descriptionText above"
        }

        descriptionText = "$descriptionText $price."

        return descriptionText
    }
}