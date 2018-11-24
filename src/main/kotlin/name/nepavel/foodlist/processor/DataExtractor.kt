package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import org.jsoup.select.Elements

interface DataExtractor {
    fun extract(urlToContent: Map<String, Elements>): List<FoodItem>
}