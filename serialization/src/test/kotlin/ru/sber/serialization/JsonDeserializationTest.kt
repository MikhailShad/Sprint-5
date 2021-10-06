package ru.sber.serialization

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class JsonDeserializationTest {

    @Test
    fun `Имена свойств совпадают`() {
        // given
        val data =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `В JSON есть лишние свойства Настроить ObjectMapper`() {
        // given
        val data =
            """{"city": "Москва", "firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `В JSON есть лишние свойства Настроить через аннотацию`() {
        // given
        val data =
            """{"city": "Москва", "firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client1>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Имена свойств различаются`() {
        // given
        val data =
            """{"name": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client2>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Кастомный формат даты`() {
        // given
        val data =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "01-01-1990"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule())

        // when
        val client = objectMapper.readValue<Client3>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
        assertEquals("123456", client.passportNumber)
        assertEquals("1234", client.passportSerial)
        assertEquals(LocalDate.of(1990, 1, 1), client.birthDate)
    }

    @Test
    fun `Поддержка optional типа`() {
        // given
        val data1 =
            """{"firstName": "Иван", "lastName": "Иванов", "middleName": "Иванович", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), JavaTimeModule(), Jdk8Module())

        // when
        val client1 = objectMapper.readValue<Client4>(data1)

        // then
        assertEquals("Иванович", client1.middleName.get())

        // given
        val data2 =
            """{"firstName": "Иван", "lastName": "Иванов", "passportNumber": "123456", "passportSerial": "1234", "birthDate": "1990-01-01"}"""

        // when
        val client2 = objectMapper.readValue<Client4>(data2)

        // then
        assertNotNull(client2.middleName)
        assertFalse(client2.middleName.isPresent)
    }
}
