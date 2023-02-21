package org.example.tokens

data class ParenthesisTokens(
    val type: ParenthesisType
) : Token() {
    override fun toString(): String = type.tokenName

    override fun reverse(): String {
        return String.format("%s", type.code)
    }
}

enum class ParenthesisType(
    val tokenName: String,
    val code: String
) {
    LEFT("LPAREN", "("),
    RIGHT("RPAREN", ")")
}
