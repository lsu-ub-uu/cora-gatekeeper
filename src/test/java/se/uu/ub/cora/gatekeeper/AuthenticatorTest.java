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
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.spider.authentication.AuthenticationException;
import se.uu.ub.cora.spider.authentication.Authenticator;

public class AuthenticatorTest {
	private static final int FIRST_NON_HARDCODED = 3;
	private UserPickerSpy userPicker;
	private Authenticator authenticator;
	private User logedInUser;
	private GatekeeperTokenProvider tokenProvider;

	@BeforeMethod
	public void setUp() {
		userPicker = new UserPickerSpy();
		authenticator = new AuthenticatorImp(userPicker);
		tokenProvider = (GatekeeperTokenProvider) authenticator;
	}

	@Test
	public void testHardCodedTokens() {
		assertEquals(userPicker.usedUserInfos.get(0).idInUserStorage, "99999");
		logedInUser = authenticator.getUserForToken("dummySystemAdminAuthenticatedToken");
		assertEquals(logedInUser, userPicker.returnedUsers.get(0));

		assertEquals(userPicker.usedUserInfos.get(1).idInUserStorage, "121212");
		logedInUser = authenticator.getUserForToken("fitnesseUserToken");
		assertEquals(logedInUser, userPicker.returnedUsers.get(1));

		assertEquals(userPicker.usedUserInfos.get(2).idInUserStorage, "131313");
		logedInUser = authenticator.getUserForToken("fitnesseAdminToken");
		assertEquals(logedInUser, userPicker.returnedUsers.get(2));
	}

	@Test
	public void testNoToken() {
		logedInUser = authenticator.getUserForToken(null);
		assertPluggedInUserPickerWasUsed();
		assertUsedUserInfoIdInUserStorage("12345");
		assertReturnedUserIsFromUserPicker();
	}

	private void assertPluggedInUserPickerWasUsed() {
		assertTrue(userPicker.userPickerWasCalled);
	}

	private void assertUsedUserInfoIdInUserStorage(String expectedIdInUserStorage) {
		UserInfo usedUserInfo = userPicker.usedUserInfo;
		assertEquals(usedUserInfo.idInUserStorage, expectedIdInUserStorage);
	}

	private void assertReturnedUserIsFromUserPicker() {
		assertEquals(logedInUser, userPicker.returnedUser);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testNonAuthenticatedUser() {
		authenticator.getUserForToken("dummyNonAuthenticatedToken");
	}

	@Test
	public void testUserOnlyPickedOncePerAuthToken() {
		logedInUser = authenticator.getUserForToken("fitnesseAdminToken");
		assertPluggedInUserPickerWasUsedOnce();
		logedInUser = authenticator.getUserForToken("fitnesseAdminToken");
		assertPluggedInUserPickerWasUsedOnce();

	}

	private void assertPluggedInUserPickerWasUsedOnce() {
		assertEquals(userPicker.returnedUsers.size(), 3);
	}

	@Test
	public void testGetAuthTokenForUserInfo() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		String authToken = tokenProvider.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPicker.usedUserInfos.get(FIRST_NON_HARDCODED), userInfo);
		logedInUser = authenticator.getUserForToken(authToken);
		assertEquals(logedInUser.loginId, "someLoginId");
	}

}
