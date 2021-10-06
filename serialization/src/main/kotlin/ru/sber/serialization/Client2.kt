package ru.sber.serialization

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class Client2(
    @JsonProperty(value = "name")
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val passportNumber: String,
    val passportSerial: String,
    val birthDate: LocalDate
)