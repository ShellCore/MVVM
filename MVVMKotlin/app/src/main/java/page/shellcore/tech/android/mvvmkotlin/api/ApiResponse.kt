package page.shellcore.tech.android.mvvmkotlin.api

import retrofit2.Response
import java.util.regex.Pattern

sealed class ApiResponse<T> {

    companion object {
        fun <T> create(error: Throwable): ApiErrorResponse<T> =
            ApiErrorResponse(error.message ?: "Unknown error")

        fun <T> create(response: Response<T>): ApiResponse<T> = if (response.isSuccessful) {
            val body = response.body()
            if (body == null || response.code() == 204) {
                ApiEmptyResponse()
            } else {
                ApiSuccessResponse(body, response.headers()["link"])
            }
        } else {
            val msg = response.errorBody()?.string()
            val errorMessage = if (msg.isNullOrEmpty()) {
                response.message()
            } else {
                msg
            }
            ApiErrorResponse(errorMessage ?: "Unknown error")
        }
    }
}

class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T,
    val links: Map<String, String>
) : ApiResponse<T>() {

    constructor(body: T, linksHeader: String?) : this(
        body = body,
        links = linksHeader?.extractLinks() ?: emptyMap()
    )

    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let {
            val matcher = PAGE_PATTERN.matcher(it)
            if (matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    null
                }
            }
        }
    }

    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
            return links
        }
    }
}

data class ApiErrorResponse<T>(
    val errorMessage: String
) : ApiResponse<T>()