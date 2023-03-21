package org.example.tokens

import java.util.LinkedList
import java.util.Queue

class Scanner(input: String) : Iterator<Token> {
    private val inputIterator: CharIterator
    private var currentLine = 1;
    private var currentCol = 1;

    private val separators = setOf(' ', '\n', '\t')
    private val digits = '0'..'9'
    private val specialCharacters = setOf('(', ')')
    init {
        inputIterator = input.iterator()
    }

    override fun hasNext() = inputIterator.hasNext()
    override fun next(): Token {
        var curChar = getNextChar() ?: throw NoSuchElementException()

        return when (curChar) {
            in separators -> PhantomToken(curChar)
            '(' -> ParenthesisToken(ParenthesisType.LEFT)
            ')' -> ParenthesisToken(ParenthesisType.RIGHT)
            in digits -> readDigit(curChar)
            else -> readAtom(curChar)
        }
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
        val res = if (charQueue.isNotEmpty())
            charQueue.poll()
        else if (inputIterator.hasNext())
            inputIterator.nextChar()
        else
            return null

        if (res == '\n') {
            currentLine++
            currentCol = 1
        }
        return res
    }
    private fun putCharToQueue(ch: Char) = charQueue.add(ch)
}