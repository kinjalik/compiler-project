package org.example.Generator

import com.sun.source.tree.Tree
import org.example.Functions.BuiltIns
import org.example.Functions.UserDefined
import org.example.Memory.VirtualStackHelper
import org.example.Utils.Utils.decToHex
import org.example.ast.AtomTreeNode
import org.example.ast.ListTreeNode
import org.example.ast.LiteralTreeNode
import org.example.ast.TreeNode


object CodeGenerator {

    private var addressLength: Int = 0
    private var frameServiceAtoms: Int = 0
    private var opcodes: OpcodeList = OpcodeList(1)
    private lateinit var ast: TreeNode

    fun init(ast: TreeNode, addressLength: Int = 32, frameServiceAtoms: Int = 3) {
        this.addressLength = addressLength
        this.frameServiceAtoms = frameServiceAtoms

        assert(addressLength in 1..32)
        assert(frameServiceAtoms >= 2)

        opcodes = OpcodeList(addressLength)
        this.ast = ast

        VirtualStackHelper.init(addressLength, frameServiceAtoms)
        VirtualStackHelper.initStack(opcodes)

        SpecialForm.init(addressLength)

        BuiltIns.setAddrLen(addressLength)

        UserDefined.setAddrLen(addressLength)
        UserDefined.setServiceFrames(frameServiceAtoms)
    }

    fun run() {
        opcodes.add("PUSH", decToHex(0, 2 * addressLength))
        val jump_to_prog_start_i = opcodes.size() - 1
        opcodes.add("JUMP")

        for (el in ast.childNodes) {
            val context = Context(frameServiceAtoms)

            if (el.childNodes[0] is AtomTreeNode && (el.childNodes[0] as AtomTreeNode).getValue() == "prog") {
                context.isProg = true
                opcodes.add("JUMPDEST")
                opcodes.list[jump_to_prog_start_i].__extraValue = decToHex(
                    opcodes.list.last().id,
                    2 * addressLength
                )

                opcodes.add("PUSH", decToHex(0, 2 * addressLength))
                val prog_atom_count = opcodes.size() - 1
                VirtualStackHelper.loadCurAtomCounterAddr(opcodes)
                opcodes.add("MSTORE")

                processCodeBlock(el.childNodes[1], context, opcodes)

                opcodes.list[prog_atom_count].__extraValue = decToHex(context.idCounter - frameServiceAtoms, 2 * addressLength)
            } else {
                declareFunction(el, context, opcodes)
            }
        }
    }

    fun getStr(): String {
        return opcodes.get_str()
    }

    fun processCodeBlock(progBody: TreeNode, ctx: Context, opcodes: OpcodeList) {
        for (call in progBody.childNodes) {
            processCall(call, ctx, opcodes)
        }
    }

    fun processCall(callBody: TreeNode, ctx: Context, opcodes: OpcodeList) {
        when (callBody) {
            is LiteralTreeNode -> return processLiteral(callBody, opcodes)
            is AtomTreeNode -> return processAtom(callBody, ctx, opcodes)
            is ListTreeNode -> {
                if (callBody.childNodes[0] is ListTreeNode) {
                    return processCodeBlock(callBody, ctx, opcodes)
                }
            }
        }
        val name = (callBody.childNodes[0] as AtomTreeNode).getValue()

        if (SpecialForm.has(name)) {
            return SpecialForm.call(callBody, ctx, opcodes)
        }

        for (i in 1 until callBody.childNodes.size) {
            processCall(callBody.childNodes[i], ctx, opcodes)
        }

        if (BuiltIns.has(name)) {
            return BuiltIns.call(callBody, ctx, opcodes)
        }

        if (UserDefined.has(name)) {
            return UserDefined.call(callBody, ctx, opcodes)
        }

    }

    fun processLiteral(callBody: TreeNode, opcodes: OpcodeList) {
        assert(callBody is LiteralTreeNode)
        val value = decToHex((callBody as LiteralTreeNode).getValue(), 2 * addressLength)
        opcodes.add("PUSH", value)
    }

    fun processAtom(callBody: TreeNode, ctx: Context, opcodes: OpcodeList) {
        assert(callBody is AtomTreeNode)
        val atomName = (callBody as AtomTreeNode).getValue()
        val (atomAddress, isNew) = ctx.getAtomAddr(atomName)
        VirtualStackHelper.loadAtomValue(opcodes, atomAddress)
    }

    fun declareFunction(callBody: TreeNode, ctx: Context, opcodes: OpcodeList) {
        assert(callBody is ListTreeNode)
        assert(callBody.childNodes[0] is LiteralTreeNode || callBody.childNodes[0] is AtomTreeNode)
        assert((callBody.childNodes[0] as AtomTreeNode).getValue() == "func")

        opcodes.add("JUMPDEST")
        UserDefined.add((callBody.childNodes[1] as AtomTreeNode).getValue(), opcodes.list.last().id)

        VirtualStackHelper.addFrame(opcodes)

        opcodes.add("PUSH")
        val funcAtomCounter = opcodes.list.size - 1
        VirtualStackHelper.loadCurAtomCounterAddr(opcodes)
        opcodes.add("MSTORE")

        for (argName in callBody.childNodes[2].childNodes.reversed()) {
            assert(argName is AtomTreeNode)
            val (address, isNew) = ctx.getAtomAddr((argName as AtomTreeNode).getValue())
            VirtualStackHelper.storeAtomValue(opcodes, address)
        }

        processCall(callBody.childNodes[3], ctx, opcodes)

        opcodes.list[funcAtomCounter].__extraValue =
            decToHex(ctx.idCounter - frameServiceAtoms, 2 * addressLength)

        VirtualStackHelper.loadBackAddress(opcodes)
        VirtualStackHelper.removeFrame(opcodes)
        opcodes.add("JUMP")
    }
}