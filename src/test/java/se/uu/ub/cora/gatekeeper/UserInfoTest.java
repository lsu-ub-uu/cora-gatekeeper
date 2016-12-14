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

package se.uu.ub.cora.gatekeeper;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import se.uu.ub.cora.userpicker.UserInfo;

public class UserInfoTest {
	@Test
	public void testUserInfoUserInStorage() {
		String idInUserStorage = "someIdFromStorage";

		UserInfo userInfo = UserInfo.withIdInUserStorage(idInUserStorage);
		assertEquals(userInfo.idInUserStorage, "someIdFromStorage");
	}

	@Test
	public void testUserInfo() {
		String idFromLogin = "idFromLogin";
		String domainFromLogin = "domainFromLogin";

		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain(idFromLogin, domainFromLogin);
		assertEquals(userInfo.idFromLogin, "idFromLogin");
	}
}
