package org.example.ast

import org.example.exceptions.ByteBaseOverflowException
import org.example.exceptions.SemanticAnalysisException
import org.example.exceptions.TreeBuildException
import org.example.tokens.Scanner
import org.example.tokens.Token

class ProgramTreeNode : TreeNode() {
    override fun parse(token: Token, tokenIter: Scanner): TreeNode {
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
        } catch(e: NumberFormatException) {
            throw Exception("Big number");
        } catch(e: SemanticAnalysisException) {
            throw e
        } catch(e: TreeBuildException) {
            throw e
        } catch(e: ByteBaseOverflowException) {
            throw e
        }
        catch(e: Exception) {
            return this
        }

        return this
    }

    override fun print(depth: Int) {
        print(String.format("%s- Program\n", " ".repeat(PRINT_INTENT * depth)))
        super.print(depth + 1)
    }
}