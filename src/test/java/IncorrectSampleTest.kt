
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.TreeNode
import org.example.ast.buildAst
import org.example.tokens.Scanner
import kotlin.test.Test
import kotlin.test.assertFailsWith

internal class IncorrectSampleTest {

    val files_prog = listOf(
        "many_prog.fs",
        "nested_prog.fs",
        "non_existing_prog.fs",
    )

    val files_function = listOf(
        "non_existing_function.fs",
        "non_existing_signature.fs",
        "same_functions.fs"
    )

    val files_var = listOf(
        "variable_not_exist.fs",
    )
    fun create_sample(filePath: String) : TreeNode {
        val originalCode = this::class.java.getResource(filePath).readText().replace('\r', ' ')

        val scanner = Scanner(originalCode)
        scanner.isOk()

        return buildAst(scanner)
    }
    @Test
    fun `Test failed prog`() {
        for(i in files_prog) {
            val ast = create_sample("incorrect_examples_prog/$i")

            val exception = assertFailsWith<Exception> {
                val semanticAnalyzer = SemanticAnalyzer(ast)
                semanticAnalyzer.run()
                semanticAnalyzer.check_prog()
            }
            println("FileName: $i")
            println(exception.message)
            println()
        }
    }

    @Test
    fun `Test failed fun`() {
        for(i in files_function) {
            val ast = create_sample("incorrect_examples_function/$i")
//            ast.print()
            val exception = assertFailsWith<Exception> {
                val semanticAnalyzer = SemanticAnalyzer(ast)
                semanticAnalyzer.run()
                semanticAnalyzer.check_prog()
            }
            println("FileName: $i")
            println(exception.message)
            println()
        }
    }

    @Test
    fun `Test variable failed`() {
        for(i in files_var) {
            val ast = create_sample("incorrect_examples_var/$i")

            val exception = assertFailsWith<Exception> {
                val semanticAnalyzer = SemanticAnalyzer(ast)
                semanticAnalyzer.run()
                semanticAnalyzer.check_prog()
            }
            println("FileName: $i")
            println(exception.message)
            println()
        }
    }
}