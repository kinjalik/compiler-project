import org.example.CFG.AllBranchHaveReturn
import org.example.CFG.CFG
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.TreeNode
import org.example.ast.buildAst
import org.example.tokens.Scanner
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class ErrorInCFGAnalyzerTest {
    val files = listOf(
        "iHaveNotReturn.fs",
    )

    fun create_sample(filePath: String): TreeNode {
        val originalCode = this::class.java.getResource("incorrect_example_for_cfg/" + filePath).readText().replace('\r', ' ')

        val scanner = Scanner(originalCode)
        scanner.isOk()

        return buildAst(scanner)
    }

    @Test
    fun `return is needed`() {
        val num_test = 0
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()

            val cfg = CFG(ast)
            cfg.run()

            AllBranchHaveReturn(cfg=cfg).run()
        }
    }

}


