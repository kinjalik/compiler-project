package org.example.Memory
import org.example.Generator.OpcodeList
import org.example.Utils.Utils.decToHex

object VirtualStackHelper {
    private var __address_length = 0
    private var __frame_service_atoms = 0

    fun init(address_length: Int, frame_service_atoms: Int) {
        this.__address_length = address_length
        this.__frame_service_atoms = frame_service_atoms
    }

    fun initStack(opcodes: OpcodeList) {
        opcodes.add("PUSH", decToHex(0x40, 2 * this.__address_length))
        opcodes.add("PUSH", decToHex(0, 2 * this.__address_length))
        opcodes.add("MSTORE")

        opcodes.add("PUSH", decToHex(0x0, 2 * this.__address_length))
        opcodes.add("DUP1")
        opcodes.add("PUSH", decToHex(0x40, 2 * this.__address_length))
        opcodes.add("MSTORE")
        opcodes.add("PUSH", decToHex(0x40 + 0x40, 2 * this.__address_length))
        opcodes.add("MSTORE")

        opcodes.add("PUSH", decToHex(0x0, 2 * this.__address_length))
        opcodes.add("PUSH", decToHex(0x40 + 0x20, 2 * this.__address_length))
        opcodes.add("MSTORE")
    }

    fun storeAtomValue(opcodes: OpcodeList, atom_address: Int) {
        this.loadAtomAddress(opcodes, atom_address)
        opcodes.add("MSTORE")
    }

    fun loadAtomAddress(opcodes: OpcodeList, atom_address: Int) {
        this.loadCurGap(opcodes)
        opcodes.add("PUSH", decToHex(atom_address, 2 * this.__address_length))
        opcodes.add("ADD")
    }

    fun loadAtomValue(opcodes: OpcodeList, atom_address: Int) {
        this.loadAtomAddress(opcodes, atom_address)
        opcodes.add("MLOAD")
    }

    fun loadCurGap(opcodes: OpcodeList) {
        opcodes.add("PUSH", decToHex(0, 2 * this.__address_length))
        opcodes.add("MLOAD")
    }

    fun storeNewGap(opcodes: OpcodeList) {
        opcodes.add("PUSH", decToHex(0, 2 * this.__address_length))
        opcodes.add("MSTORE")
    }

    fun loadPrevGap(opcodes: OpcodeList) {
        this.loadCurGap(opcodes)
        opcodes.add("MLOAD")
    }

    fun loadCurAtomCounterAddr(opcodes: OpcodeList) {
        this.loadCurGap(opcodes)
        opcodes.add("PUSH", decToHex(0x20, 2 * this.__address_length))
        opcodes.add("ADD")
    }

    fun loadCurAtomCounter(opcodes: OpcodeList) {
        this.loadCurAtomCounterAddr(opcodes)
        opcodes.add("MLOAD")
    }

    fun loadBackAddressAddr(opcodes: OpcodeList) {
        this.loadCurGap(opcodes)
        opcodes.add("PUSH", decToHex(0x40, 2 * this.__address_length))
        opcodes.add("ADD")
    }

    fun loadBackAddress(opcodes: OpcodeList) {
        this.loadBackAddressAddr(opcodes)
        opcodes.add("MLOAD")
    }

    fun storeBackAddress(opcodes: OpcodeList) {
        this.loadBackAddressAddr(opcodes)
        opcodes.add("MSTORE")
    }

    fun calcCurFrameSize(opcodes: OpcodeList) {
        opcodes.add("PUSH", decToHex(this.__frame_service_atoms * 0x20, 2 * this.__address_length))

        this.loadCurAtomCounter(opcodes)
        opcodes.add("PUSH", decToHex(32, 2 * this.__address_length))
        opcodes.add("MUL")

        opcodes.add("ADD")
    } 
    fun calcNewFrameGap(opcodes: OpcodeList) {
        this.loadCurGap(opcodes)
        this.calcCurFrameSize(opcodes)
        opcodes.add("ADD")
    }

    fun addFrame(opcodes: OpcodeList) {
        this.loadCurGap(opcodes)
        this.calcNewFrameGap(opcodes)
        opcodes.add("MSTORE")

        this.calcNewFrameGap(opcodes)
        this.storeNewGap(opcodes)

        this.storeBackAddress(opcodes)
    }

    fun removeFrame(opcodes: OpcodeList) {
        this.loadPrevGap(opcodes)

        this.storeNewGap(opcodes)
    }
}