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

import java.util.HashSet

data class Address(
        var country: String = "",
        var state: String = "",
        var city: String = "",
        var region: String = "",
        var block: String = "",
        var street: String = "")

sealed class Place(open val name: String, open val code: String) {

    override fun equals(other: Any?): Boolean =
            other != null && javaClass === other.javaClass && code == (other as Place).code

    override fun hashCode(): Int = code.hashCode()

    class Country(override val name: String, override val code: String) : Place(name, code) {
        internal val _states: MutableSet<State> = HashSet()
        val states: Set<State>
            get() = _states
    }

    class State(val country: Country, override val name: String, override val code: String) : Place(name, code) {
        internal val _cities: MutableSet<City> = HashSet()
        val cities: Set<City>
            get() = _cities
    }

    class City(val state: State, override val name: String, override val code: String) : Place(name, code) {
        internal val _regions: MutableSet<Region> = HashSet()
        val regions: Set<Region>
            get() = _regions
    }

    class Region(val city: City, override val name: String, override val code: String) : Place(name, code) {
        internal val _blocks: MutableSet<Block> = HashSet()
        val blocks: Set<Block>
            get() = _blocks
    }

    class Block(val region: Region, override val name: String, override val code: String) : Place(name, code)

}

fun Place.Country.toAddress() = Address(country = name)

fun Place.State.toAddress() = country.toAddress().apply { state = name }

fun Place.City.toAddress() = state.toAddress().apply { city = name }

fun Place.Region.toAddress() = city.toAddress().apply { region = name }

fun Place.Block.toAddress() = region.toAddress().apply { block = name }
