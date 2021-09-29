package ru.sber.streams


// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long {
    return list.asSequence().withIndex()
        .filter { it.index % 3 == 0 }
        .fold(0) { sum, element -> sum + element.value }
}

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> {
    return generateSequence(Pair(0, 1)) { Pair(it.second, it.first + it.second) }.map { it.first }
}

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> {
    return this.customers.asSequence()
        .map { it.city }
        .toSet()
}

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> {
    return this.customers.asSequence()
        .flatMap { it.orders }
        .flatMap { it.products }
        .toSet()
}

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? {
    return this.customers.maxByOrNull { it.orders.size }
}

// 6. Получить самый дорогой продукт, когда-либо приобретенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? {
    return this.orders.asSequence()
        .flatMap { it.products }
        .maxByOrNull { it.price }
}

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> {
    return this.customers.asSequence()
        .map { customer ->
            return@map customer.city to customer.orders.asSequence()
                .filter { it.isDelivered }
                .flatMap { it.products }
                .count()
        }
        .groupingBy { it.first }
        .fold(0) { accumulator, element -> accumulator + element.second }
}

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
//  Адовая наркомания, но что поделать
// Для начала добавим маленькое расширение, которое упростит жизнь
fun <K> MutableMap<K, Int>.accumulateSum(another: Map<K, Int>): MutableMap<K, Int> {
    another.forEach {
        if (this.containsKey(it.key)) {
            this[it.key] = this[it.key]!! + it.value
        } else {
            this.putIfAbsent(it.key, it.value)
        }
    }

    return this
}

fun Shop.getMostPopularProductInCity(): Map<City, Product> {
    return this.customers.asSequence()
        // По каждому покупателю находим его город и список продуктов с числом покупок
        .map { customer ->
            return@map customer.city to customer.orders.asSequence()
                .flatMap { it.products }
                .groupingBy { it }
                .eachCount()
        }
        // Группируем данные по городам, теперь надо склеить данные о купленных продуктах
        .groupingBy { it.first }
        .aggregateTo(mutableMapOf<City, MutableMap<Product, Int>>()) { _, accumulator, element, first ->
            // Если результирующая мапа пустая, создадим новую и положим в нее все, что найдется
            if (first) {
                return@aggregateTo mutableMapOf<Product, Int>().accumulateSum(element.second)
            } else {
                return@aggregateTo accumulator!!.accumulateSum(element.second)
            }
        }
        // Ура, теперь у нас есть мапа городов со списком всех купленных продуктов и их количеством, найдем максимум
        .map { it.key to it.value.maxByOrNull { entry: Map.Entry<Product, Int> -> entry.value }!!.key }
        .toMap()
}

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> {
    return this.customers.asSequence()
        .map { it.orders.flatMap { order -> order.products }.toSet() }
        .reduce { acc: Set<Product>, set: Set<Product> -> acc.intersect(set) }
}

