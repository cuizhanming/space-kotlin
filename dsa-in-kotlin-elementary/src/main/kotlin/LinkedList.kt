package com.cuizhanming.spring

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
//    "creating and linking nodes" example {
//        val node1 = Node(1)
//        val node2 = Node(2)
//        val node3 = Node(3)
//        node1.next = node2
//        node2.next = node3
//        println(node1)
//    }
//
//    "push" example {
//        val list = LinkedList<Int>()
//        list.push(3).push(2).push(1)
//        println(list)
//    }
//
//    "append" example {
//        val list = LinkedList<Int>()
//        list.append(3).append(2).append(1)
//        println(list)
//    }
//
//    "inserting at a particular index" example {
//        val list = LinkedList<Int>()
//        list.push(3)
//        list.push(2)
//        list.push(1)
//        println("Before inserting: $list")
//        for (i in 1..3) {
//            list.insert(-1 * i, i)
//        }
//        println("After inserting: $list")
//    }
//
//    "pop" example {
//        val list = LinkedList<Int>()
//        list.push(3)
//        list.push(2)
//        list.push(1)
//        println("Before popping list: $list")
//        val poppedValue = list.pop()
//        println("After popping list: $list")
//        println("Popped value: $poppedValue")
//    }
//
//    "removing the last node" example {
//        val list = LinkedList<Int>()
//        list.push(3)
//        list.push(2)
//        list.push(1)
//        println("Before removing last node: $list")
//        val removedValue = list.removeLast()
//        println("After removing last node: $list")
//        println("Removed value: $removedValue")
//    }
//
//    "removing a node after a particular node" example {
//        val list = LinkedList<Int>()
//        list.push(3)
//        list.push(2)
//        list.push(1)
//        println("Before removing at particular index: $list")
//        val index = 1
//        val node = list.nodeAt(index - 1)!!
//        val removedValue = list.removeAfter(node)
//        println("After removing at index $index: $list")
//        println("Removed value: $removedValue")
//    }
//
//    "printing doubles" example {
//        val list = LinkedList<Int>()
//        list.push(3)
//        list.push(2)
//        list.push(1)
//        println(list)
//        for (item in list) {
//            println("Double: ${item * 2}")
//        }
//    }
//    "removing elements" example {
//        val list: MutableCollection<Int> = LinkedList()
//        list.add(3)
//        list.add(2)
//        list.add(1)
//        println(list)
//        list.remove(1)
//        println(list)
//    }
//    "retaining elements" example {
//        val list: MutableCollection<Int> = LinkedList()
//        list.add(3)
//        list.add(2)
//        list.add(1)
//        list.add(4)
//        list.add(5)
//        println(list)
//        list.retainAll(listOf(3, 4, 5))
//        println(list)
//    }
    "remove all elements" example {
        val list: MutableCollection<Int> = LinkedList()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)
        println(list)
        list.removeAll(listOf(3, 4, 5))
        println(list)
    }

    "print in reverse" example {
        val list = LinkedList<Int>()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)
        println(list)
        list.printInReverse()
    }

    "print middle" example {
        val list = LinkedList<Int>()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)
        println(list)
        println(list.getMiddle()?.value)
    }

    "reverse list" example {
        val list = LinkedList<Int>()
        list.add(3)
        list.add(2)
        list.add(1)
        list.add(4)
        list.add(5)
        println("Original: $list")
        println("Reversed: ${list.reversed()}")
    }

    "merge lists" example {
        val list = LinkedList<Int>()
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        val other = LinkedList<Int>()
        other.add(-1)
        other.add(0)
        other.add(2)
        other.add(2)
        other.add(7)
        println("Left: $list")
        println("Right: $other")
        println("Merged: ${list.mergeSorted(other)}")
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

class LinkedListIterator<T>(private val list: LinkedList<T>) : Iterator<T>, MutableIterator<T> {
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

    override fun remove() {
        // The reason why here is index == 1, and index-2 ?
        // because the lastNode is the next of the previous node, so we need to remove the next of the previous node.
        // why not index == 0 or index-1?
        // because when iterator.next() is called, the index is increased by 1 now. The current node's index is actually index-1.
        // therefore it's previous node's index is index-2.
        if (index == 1) {
            list.pop()
        } else {
            val prevNode = list.nodeAt(index - 2) ?: return
            list.removeAfter(prevNode)
            lastNode = prevNode
        }
        index--
    }

}

class LinkedList<T> : Iterable<T>, Collection<T>, MutableIterable<T>, MutableCollection<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    override var size = 0
        private set

    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            append(element)
        }
        return true
    }

    override fun add(element: T): Boolean {
        append(element)
        return true
    }

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

    override fun iterator(): MutableIterator<T> {
        return LinkedListIterator(this)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (!elements.contains(item)) {
                iterator.remove()
                result = true
            }
        }
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        for (item in elements) {
            result = remove(item) || result
        }
        return result
    }

    override fun remove(element: T): Boolean {
       val iterator = iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (item == element) {
                iterator.remove()
                return true
            }
        }
        return false
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

fun <T> LinkedList<T>.printInReverse() {
    this.nodeAt(0)?.printInReverse()
}

fun <T> Node<T>.printInReverse() {
    this.next?.printInReverse()
    if (this.next != null) {
        print(" -> ")
    }
    print(this.value.toString())
}

fun <T> LinkedList<T>.getMiddle(): Node<T>? {
    var slow = this.nodeAt(0)
    var fast = this.nodeAt(0)

    while (fast != null) {
        fast = fast.next
        if (fast != null) {
            fast = fast.next
            slow = slow?.next
        }
    }
    return slow
}

fun <T> LinkedList<T>.addInReverse(list: LinkedList<T>, node: Node<T>) {
    if (node.next != null) {
        addInReverse(list, node.next!!)
    }
    list.append(node.value)
}

fun <T> LinkedList<T>.reversed(): LinkedList<T> {
    val result = LinkedList<T>()
    if (isEmpty()) {
        return result
    }
    addInReverse(result, this.nodeAt(0)!!)
    return result
}

fun <T : Comparable<T>> LinkedList<T>.mergeSorted(otherList: LinkedList<T>): LinkedList<T> {
    if (this.isEmpty()) {
        return otherList
    }

    if (otherList.isEmpty()) {
        return this
    }

    var result = LinkedList<T>()

    var leftNode = this.nodeAt(0)
    var rightNode = otherList.nodeAt(0);

    while (leftNode != null && rightNode != null) {
        if (leftNode.value < rightNode.value) {
            leftNode = append(result, leftNode)
        } else {
            rightNode = append(result, rightNode)
        }
    }

    while (leftNode != null) {
        leftNode = append(result, leftNode)
    }

    while (rightNode != null) {
        rightNode = append(result, rightNode)
    }

    return result;


}


private fun <T : Comparable<T>> append(result: LinkedList<T>, node: Node<T>): Node<T>? {
    result.append(node.value)
    return node.next
}