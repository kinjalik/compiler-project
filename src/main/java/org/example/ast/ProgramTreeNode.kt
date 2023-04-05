package org.example.ast

import org.example.tokens.Token
import java.lang.Exception

class ProgramTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Iterator<Token>): TreeNode {
        var flag = false
        try {
            while (tokenIter.hasNext()) {
                if (!flag) {
                    val temp = ElementTreeNode().parse(token, tokenIter)
                    if (temp.childNodes.isEmpty()) continue
                    childNodes += temp
                    flag = true
                } else {
                    val temp = ElementTreeNode().parse(tokenIter.next(), tokenIter)
                    if (temp.childNodes.isEmpty()) continue
                    childNodes += temp
                }
            }
        } catch(e: Exception) {
            return this
        }

        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Program\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth + 1)
    }
}