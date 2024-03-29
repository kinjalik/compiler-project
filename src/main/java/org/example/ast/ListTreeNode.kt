package org.example.ast

import org.example.tokens.*

class ListTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Scanner): TreeNode {
        assert(token is ParenthesisToken)
        while (tokenIter.hasNext()) {
            val tk = tokenIter.next()
            if (tk is PhantomToken)
                continue

            if (tk is ParenthesisToken && tk.type == ParenthesisType.RIGHT) {
                break
            } else {
                val temp = ElementTreeNode().parse(tk, tokenIter)
                childNodes += temp
            }
        }
        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- List\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth + 1)
    }
}