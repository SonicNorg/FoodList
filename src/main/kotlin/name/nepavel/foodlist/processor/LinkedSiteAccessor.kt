package name.nepavel.foodlist.processor

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import org.slf4j.LoggerFactory

abstract class LinkedSiteAccessor(override val url: String, val param: String = "") : SiteAccessor {
    private val log = LoggerFactory.getLogger(LinkedSiteAccessor::class.java)

    private val toVisit: LinkedBlockingDeque<String> = LinkedBlockingDeque()
    private val visited: MutableSet<String> = HashSet(100)

    override fun iterator(): Iterator<Pair<String, Optional<Elements>>> {
        visited.clear()
        toVisit.clear()
        retryOnceThenIgnore { extractLinks(Jsoup.connect(url + param).get().allElements) }
        visited.add(url)
        log.info("Visited: {}", url)
        return object : Iterator<Pair<String, Optional<Elements>>> {
            override fun hasNext(): Boolean = toVisit.isNotEmpty()

            override fun next(): Pair<String, Optional<Elements>> {
                val nextUrl = toVisit.poll() ?: return "" to Optional.empty()
                visited.add(nextUrl)
                log.info("Visited: {}", url)
                return nextUrl to Optional.ofNullable(retryOnceThenIgnore {
                    Jsoup.connect(nextUrl + param).get().allElements
                }?.also {
                    extractLinks(it)
                })
            }
        }
    }

    open fun Elements.filterLinkContainers(): Elements = this

    open fun Elements.extractLinksToVisit(): List<String> = flatMap { it.getElementsByTag("a") }
            .map { it.attr("href") }
            .filter { it.startsWith("/") }
            .map { url + it }

    private fun extractLinks(elements: Elements) {
        toVisit.addAll(elements
                .filterLinkContainers()
                .extractLinksToVisit()
                .filter {
                    !visited.contains(it)
                            && !toVisit.contains(it)
                            && !it.endsWith(".jpg", true)
                            && !it.endsWith(".jpeg", true)
                            && !it.endsWith(".png", true)
                }
                .distinct())
    }
}

