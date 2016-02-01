/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Vic Lau
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.swordess.common.location

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocationsTest {

    @Test
    fun testPlaceEqualsOnlyIfBothTypeAndCodeEquals() {
        val p1 = Place.Country("Test", "1")

        val countryB = Place.Country("Country B", "2")
        val p2 = Place.State(countryB, "Test", "1")
        val p3 = Place.State(countryB, "Another name", "1")

        assertFalse(p2.equals(p1))
        assertTrue(p2.equals(p3))
    }

    @Test
    fun testCountryToAddress() {
        val country = Place.Country("中国", "1")
        assertEquals("Address(country=中国, state=, city=, region=, block=, street=)", country.toAddress().toString())
    }

    @Test
    fun testStateToAddress() {
        val state = Place.State(Place.Country("中国", "1"), "四川", "1")
        assertEquals("Address(country=中国, state=四川, city=, region=, block=, street=)", state.toAddress().toString())
    }

    @Test
    fun testCityToAddress() {
        val city = Place.City(Place.State(Place.Country("中国", "1"), "四川", "1"), "成都", "1")
        assertEquals("Address(country=中国, state=四川, city=成都, region=, block=, street=)", city.toAddress().toString())
    }

    @Test
    fun testRegionToAddress() {
        val region = Place.Region(Place.City(Place.State(Place.Country("中国", "1"), "四川", "1"), "成都", "1"), "青羊区", "1")
        assertEquals("Address(country=中国, state=四川, city=成都, region=青羊区, block=, street=)", region.toAddress().toString())
    }

    @Test
    fun testBlockToAddress() {
        val block = Place.Block(Place.Region(Place.City(Place.State(Place.Country("中国", "1"), "四川", "1"), "成都", "1"), "青羊区", "1"), "草市街", "1")
        assertEquals("Address(country=中国, state=四川, city=成都, region=青羊区, block=草市街, street=)", block.toAddress().toString())
    }

}