package org.example.ast

abstract class AccessibleTreeNode<T>: TreeNode()
{
    abstract var value: T

    public fun getValue(): T{
        return value
    }
}