package org.example.ast

import org.example.exceptions.TreeBuildException
import org.example.tokens.AtomToken
import org.example.tokens.PhantomToken
import org.example.tokens.Scanner
import org.example.tokens.Token

class AtomTreeNode : TreeNode() {
    private var value: String = ""
    val atomNameRegex = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")

    override fun parse(token: Token, tokenIter: Scanner): TreeNode {
        var tk = token
        while (tk is PhantomToken)
            tk = tokenIter.next()
        if (tk !is AtomToken)
            throw TreeBuildException(
                tokenIter.getCurrentLine(),
                tokenIter.getCurrentCol(),
                tk.toString())

        value = tk.value
        if (!value.matches(atomNameRegex)) {
            throw
            TreeBuildException(tokenIter.getCurrentLine(), tokenIter.getCurrentCol(), value)
        }


        numberLine = tokenIter.getCurrentLine()
        numberCh = tokenIter.getCurrentCol()
        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Atom \"%s\"\n", " ".repeat(PRINT_INTENT * depth), value))
        super.print(depth + 1)
    }

    fun getValue(): String {
        return value
    }
}