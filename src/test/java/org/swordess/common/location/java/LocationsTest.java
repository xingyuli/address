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

package org.swordess.common.location.java;

import org.junit.Test;
import org.swordess.common.location.LocationsKt;
import org.swordess.common.location.Place;
import org.swordess.common.location.Place.Block;
import org.swordess.common.location.Place.City;
import org.swordess.common.location.Place.Country;
import org.swordess.common.location.Place.Region;
import org.swordess.common.location.Place.State;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocationsTest {

    @Test
    public void testPlaceEqualsOnlyIfBothTypeAndCodeEquals() {
        Place p1 = new Country("Test", "1");

        Country countryB = new Country("Country B", "2");
        Place p2 = new State(countryB, "Test", "1");
        Place p3 = new State(countryB, "Another name", "1");

        assertFalse(p2.equals(p1));
        assertTrue(p2.equals(p3));
    }

    @Test
    public void testCountryToAddress() {
        Country country = new Country("中国", "1");
        assertEquals("Address(country=中国, state=, city=, region=, block=, street=)", LocationsKt.toAddress(country).toString());
    }

    @Test
    public void testStateToAddress() {
        State state = new State(new Country("中国", "1"), "四川", "1");
        assertEquals("Address(country=中国, state=四川, city=, region=, block=, street=)", LocationsKt.toAddress(state).toString());
    }

    @Test
    public void testCityToAddress() {
        City city = new City(new State(new Country("中国", "1"), "四川", "1"), "成都", "1");
        assertEquals("Address(country=中国, state=四川, city=成都, region=, block=, street=)", LocationsKt.toAddress(city).toString());
    }

    @Test
    public void testRegionToAddress() {
        Region region = new Region(new City(new State(new Country("中国", "1"), "四川", "1"), "成都", "1"), "青羊区", "1");
        assertEquals("Address(country=中国, state=四川, city=成都, region=青羊区, block=, street=)", LocationsKt.toAddress(region).toString());
    }

    @Test
    public void testBlockToAddress() {
        Block block = new Block(new Region(new City(new State(new Country("中国", "1"), "四川", "1"), "成都", "1"), "青羊区", "1"), "草市街", "1");
        assertEquals("Address(country=中国, state=四川, city=成都, region=青羊区, block=草市街, street=)", LocationsKt.toAddress(block).toString());
    }

}
