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

import org.swordess.common.location.Location
import org.swordess.common.location.Place
import java.util.HashMap

class LocationImpl : Location {

    private val countryCodeToCountries: MutableMap<String, Place.Country> = HashMap()

    lateinit var defaultCountryCode: String

    constructor(addressFiles: Set<String>) {
        addressFiles.forEach {
            PlaceParser.parse(it).forEach { countryCodeToCountries[it.code] = it }
        }
    }

    override fun findCountry(): Place.Country? = defaultCountry()

    override fun findState(stateCode: String): Place.State? =
            findCountry()?.states?.firstOrNull { stateCode == it.code }

    override fun findCity(stateCode: String, cityCode: String): Place.City? =
            findState(stateCode)?.cities?.firstOrNull { cityCode == it.code }

    override fun findRegion(stateCode: String, cityCode: String, regionCode: String): Place.Region? =
            findCity(stateCode, cityCode)?.regions?.firstOrNull { regionCode == it.code }

    private fun defaultCountry(): Place.Country? = countryCodeToCountries[defaultCountryCode]

}