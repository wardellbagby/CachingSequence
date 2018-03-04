package com.wardellbagby.cachingsequence

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test

class CachingSequenceTest {

    @Test
    fun testEmptyCachingSequence() {
        val cachingSequence = emptyCachingSequence<Int>()
        cachingSequence.forEach {
            fail("Received value: $it. Expected no values.")
        }
    }

    @Test
    fun testCachingSequence() {
        val cachingSequence = (0..1000).asCachingSequence()
        val iterator = cachingSequence.iterator()
        assertEquals(cachingSequence[10], 10)
        assertEquals(iterator.next(), 11)
        assertEquals(cachingSequence[1], 1)
        assertEquals(iterator.next(), 12)
        assertEquals(iterator.next(), 13)
        assertEquals(iterator.next(), 14)
        assertEquals(iterator.next(), 15)
        assertEquals(iterator.next(), 16)
        assertEquals(iterator.next(), 17)
        assertEquals(iterator.next(), 18)
        assertEquals(iterator.next(), 19)
        assertEquals(cachingSequence[400], 400)
        assertEquals(iterator.next(), 401)
        assertEquals(cachingSequence[11], 11)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testLessThanZero() {
        val cachingSequence = CachingSequence {
            (0..1000).iterator()
        }
        cachingSequence[-1]
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testGreaterThanMax() {
        val cachingSequence = CachingSequence {
            (0..1000).iterator()
        }
        cachingSequence[1001]
    }

    @Test
    fun testMultipleIterator() {
        var fail = false
        val cachingSequence: CachingSequence<Int> = buildCachingSequence {
            if (fail) {
                fail("Called the builder caching sequence twice.")
            }
            (0..1000).forEach { yield(it) }
        }
        val expected = cachingSequence.iterator().asSequence().take(10).toList()
        fail = true
        val actual = cachingSequence.iterator().asSequence().take(10).toList()
        assertEquals(expected, actual)
    }
}