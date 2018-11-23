package name.nepavel.foodlist.processor

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.InterruptedIOException
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

abstract class LinkedSiteAccessor(override val url: String, val param: String = "") : SiteAccessor {
    private val toVisit: LinkedBlockingDeque<String> = LinkedBlockingDeque()
    private val visited: MutableSet<String> = HashSet(100)

    override fun iterator(): Iterator<Pair<String, Optional<Document>>> {
        visited.clear()
        toVisit.clear()
        retryOnceThenIgnore { extractLinks(Jsoup.connect(url + param).get()) }
        visited.add(url)
        return object : Iterator<Pair<String, Optional<Document>>> {
            override fun hasNext(): Boolean = toVisit.isNotEmpty()

            override fun next(): Pair<String, Optional<Document>> {
                val nextUrl = toVisit.poll() ?: return "" to Optional.empty()
                visited.add(nextUrl)
                return nextUrl to Optional.ofNullable(retryOnceThenIgnore {
                    Jsoup.connect(url + param).get()
                }?.also {
                    extractLinks(it)
                })
            }
        }
    }

    open fun Document.filterLinkContainers(): Elements = allElements

    open fun Elements.extractLinksToVisit(): List<String> = flatMap { it.getElementsByTag("a") }
            .map { it.attr("href") }
            .filter { it.startsWith("/") }
            .map { url + it }

    private fun extractLinks(doc: Document) {
        toVisit.addAll(doc
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

