package org.example.ast

import org.example.tokens.Scanner

fun buildAst(scanner: Scanner) = ProgramTreeNode().parse(scanner.next(), scanner)