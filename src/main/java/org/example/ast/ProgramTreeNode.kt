package org.example.ast

import org.example.tokens.Token

class ProgramTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Iterator<Token>): TreeNode {
        while (tokenIter.hasNext()) {
            childNodes += ElementTreeNode().parse(token, tokenIter)
        }

        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Program\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth + 1)
    }
}