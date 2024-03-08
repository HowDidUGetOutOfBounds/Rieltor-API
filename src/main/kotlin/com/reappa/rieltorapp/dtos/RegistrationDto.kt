package com.reappa.rieltorapp.dtos

import lombok.AllArgsConstructor
import lombok.Data

//@Data
@AllArgsConstructor
data class RegistrationDto(
    val email:String,
    val password:String,
    val confirmPassword:String,
){

}
