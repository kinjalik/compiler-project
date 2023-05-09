package org.example.ast

import org.example.Utils.Utils
import org.example.exceptions.ByteBaseOverflowException
import org.example.exceptions.TreeBuildException
import org.example.tokens.DigitToken
import org.example.tokens.PhantomToken
import org.example.tokens.Scanner
import org.example.tokens.Token

class LiteralTreeNode : TreeNode() {
    private var value: Int = 0
    private  var base: Int = 0

    override fun parse(token: Token, tokenIter: Scanner): TreeNode {
        var tk = token
        while (tk is PhantomToken)
            tk = tokenIter.next()

        if (tk !is DigitToken)
            throw TreeBuildException(
                tokenIter.getCurrentLine(),
                tokenIter.getCurrentCol(),
                tk.toString())

        value = tk.value
        base = Utils.decodeByteBase(value)

        if(base > 32){
            throw ByteBaseOverflowException(base, value)
        }

        numberLine = tokenIter.getCurrentLine()
        numberCh = tokenIter.getCurrentCol()
        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Literal \"%d\"\n", " ".repeat(PRINT_INTENT * depth), value))
        super.print(depth + 1)
    }

    fun getValue(): Int {
        return value
    }

    fun getBase(): Int {
        return base
    }
}