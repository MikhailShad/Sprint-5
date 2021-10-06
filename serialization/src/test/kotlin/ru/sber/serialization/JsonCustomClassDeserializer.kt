package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JsonCustomClassDeserializer {

    @Test
    fun `Нобходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val simpleModuleClient7 = SimpleModule().addDeserializer(Client7::class.java, Client7Deserializer)
        val objectMapper = ObjectMapper()
            .registerModules(simpleModuleClient7)

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }

    companion object Client7Deserializer : StdDeserializer<Client7>(Client7::class.java) {

        override fun deserialize(jp: JsonParser?, ctxt: DeserializationContext?): Client7 {
            val node: JsonNode? = jp?.codec?.readTree(jp)
            val fio = node!!.get("client").textValue().split(" ")
            val lastName = fio[0]
            val firstName = fio[1]
            val middleName = fio[2]

            return Client7(firstName, lastName, middleName)
        }
    }
}
