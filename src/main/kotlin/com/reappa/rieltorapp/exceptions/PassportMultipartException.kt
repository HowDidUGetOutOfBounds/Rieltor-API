package com.reappa.rieltorapp.exceptions

import java.util.*

class PassportMultipartException(
    override val message: String,
): Exception() {
    private val timestamp: Date = Date()
}