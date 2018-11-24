package name.nepavel.foodlist.processor

import org.jsoup.select.Elements
import java.io.InterruptedIOException
import java.util.*
import org.slf4j.LoggerFactory

interface SiteAccessor : Iterable<Pair<String, Optional<Elements>>> {
    companion object {
        val log = LoggerFactory.getLogger(SiteAccessor::class.java)
    }

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
                log.warn("Retry in 50 ms: ", retry)
                Thread.sleep(50)
                block.invoke()
            } catch (e: Exception) {
                log.error("Unrecoverable: ", retry)
                null
            }
        }
    }
}