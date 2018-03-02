package com.wardellbagby.cachingsequence

import org.junit.Assert.assertEquals
import org.junit.Assert.fail

class CachingSequenceTest {

    @org.junit.Test
    fun testEmptyCachingSequence() {
        val cachingSequence = emptyCachingSequence<Int>()
        cachingSequence.forEach {
            fail("Received value: $it. Expected no values.")
        }
    }

    @org.junit.Test
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

    @org.junit.Test(expected = IndexOutOfBoundsException::class)
    fun testLessThanZero() {
        val cachingSequence = CachingSequence {
            (0..1000).iterator()
        }
        cachingSequence[-1]
    }

    @org.junit.Test(expected = IndexOutOfBoundsException::class)
    fun testGreaterThanMax() {
        val cachingSequence = CachingSequence {
            (0..1000).iterator()
        }
        cachingSequence[1001]
    }
}