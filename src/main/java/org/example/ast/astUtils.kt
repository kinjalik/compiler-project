package org.example.ast

import org.example.tokens.Token

fun buildAst(scanner: Iterator<Token>) = ProgramTreeNode().parse(scanner.next(), scanner)