package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import java.util.concurrent.LinkedBlockingQueue
import kotlin.test.Ignore
import kotlin.test.Test

class IntelmealTest {

    @Test
    @Ignore("Этот тест реально потащит ВСЕ данные. Использовался для отладки парсера.")
    fun testIntelmeal() {
        val acc = IntelmealAcc()
        val ex = IntelmealEx()
        val queue = LinkedBlockingQueue<FoodItem>()
        val dataLoader = DataLoader("Intelmeal", acc, ex)
        dataLoader.queue(queue)
        dataLoader.process()
    }
}