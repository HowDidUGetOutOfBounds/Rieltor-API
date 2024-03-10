package com.reappa.rieltorapp.dtos

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class JsonWebTokenResponse(
    private val token: String,
    ) {

}
