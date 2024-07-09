package com.cuizhanming.spring

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    "creating and linking nodes" example {
        val node1 = Node(1)
        val node2 = Node(2)
        val node3 = Node(3)
        node1.next = node2
        node2.next = node3
        println(node1)
    }

    "push" example {
        val list = LinkedList<Int>()
        list.push(3).push(2).push(1)
        println(list)
    }

    "append" example {
        val list = LinkedList<Int>()
        list.append(3).append(2).append(1)
        println(list)
    }

    "inserting at a particular index" example {
        val list = LinkedList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        println("Before inserting: $list")
        for (i in 1..3) {
            list.insert(-1 * i, i)
        }
        println("After inserting: $list")
    }

    "pop" example {
        val list = LinkedList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        println("Before popping list: $list")
        val poppedValue = list.pop()
        println("After popping list: $list")
        println("Popped value: $poppedValue")
    }

    "removing the last node" example {
        val list = LinkedList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        println("Before removing last node: $list")
        val removedValue = list.removeLast()
        println("After removing last node: $list")
        println("Removed value: $removedValue")
    }

    "removing a node after a particular node" example {
        val list = LinkedList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        println("Before removing at particular index: $list")
        val index = 1
        val node = list.nodeAt(index - 1)!!
        val removedValue = list.removeAfter(node)
        println("After removing at index $index: $list")
        println("Removed value: $removedValue")
    }

    "printing doubles" example {
        val list = LinkedList<Int>()
        list.push(3)
        list.push(2)
        list.push(1)
        println(list)
        for (item in list) {
            println("Double: ${item * 2}")
        }
    }
}

infix fun String.example(function: () -> Unit) {
    println("--- Example of $this ---")
    function()
    println()
}

data class Node<T> (var value: T, var next: Node<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> ${next.toString()}"
        } else {
            "$value"
        }
    }
}

class LinkedListIterator<T>(private val list: LinkedList<T>) : Iterator<T> {
    private var index = 0
    private var lastNode: Node<T>? = null

    override fun hasNext(): Boolean {
        return index < list.size
    }

    override fun next(): T {
        if (index >= list.size) throw IndexOutOfBoundsException()

        lastNode =
            if (index == 0) {
                list.nodeAt(0)
            } else {
                lastNode?.next
            }

        index++
        return lastNode!!.value
    }

}

class LinkedList<T> : Iterable<T>, Collection<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    override var size = 0
        private set

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            if (!contains(element)) return false
        }
        return true
    }

    override fun contains(element: T): Boolean {
        for (item in this) {
            if (element == item) return true
        }
        return false
    }

    override fun iterator(): Iterator<T> {
        return LinkedListIterator(this)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun toString(): String {
        return if (isEmpty()) {
             "Empty list"
        } else {
             head.toString()
        }
    }

    fun push(value: T): LinkedList<T> {
        head = Node(value, head)
        if (tail == null) {
            tail = head
        }
        size++
        return this
    }

    fun append(value: T): LinkedList<T> {
        if (isEmpty()) {
            push(value)
            return this
        }
        tail?.next = Node(value)
        tail = tail?.next
        size++
        return this
    }

    fun insert(value: T, index: Int) {
        val afterNode = nodeAt(index)
        insert(value, afterNode)
    }

    fun insert(value: T, afterNode: Node<T>?): Node<T> {
        if (tail == afterNode) {
            append(value)
            return tail!!
        }
        val newNode = Node(value, afterNode?.next)
        afterNode?.next = newNode
        size++
        return newNode
    }

    fun nodeAt(index: Int): Node<T>? {
        var currentNode = head
        var currentIndex = 0

        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.next
            currentIndex++
        }
        return currentNode
    }

    fun pop(): T? {
        if (!isEmpty()) size--
        val result = head?.value
        head = head?.next
        if (isEmpty()) {
            tail = null
        }
        return result
    }

//    fun removeLast(): T? {
//        if (!isEmpty()) size--
//        val result = tail?.value
//        tail = nodeAt(size-1)
//        tail?.next = null
//        return result
//    }

    fun removeLast(): T? {
        // 1 if null
        val head = head ?: return null
        // 2 if one
        if (head.next == null) return pop()

        // 3 if more than one
        size--

        var prev = head
        var current = head
        // iterating to the last, which will be current
        var next = current.next
        while (next != null) {
            prev = current
            current = next
            next = current.next // iterate to the last that has no next link
        }
        // now disconnect the current from the link, by removing prev.next link to null
        prev.next = null

        // make sure tail also updated
        tail = prev

        return current.value
    }

    fun removeAfter(node: Node<T>): T? {
        // remove current node's next
        val result = node.next?.value

        // check if tail need resign, only if it's tail removed
        if (tail == node.next) {
            tail = node
        }

        // check if the list size changed
        if (node.next != null) {
            size--
        }

        // Now resign current node's next to the one after next.
        node.next = node.next?.next
        return result
    }
}