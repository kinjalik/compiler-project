package org.example.Semantic

import java.util.*
import kotlin.collections.ArrayDeque

object StackVar {
    val stack = ArrayDeque<TreeSet<String>>()
    fun add_elem(elem: String){
        stack.last().add(elem)
    }

    fun new_stack() {
        stack.addLast(TreeSet<String>())
    }

    fun remove_last_stack() {
        stack.removeLast()
    }

    fun contain(elem: String) : Boolean {
        var is_find = false
        for(i in stack) {
            if (i.isEmpty()) continue
            is_find = is_find || i.contains(elem)
        }
        return is_find
    }

    fun clear() {
        stack.clear()
    }
}