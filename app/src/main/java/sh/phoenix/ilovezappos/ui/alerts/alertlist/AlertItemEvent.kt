package sh.phoenix.ilovezappos.ui.alerts.alertlist

sealed class AlertItemEvent {
    object OnStart : AlertItemEvent()

    data class OnClick(val createdDate: String) : AlertItemEvent()
}