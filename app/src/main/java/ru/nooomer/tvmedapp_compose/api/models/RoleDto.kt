package com.nooomer.tvmspring.dto

import java.time.LocalDateTime
import java.util.*

data class RoleDto(
    var id: UUID,
    var createdDate: String,
    var updatedDate: String?,
    var name: String,
)