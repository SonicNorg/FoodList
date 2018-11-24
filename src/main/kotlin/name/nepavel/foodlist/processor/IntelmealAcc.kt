package name.nepavel.foodlist.processor

import org.jsoup.select.Elements

class IntelmealAcc : LinkedSiteAccessor("http://www.intelmeal.ru/nutrition/food_category.php") {
    override fun Elements.filterLinkContainers(): Elements =
        Elements(this.filter { it.hasClass("tH") && it.tagName() == "table" } )

    override fun Elements.extractLinksToVisit(): List<String> = flatMap { it.getElementsByTag("a") }
            .map { it.attr("abs:href") }
            .filter { it.startsWith("http://www.intelmeal.ru/nutrition") }
}