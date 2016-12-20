/*
 * Copyright 2016 Uppsala University Library
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
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.userpicker.UserInfo;

public class JsonToUserInfoConverterTest {
	@Test
	public void testJsonToUserInfoConverter() {
		String jsonUserInfo = "{\"children\":[" + "{\"name\":\"idFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"domainFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"idInUserStorage\",\"value\":\"131313\"}"
				+ "],\"name\":\"userInfo\"}";
		JsonToUserInfoConverter converter = new JsonToUserInfoConverter(jsonUserInfo);
		UserInfo userInfo = converter.parseUserInfoFromJson();
		assertEquals(userInfo.idInUserStorage, "131313");
		assertNull(userInfo.idFromLogin);
		assertNull(userInfo.domainFromLogin);
	}

	@Test
	public void testJsonToUserInfoConverter2() {
		String jsonUserInfo = "{\"children\":["
				+ "{\"name\":\"idFromLogin\",\"value\":\"someLoginIdWithProblem\"},"
				+ "{\"name\":\"domainFromLogin\",\"value\":\"\"},"
				+ "{\"name\":\"idInUserStorage\",\"value\":\"\"}" + "],\"name\":\"userInfo\"}";

		JsonToUserInfoConverter converter = new JsonToUserInfoConverter(jsonUserInfo);
		UserInfo userInfo = converter.parseUserInfoFromJson();
		assertNull(userInfo.idInUserStorage);
		assertEquals(userInfo.idFromLogin, "someLoginIdWithProblem");
		assertEquals(userInfo.domainFromLogin, "");
	}

	@Test
	public void testJsonToUserInfoConverter2NoIdInUserStorage() {
		String jsonUserInfo = "{\"children\":["
				+ "{\"name\":\"idFromLogin\",\"value\":\"someLoginIdWithProblem\"},"
				+ "{\"name\":\"domainFromLogin\",\"value\":\"\"}" + "],\"name\":\"userInfo\"}";

		JsonToUserInfoConverter converter = new JsonToUserInfoConverter(jsonUserInfo);
		UserInfo userInfo = converter.parseUserInfoFromJson();
		assertNull(userInfo.idInUserStorage);
		assertEquals(userInfo.idFromLogin, "someLoginIdWithProblem");
		assertEquals(userInfo.domainFromLogin, "");
	}

}
