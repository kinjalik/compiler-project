
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.TreeNode
import org.example.ast.buildAst
import org.example.tokens.Scanner
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

internal class AllErrorInSemanticAnalyzerTest {
    val files = listOf(
        "example0.fs",
        "example1.fs",
        "example2.fs",
        "example3.fs",
        "example4.fs",
        "example5.fs",
        "example6.fs",
        "example7.fs",
        "example8.fs",
        "example9.fs",
        "example10.fs",
        "example11.fs",
        "example12.fs",
        "overflow.fs",
    )

    fun create_sample(filePath: String): TreeNode {
        val originalCode = this::class.java.getResource("incorrect_example_semantic_analyzer/" + filePath).readText().replace('\r', ' ')

        val scanner = Scanner(originalCode)
        scanner.isOk()

        return buildAst(scanner)
    }

    @Test
    fun `Unexpected token "prog" on the current program level`() {
        val num_test = 0
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Unexpected token \"prog\" on the current program level" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Multiple definitions of "prog" found`() {
        val num_test = 1
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Multiple definitions of \"prog\" found" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Token "prog" not found`() {
        val num_test = 2
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Token \"prog\" not found" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Multiple declarations of function "$name" found`() {
        val num_test = 3
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Multiple declarations of function" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `"func" declaration keyword should be first in the list`() {
        val num_test = 4
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("\"func\" declaration keyword should be first in the list" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Nested "func" is not allowed`() {
        val num_test = 5
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Nested \"func\" is not allowed" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Unknown error in function declaration`() {
        val num_test = 6
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Unknown error in function declaration" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `"setq" operation should be first in the list`() {
        val num_test = 7
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("\"setq\" operation should be first in the list" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Unknown error in usage setq`() {
        val num_test = 8
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Unknown error in usage setq" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Call of "cond" requires 2 or 3 arguments but ${arguments} were provided`() {
        val num_test = 9
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Call of \"cond\" requires 2 or 3 arguments" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Call of "${atomName}" requires $size arguments but ${arguments} were provided`() {
        val num_test = 10
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Call of" in exception.message.toString())
        assertTrue("requires" in exception.message.toString())
        assertTrue("arguments but" in exception.message.toString())
        assertTrue("were provided" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Function "${atomName}" should be the first element of the list`() {
        val num_test = 11
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Function" in exception.message.toString())
        assertTrue("should be the first element of the list" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Object with name "${atomName}" do not exist`() {
        val num_test = 12
        val ast = create_sample(files[num_test])
        val exception = assertFailsWith<Exception> {
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }
        assertTrue("Object with name" in exception.message.toString())
        assertTrue("do not exist" in exception.message.toString())
        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }

    @Test
    fun `Overflow`() {
        val num_test = 13
        val exception = assertFailsWith<Exception> {
            val ast = create_sample(files[num_test])
            val semanticAnalyzer = SemanticAnalyzer(ast)
            semanticAnalyzer.run()
        }

        println("File name: ${files[num_test]}")
        println("${exception.message}\n")
    }
}


