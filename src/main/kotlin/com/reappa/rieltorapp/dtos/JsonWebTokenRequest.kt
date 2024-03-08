package com.reappa.rieltorapp.dtos

import lombok.AllArgsConstructor
import lombok.Data
import lombok.Getter

@Data
@AllArgsConstructor
data class JsonWebTokenRequest(
    val email:String,
    val password:String,
    ) {

}