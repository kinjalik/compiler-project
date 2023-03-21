import org.example.ast.ProgramTreeNode
import org.example.ast.buildAst
import org.example.tokens.Scanner
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import java.nio.file.Files
import java.nio.file.Path

internal class AstIntegrationTest {
    @ParameterizedTest
    @ArgumentsSource(ExampleProgramArgumentProvider::class)
    fun `ast tree building test`(filePath: String) {
        val path = Path.of(filePath)
        val originalCode = Files.readString(path)

        val scanner = Scanner(originalCode)
        val ast = buildAst(scanner)

        ast.print()
    }
}
