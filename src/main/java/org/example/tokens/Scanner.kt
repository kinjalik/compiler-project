package org.example.tokens

import org.example.exceptions.TreeBuildException
import java.util.*

class Scanner(input: String) : Iterator<Token> {
    private val inputIterator: CharIterator
    private var currentLine = 1
    private var currentCol = 0
    private var balance = 0

    private val separators = setOf(' ', '\n', '\t', '\r')
    private val digits = '0'..'9'
    private val specialCharacters = setOf('(', ')')
    init {
        inputIterator = input.iterator()
    }

    fun getCurrentLine(): Int {
        return currentLine
    }

    fun getCurrentCol(): Int {
        return currentCol
    }
    override fun hasNext() = inputIterator.hasNext()
    override fun next(): Token {
        var curChar = getNextChar() ?: throw NoSuchElementException()

        val token = when (curChar) {
            in separators -> PhantomToken(curChar)
            '(' -> {
                balance += 1
                ParenthesisToken(ParenthesisType.LEFT)
            }
            ')' -> {
                balance -= 1
                ParenthesisToken(ParenthesisType.RIGHT)
            }
            in digits -> readDigit(curChar)
            else -> readAtom(curChar)
        }
        if (balance < 0) {
            throw TreeBuildException(currentLine, currentCol, ")")
        }
        return token
    }

    private fun readDigit(firstChar: Char): Token {
        var rawValue = "" + firstChar
        do {
            val curChar = getNextChar() ?: break
            if (curChar in digits)
                rawValue += curChar
            else {
                putCharToQueue(curChar)
                break
            }
        }  while (hasNextChar())
        return DigitToken(rawValue.toInt())
    }

    private fun readAtom(firstChar: Char): Token {
        var value = "" + firstChar
        do {
            val curChar = getNextChar() ?: break
            if (curChar in separators || curChar in specialCharacters) {
                putCharToQueue(curChar)
                break
            } else {
                value += curChar
            }
        } while (hasNextChar())
        return AtomToken(value)
    }

    /**
     * This queue is required if we already took character from input, but we do not need it yed
     */
    private val charQueue: Queue<Char> = LinkedList()
    private fun hasNextChar(): Boolean = charQueue.isNotEmpty() || inputIterator.hasNext()
    private fun getNextChar(): Char? {
        var flag = true
        val res = if (charQueue.isNotEmpty()) {
            flag = false
            charQueue.poll()
        }
        else if (inputIterator.hasNext())
            inputIterator.nextChar()
        else
            return null

        if (res == '\n') {
            currentLine++
            currentCol = 0
        } else if (res != null && flag){
            currentCol++
        }
        return res
    }
    private fun putCharToQueue(ch: Char) = charQueue.add(ch)

    fun isOk() {
        if (balance > 0) {
            throw TreeBuildException(currentLine, currentCol, "(")
        }
    }
}