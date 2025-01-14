package org.mandziuk.calculalim.db.dtos

data class NutrientEnabledDTO(
    val nutrientName: String,
    val enabled: Boolean,
    val nutrientId: Long
)
