package com.wardellbagby.cachingsequence

import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.LongStream
import java.util.stream.Stream
import kotlin.coroutines.experimental.SequenceBuilder
import kotlin.coroutines.experimental.buildIterator

/**
 * A sequence that caches values yielded by itself and allows subscripting.
 *
 * Subscripting an index that has already been emitted will return a cached value that does not
 * reiterate through the initial iterator.
 *
 * Subscripting will consume values from the iterator. The iterator will not be reset when fully
 * exhausted.
 *
 * @author Wardell Bagby
 */
open class CachingSequence<out T>(sequence: Sequence<T>) : Sequence<T> {

    private val yieldedItems: MutableList<T> = mutableListOf()
    private val cachingIterator = CachingIterator(sequence.iterator())

    override fun iterator(): Iterator<T> {
        return cachingIterator
    }

    operator fun get(index: Int): T {
        if (index < 0) throw IndexOutOfBoundsException("Index $index is not in range of this sequence.")
        if (index < yieldedItems.size) {
            return yieldedItems[index]
        }
        var currentIndex = yieldedItems.size
        do {
            val currentValue = cachingIterator.next()
            if (currentIndex == index) {
                return currentValue
            }
            currentIndex += 1
        } while (cachingIterator.hasNext() && currentIndex <= index)
        throw IndexOutOfBoundsException("Index $index is not in range of this sequence.")
    }

    private inner class CachingIterator(private val iterator: Iterator<T>) : Iterator<T> {
        private var currentIndex = 0
        override fun hasNext() = iterator.hasNext()

        override fun next(): T {
            val currentValue = iterator.next()
            yieldedItems.add(currentIndex++, currentValue)
            return currentValue
        }

    }
}

/**
 * Returns an empty caching sequence.
 */
fun <T> emptyCachingSequence(): CachingSequence<T> = CachingSequence(emptySequence())

/**
 * Given an [iterator] function, constructs a [CachingSequence] that returns values through the
 * [Iterator] provided by that function.
 * The values are evaluated lazily, and the sequence is potentially infinite.
 */
@Suppress("FunctionName") //Matches the Sequence function.
inline fun <T> CachingSequence(crossinline iterator: () -> Iterator<T>) = CachingSequence(Sequence(iterator))

/**
 * Creates a [CachingSequence] that returns the specified values.
 */
fun <T> cachingSequenceOf(vararg elements: T): CachingSequence<T> = if (elements.isEmpty()) emptyCachingSequence() else elements.asCachingSequence()

/**
 * Builds a [CachingSequence] lazily yielding values one by one.
 */
fun <T> buildCachingSequence(builderAction: suspend SequenceBuilder<T>.() -> Unit): CachingSequence<T> = CachingSequence { buildIterator(builderAction) }

/**
 * Creates a sequence that returns all elements from this iterator.
 */
fun <T> Iterator<T>.asCachingSequence(): CachingSequence<T> = CachingSequence { this }

/**
 * Creates a sequence that returns all elements from this iterable.
 */
fun <T> Iterable<T>.asCachingSequence(): CachingSequence<T> = CachingSequence { this.iterator() }

/**
 * Creates a caching sequence that returns all values from this enumeration.
 */
fun <T> java.util.Enumeration<T>.asCachingSequence(): CachingSequence<T> = this.iterator().asCachingSequence()

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun <T> Array<out T>.asCachingSequence(): CachingSequence<T> {
    if (isEmpty()) return emptyCachingSequence()
    return CachingSequence(asCachingSequence())
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun ByteArray.asCachingSequence(): Sequence<Byte> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun ShortArray.asCachingSequence(): Sequence<Short> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun IntArray.asCachingSequence(): Sequence<Int> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun LongArray.asCachingSequence(): Sequence<Long> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun FloatArray.asCachingSequence(): Sequence<Float> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun DoubleArray.asCachingSequence(): Sequence<Double> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun BooleanArray.asCachingSequence(): Sequence<Boolean> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original array returning its elements when being iterated.
 */
fun CharArray.asCachingSequence(): Sequence<Char> {
    if (isEmpty()) return emptySequence()
    return Sequence { this.iterator() }
}

/**
 * Creates a [CachingSequence] instance that wraps the original stream iterating through its elements.
 */
fun <T> Stream<T>.asCachingSequence(): CachingSequence<T> = CachingSequence { iterator() }

/**
 * Creates a [CachingSequence] instance that wraps the original stream iterating through its elements.
 */
fun IntStream.asCachingSequence(): CachingSequence<Int> = CachingSequence { iterator() }

/**
 * Creates a [CachingSequence] instance that wraps the original stream iterating through its elements.
 */
fun LongStream.asCachingSequence(): CachingSequence<Long> = CachingSequence { iterator() }

/**
 * Creates a [CachingSequence] instance that wraps the original stream iterating through its elements.
 */
fun DoubleStream.asCachingSequence(): CachingSequence<Double> = CachingSequence { iterator() }

/**
 * Creates a [CachingSequence] instance that wraps the original map returning its entries when being iterated.
 */
fun <K, V> Map<out K, V>.asCachingSequence(): CachingSequence<Map.Entry<K, V>> {
    return entries.asCachingSequence()
}

/**
 * Creates a [CachingSequence] instance that wraps the original char sequence returning its characters when being iterated.
 */
fun CharSequence.asCachingSequence(): CachingSequence<Char> {
    if (this is String && isEmpty()) return emptyCachingSequence()
    return CachingSequence { this.iterator() }
}