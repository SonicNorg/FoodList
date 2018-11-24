package name.nepavel.foodlist.processor

import name.nepavel.foodlist.model.FoodItem
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory

class IntelmealEx : DataExtractor {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun extract(urlToContent: Map<String, Elements>): List<FoodItem> {
        return urlToContent.flatMap { entry ->
            val category = entry.value.first { it.hasClass("publish1") }.text()
            log.info("Processing category: {}", category)
            entry.value.filter { element ->
                element.hasClass("bb10 L700")
            }.map { it.extractItem(category) }
        }
    }

    private fun Element.extractItem(category: String): FoodItem {
        return FoodItem(
                child(0).text(),
                child(1).text().toFloat(),
                child(2).text().toFloat(),
                child(3).text().toFloat(),
                (child(4).text().toFloat() * 1000).toInt(),
                null,
                category,
                child(0).child(0).attr("abs:href")
        )
    }
}