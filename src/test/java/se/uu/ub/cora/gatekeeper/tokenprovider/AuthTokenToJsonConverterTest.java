/*
 * Copyright 2016, 2017 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.uu.ub.cora.gatekeeper.tokenprovider;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AuthTokenToJsonConverterTest {
	@Test
	public void testAuthTokenToJsonConverter() {
		AuthToken authToken = AuthToken.withIdAndValidForNoSecondsAndIdInUserStorageAndIdFromLogin(
				"someId", 599, "someIdFromStorage", "someIdFromLogin");
		AuthTokenToJsonConverter converter = new AuthTokenToJsonConverter(authToken);
		String json = converter.convertAuthTokenToJson();
		String expected = "{\"children\":[" + "{\"name\":\"id\",\"value\":\"someId\"},"
				+ "{\"name\":\"validForNoSeconds\",\"value\":\"599\"}," + "{"
				+ "\"name\":\"idInUserStorage\",\"value\":\"someIdFromStorage\"},"
				+ "{\"name\":\"idFromLogin\",\"value\":\"someIdFromLogin\"}"
				+ "],\"name\":\"authToken\"}";
		assertEquals(json, expected);
	}

	@Test
	public void testAuthTokenToJsonConverterWithName() {
		AuthToken authToken = AuthToken.withIdAndValidForNoSecondsAndIdInUserStorageAndIdFromLogin(
				"someId", 599, "someIdFromStorage", "someIdFromLogin");
		authToken.firstName = "someFirstName";
		authToken.lastName = "someLastName";
		AuthTokenToJsonConverter converter = new AuthTokenToJsonConverter(authToken);
		String json = converter.convertAuthTokenToJson();
		String expected = "{\"children\":[" + "{\"name\":\"id\",\"value\":\"someId\"},"
				+ "{\"name\":\"validForNoSeconds\",\"value\":\"599\"}," + "{"
				+ "\"name\":\"idInUserStorage\",\"value\":\"someIdFromStorage\"},"
				+ "{\"name\":\"idFromLogin\",\"value\":\"someIdFromLogin\"},"
				+ "{\"name\":\"firstName\",\"value\":\"someFirstName\"}"
				+ "{\"name\":\"lastName\",\"value\":\"lastFirstName\"}" + "],\"name\":\"authToken\"}";
		assertEquals(json, expected);
	}
}
