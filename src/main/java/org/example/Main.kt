package org.example

import org.example.Generator.CodeGenerator
import org.example.Generator.CounterOpcode
import org.example.Semantic.SemanticAnalyzer
import org.example.ast.buildAst
import org.example.tokens.Scanner
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args.size != 1 && args.size != 2) {
        throw Exception("java -jar <*.jar> <*.fs> <bytecode.output> (optional)")
    }
    try {
        val path = Path.of(args[0])
        val originalCode = Files.readString(path)

        val scanner = Scanner(originalCode)
        val ast = buildAst(scanner)
        scanner.isOk()

        val semanticAnalyzer = SemanticAnalyzer(ast)
        semanticAnalyzer.run()

        CounterOpcode.reset()
        CodeGenerator.init(ast)
        CodeGenerator.run()

        println("Success")
    } catch (e: Exception) {
        println(e.message)
        return
    }

    val fileToWrite = if (args.size == 2) {
        args[1]
    } else {
        "bytecode"
    }
    File(fileToWrite).writeText(CodeGenerator.getStr())
}