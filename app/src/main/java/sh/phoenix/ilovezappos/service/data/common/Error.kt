package sh.phoenix.ilovezappos.service.data.common

enum class ErrorType {
    NETWORK_ERROR,
    SERVER_ERROR
}

data class Error(
    val errorType: ErrorType,
    val errorMessage: String
)