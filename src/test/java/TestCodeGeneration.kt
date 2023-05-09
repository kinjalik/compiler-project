import Providers.ExampleProgramArgumentProvider
import org.example.Generator.CodeGenerator
import org.example.Generator.CounterOpcode
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.buildAst
import org.example.tokens.Scanner
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.nio.file.Files
import java.nio.file.Path


internal class TestCodeGeneration {
    @ParameterizedTest
    @ArgumentsSource(ExampleProgramArgumentProvider::class)
    fun `Just build`(filePath: String) {
        val path = Path.of(filePath)
        val originalCode = Files.readString(path)

        val scanner = Scanner(originalCode)
        val ast = buildAst(scanner)

        ast.print()

        val semanticAnalyzer = SemanticAnalyzer(ast)
        semanticAnalyzer.run()

        CounterOpcode.reset()
        CodeGenerator.init(ast)
        CodeGenerator.run()

        println("\n===ORIGINAL START===")
        print(originalCode)
        print("\n===ORIGINAL END===\n")

        println("\n===OPCODE START===")
        print(CodeGenerator.getStr())
        print("\n===OPCODE END===\n")
        println("\n\n")
    }
}
