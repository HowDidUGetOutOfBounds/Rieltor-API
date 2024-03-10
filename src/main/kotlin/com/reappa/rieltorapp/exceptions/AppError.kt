package com.reappa.rieltorapp.exceptions

import lombok.AllArgsConstructor
import lombok.Data
import java.util.Date

@AllArgsConstructor
@Data
class AppError(
    private val status: Int,
    private val message: String,
) {
    private val timestamp:Date= Date()
}
