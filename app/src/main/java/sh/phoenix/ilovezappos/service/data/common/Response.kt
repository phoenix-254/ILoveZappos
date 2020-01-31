package sh.phoenix.ilovezappos.service.data.common

data class Response(
    val statusCode: Int,
    val success: Boolean,
    val data: Data?,
    val error: Error?
)