import org.example.Generator.CodeGenerator
import org.example.Generator.CounterOpcode
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.buildAst
import org.example.tokens.Scanner
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Exception("Pass the *.fs file")
    }
    val path = Path.of(args[0])
    val originalCode = Files.readString(path)

    val scanner = Scanner(originalCode)
    val ast = buildAst(scanner)

    val semanticAnalyzer = SemanticAnalyzer(ast)
    semanticAnalyzer.run()

    CounterOpcode.reset()
    CodeGenerator.init(ast)
    CodeGenerator.run()

    File("src/test/nodejs/prog.fs").writeText(CodeGenerator.getStr())
}