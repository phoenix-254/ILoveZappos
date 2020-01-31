package sh.phoenix.ilovezappos

import sh.phoenix.ilovezappos.database.AlertEntity
import sh.phoenix.ilovezappos.model.AlertItem

internal val AlertEntity.toAlertItem: AlertItem
    get() = AlertItem(
        this.createdDate,
        this.title,
        this.description,
        this.triggerPrice,
        this.alertType
    )

internal fun List<AlertEntity>.toAlertItemList(): List<AlertItem> = this.flatMap {
    listOf(it.toAlertItem)
}

internal val AlertItem.toAlertEntity: AlertEntity
    get() = AlertEntity(
        this.createdDate,
        this.title,
        this.description,
        this.triggerPrice,
        this.alertType
    )