package name.nepavel.foodlist.processor

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class IntelmealAcc : LinkedSiteAccessor("http://www.intelmeal.ru/nutrition/foodlist_Baby_Foods.php") {
    override fun Document.filterLinkContainers(): Elements =
        Elements(getElementsByTag("ul").filter { it.hasClass("list1") })

    override fun Elements.extractLinksToVisit(): List<String> = flatMap { it.getElementsByTag("a") }
            .map { it.attr("abs:href") }
            .filter { it.startsWith("http://www.intelmeal.ru/nutrition") }


}