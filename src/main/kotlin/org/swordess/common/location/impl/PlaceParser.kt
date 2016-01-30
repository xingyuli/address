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

package org.swordess.common.location.impl

import org.swordess.common.lang.io.resourceNameAsStream
import org.swordess.common.location.Place
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory

object PlaceParser {

    private const val TAG_NAME_COUNTRY = "Country"
    private const val TAG_NAME_STATE = "State"
    private const val TAG_NAME_CITY = "City"
    private const val TAG_NAME_REGION = "Region"
    private const val TAG_NAME_BLOCK = "Block"

    private const val ATTR_NAME_CODE = "Code"
    private const val ATTR_NAME_NAME = "Name"

    fun parse(file: String): List<Place.Country> {
        val countries = ArrayList<Place.Country>()

        file.resourceNameAsStream().bufferedReader().use {
            val dbf = DocumentBuilderFactory.newInstance()
            val doc = dbf.newDocumentBuilder().parse(InputSource(it))

            val countryNodes = doc.getElementsByTagName(TAG_NAME_COUNTRY)
            for (i in 0 until countryNodes.length) {
                countries.add(parseCountry(countryNodes.item(i)))
            }
        }

        return countries
    }

    private fun parseCountry(countryNode: Node): Place.Country {
        val (name, code) = parsePlace(countryNode)
        val country = Place.Country(name, code)

        countryNode.filterChild { TAG_NAME_STATE == it.nodeName }
                .forEach { country._states.add(parseState(country, it)) }

        return country
    }

    private fun parseState(country: Place.Country, stateNode: Node): Place.State {
        val (name, code) = parsePlace(stateNode)
        val state = Place.State(country, name, code)

        stateNode.filterChild { TAG_NAME_CITY == it.nodeName }
                .forEach { state._cities.add(parseCity(state, it)) }

        return state
    }

    private fun parseCity(state: Place.State, cityNode: Node): Place.City {
        val (name, code) = parsePlace(cityNode)
        val city = Place.City(state, name, code)

        cityNode.filterChild { TAG_NAME_REGION == it.nodeName }
                .forEach { city._regions.add(parseRegion(city, it)) }

        return city
    }

    private fun parseRegion(city: Place.City, regionNode: Node): Place.Region {
        val (name, code) = parsePlace(regionNode)
        val region = Place.Region(city, name, code)

        regionNode.filterChild { TAG_NAME_BLOCK == it.nodeName }
                .forEach { region._blocks.add(parseBlock(region, it)) }

        return region
    }

    private fun parseBlock(region: Place.Region, blockNode: Node): Place.Block {
        val (name, code) = parsePlace(blockNode)
        return Place.Block(region, name, code)
    }

    private fun Node.filterChild(predicate: (Node)->Boolean): List<Node> {
        val result = arrayListOf<Node>()
        val children = childNodes
        for (i in 0 until children.length) {
            val child = children.item(i)
            if (predicate(child)) {
                result.add(child)
            }
        }
        return result
    }

    private data class NameCodePair(val name: String, val code: String)

    private fun parsePlace(node: Node): NameCodePair =
        NameCodePair(
                node.attributes.getNamedItem(ATTR_NAME_NAME).nodeValue,
                node.attributes.getNamedItem(ATTR_NAME_CODE).nodeValue)

}