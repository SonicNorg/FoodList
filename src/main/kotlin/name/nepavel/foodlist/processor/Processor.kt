package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import java.util.concurrent.LinkedBlockingQueue

class Processor {
    fun start() {
        val queue = LinkedBlockingQueue<FoodItem>()
        DataLoader("Intelmeal", IntelmealAcc(), IntelmealEx()).apply { queue(queue) }.process()

    }
}