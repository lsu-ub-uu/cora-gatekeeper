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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;

public class GatekeeperTokenProviderTest {
	private static final int FIRST_NON_HARDCODED = 3;
	private UserPickerSpy userPicker;
	private Gatekeeper gatekeeper;
	private User logedInUser;
	private GatekeeperTokenProvider tokenProvider;

	@BeforeMethod
	public void setUp() {
		userPicker = new UserPickerSpy();
		Gatekeeper.INSTANCE.setUserPicker(userPicker);
		gatekeeper = Gatekeeper.INSTANCE;
		tokenProvider = new GatekeeperTokenProviderImp();
	}

	@Test
	public void testGetAuthTokenForUserInfo() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		String authToken = tokenProvider.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPicker.usedUserInfos.get(FIRST_NON_HARDCODED), userInfo);
		logedInUser = gatekeeper.getUserForToken(authToken);
		assertEquals(logedInUser.loginId, "someLoginId");
	}

}
