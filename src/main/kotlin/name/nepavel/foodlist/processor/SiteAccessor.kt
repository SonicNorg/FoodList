package name.nepavel.foodlist.processor

import org.jsoup.nodes.Document
import java.io.InterruptedIOException
import java.util.*

interface SiteAccessor : Iterable<Pair<String, Optional<Document>>> {
    val url: String


    //ugly ad-hoc implementation of breaker pattern
    fun <T> retryOnceThenIgnore(block: () -> T?): T? {
        return try {
            block.invoke()
        } catch (noRetry: InterruptedException) {
            throw noRetry
        } catch (noRetry: InterruptedIOException) {
            throw noRetry
        } catch (retry: Exception) {
            try {
                Thread.sleep(50)
                block.invoke()
            } catch (e: Exception) {
                null
            }
        }
    }
}