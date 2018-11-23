package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import org.jsoup.nodes.Document

interface DataExtractor {
    fun extract(urlToContent: Map<String, Document>): List<FoodItem>
}