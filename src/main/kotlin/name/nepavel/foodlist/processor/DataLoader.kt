package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import java.util.*

class DataLoader(val name: String, private val siteAccessor: SiteAccessor, private val dataExtractor: DataExtractor) {
    private lateinit var queue: Queue<FoodItem>

    fun queue(queue: Queue<FoodItem>) {
        this.queue = queue
    }

    fun process(): Date {
        try {
            siteAccessor.forEach {
                if (it.second.isPresent) queue.addAll(dataExtractor.extract(mapOf(it.first to it.second.get())))
                if (Thread.currentThread().isInterrupted) return@forEach
            }
        } catch (e: Exception) {
            //log.error("Exception while loading data by $name: ", e)
        }
        return Date()
    }
}