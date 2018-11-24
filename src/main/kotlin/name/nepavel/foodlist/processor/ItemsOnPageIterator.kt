package name.nepavel.foodlist.processor

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*


class ItemsOnPageIterator(private val pageIterator: Iterator<Pair<String, Optional<Elements>>>, private val extractPartLinksFromPage: (Elements) -> List<String>) : Iterator<Pair<String, Optional<Elements>>> {
    private val partLinks = LinkedList<String>().toMutableList()

    override fun hasNext(): Boolean = partLinks.isNotEmpty() || pageIterator.hasNext()

    override fun next(): Pair<String, Optional<Elements>> {
        if (partLinks.isEmpty()) {
            val pageUrlToContent: Pair<String, Elements>
            if (!pageIterator.hasNext()) {
//                log.warn("PartOnPageIterator - no next page found!")
                return "" to Optional.empty()
            } else pageUrlToContent = pageIterator.next().run {
//                log.debug("PartsOnPageIterator: Next URL to extract part links from: {}, content is {}present", first, if (second.isPresent) "" else "NOT ")
                if (second.isPresent) first to second.get() else return this
            }

            partLinks.addAll(extractPartLinksFromPage(pageUrlToContent.second).distinct())
//            log.debug("PartOnPageIterator - extracted {} part links from {}", partLinks.size, pageUrlToContent.first)
        }
        return if (partLinks.isEmpty()) {
//            log.debug("PartOnPageIterator - no links")
            "" to Optional.empty()
        } else {
            val head = partLinks.removeAt(0)
//            log.debug("PartOnPageIterator - next link is {}, {} remain", head, partLinks.size)
            head to Optional.ofNullable(Jsoup.connect(head).get().allElements)
        }
    }
}