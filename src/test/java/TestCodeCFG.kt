
import org.example.CFG.CFG
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.buildAst
import org.example.tokens.Scanner
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.nio.file.Files
import java.nio.file.Path


internal class TestCodeCFG {
    @ParameterizedTest
    @ArgumentsSource(ExampleProgramArgumentProviderToCompile::class)
    fun `CFG build`(filePath: String) {
        val path = Path.of(filePath)
        val originalCode = Files.readString(path)

        val scanner = Scanner(originalCode)
        scanner.isOk()
        val ast = buildAst(scanner)


        val semanticAnalyzer = SemanticAnalyzer(ast)
        semanticAnalyzer.run()

        val cfg = CFG(ast)
        cfg.run()

        (0..40).forEach{ _ -> print("=") }
        println()
        print(originalCode)
        println()
//
//        (0..40).forEach{ _ -> print("=") }
//        println()
//        ast.print()
//        println()

        (0..40).forEach{ _ -> print("=") }
        println()
        cfg.print()
        println()

    }
}
