package ru.nooomer.tvmedapp_compose.api.models

import com.nooomer.tvmspring.dto.RoleDto
import java.util.UUID

data class UserDto(
    var id: UUID,
    var surename: String,
    var name: String,
    var sName: String,
    var phoneNumber: String,
    var insurancePolicyNumber: String,
    var password: String,
    var userType: String,
    var roles: MutableSet<RoleDto>,
)
