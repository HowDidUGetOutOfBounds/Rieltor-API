package com.reappa.rieltorapp.dtos

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class AccountDto(
    private val id: Long,
    private val email: String,
){

}
