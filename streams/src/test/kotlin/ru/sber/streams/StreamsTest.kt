package ru.sber.streams

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StreamsTest {

    @Test
    fun getSumWithIndexDivisibleByThreeTest() {
        assertEquals(4, getSumWithIndexDivisibleByThree(listOf(3L ,2L, 563L, 1L, 244L, 27L, 0L)))
        assertEquals(612, getSumWithIndexDivisibleByThree(listOf(22L, 13L ,2L, 563L, 1L, 244L, 27L, 0L)))
    }

    @Test
    fun generateFibonacciSequenceTest() {
        assert(generateFibonacciSequence().take(4).toList() == listOf(0, 1, 1, 2))
        assert(generateFibonacciSequence().take(10).toList() == listOf(0, 1, 1, 2, 3, 5, 8, 13, 21, 34))
    }

    @Test
    fun getCustomersCitiesTest() {
        assertEquals(setOf(Moscow, Omsk, Tomsk, Perm, Tula), shop.getCustomersCities())
    }

    @Test
    fun allOrderedProductsTest() {
        assertEquals(setOf(milk, bread, water, banana, apple, mango, beef, yogurt, chocolate, oil), shop.allOrderedProducts())
    }

    @Test
    fun getCustomerWithMaximumNumberOfOrdersTest() {
        assertEquals("Irina", shop.getCustomerWithMaximumNumberOfOrders()!!.name)
    }

    @Test
    fun getMostExpensiveProductTest() {
        val customer1 = Customer("Ivan", Moscow, listOf(Order(listOf(milk, bread)), Order(listOf(beef))))
        val customer2 = Customer("Ivan", Tula,listOf(Order(listOf(mango, water))))
        assertEquals(beef, customer1.getMostExpensiveProduct())
        assertEquals(mango, customer2.getMostExpensiveProduct())
    }

    @Test
    fun getNumberOfOrderedProductByCityTest() {
        assertEquals(8, shop.getNumberOfDeliveredProductByCity()[Moscow])
        assertEquals(0, shop.getNumberOfDeliveredProductByCity()[Tula])
    }

    @Test
    fun getMostPopularProductInCity() {
        assertEquals(milk, shop.getMostPopularProductInCity()[Moscow])
        assertEquals(mango, shop.getMostPopularProductInCity()[Perm])
    }

    @Test
    fun getProductsOrderedByAllTest() {
        val shop = Shop("Test shop", listOf(
            Customer("Alex", Moscow, listOf(Order(listOf(water, mango)), Order(listOf(bag)))),
            Customer("Mary", Tula, listOf(Order(listOf(chocolate, bag)))),
            Customer("Ivan", Perm, listOf(Order(listOf(water, bag)))),
        ))
        assertEquals(setOf(bag), shop.getProductsOrderedByAll())
    }

}