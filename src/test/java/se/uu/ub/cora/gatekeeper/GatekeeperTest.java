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

import se.uu.ub.cora.gatekeeper.authentication.User;
import se.uu.ub.cora.spider.authentication.AuthenticationException;

public class GatekeeperTest {
	private static final int FIRST_NON_HARDCODED = 3;
	private UserPickerFactorySpy userPickerFactory;
	private GatekeeperImp gatekeeper;
	private User logedInUser;

	@BeforeMethod
	public void setUp() {
		userPickerFactory = new UserPickerFactorySpy();
		GatekeeperImp.INSTANCE.setUserPickerFactory(userPickerFactory);
		gatekeeper = GatekeeperImp.INSTANCE;
	}

	@Test
	public void testEnum() {
		// small hack to get 100% coverage on enum
		GatekeeperImp.valueOf(GatekeeperImp.INSTANCE.toString());
	}

	@Test
	public void testHardCodedTokens() {
		assertEquals(userPickerFactory.factoredUserPickers.get(0).usedUserInfo.idInUserStorage,
				"99999");
		logedInUser = gatekeeper.getUserForToken("dummySystemAdminAuthenticatedToken");
		assertEquals(logedInUser, userPickerFactory.factoredUserPickers.get(0).returnedUser);

		assertEquals(userPickerFactory.factoredUserPickers.get(1).usedUserInfo.idInUserStorage,
				"121212");
		logedInUser = gatekeeper.getUserForToken("fitnesseUserToken");
		assertEquals(logedInUser, userPickerFactory.factoredUserPickers.get(1).returnedUser);

		assertEquals(userPickerFactory.factoredUserPickers.get(2).usedUserInfo.idInUserStorage,
				"131313");
		logedInUser = gatekeeper.getUserForToken("fitnesseAdminToken");
		assertEquals(logedInUser, userPickerFactory.factoredUserPickers.get(2).returnedUser);
	}

	@Test
	public void testNoToken() {
		logedInUser = gatekeeper.getUserForToken(null);
		assertPluggedInUserPickerWasUsed();
		assertUsedUserInfoIdInUserStorage(3, "12345");
		assertReturnedUserIsFromUserPicker(3);
	}

	private void assertPluggedInUserPickerWasUsed() {
		assertTrue(userPickerFactory.factoredUserPickers.get(0).userPickerWasCalled);
	}

	private void assertUsedUserInfoIdInUserStorage(int factoredNo, String expectedIdInUserStorage) {
		UserInfo usedUserInfo = userPickerFactory.factoredUserPickers.get(factoredNo).usedUserInfo;
		assertEquals(usedUserInfo.idInUserStorage, expectedIdInUserStorage);
	}

	private void assertReturnedUserIsFromUserPicker(int factoredNo) {
		assertEquals(logedInUser,
				userPickerFactory.factoredUserPickers.get(factoredNo).returnedUser);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testNonAuthenticatedUser() {
		gatekeeper.getUserForToken("dummyNonAuthenticatedToken");
	}

	@Test
	public void testUserOnlyPickedOncePerAuthToken() {
		logedInUser = gatekeeper.getUserForToken("fitnesseAdminToken");
		assertPluggedInUserPickerWasUsedOnce();
		logedInUser = gatekeeper.getUserForToken("fitnesseAdminToken");
		assertPluggedInUserPickerWasUsedOnce();

	}

	private void assertPluggedInUserPickerWasUsedOnce() {
		assertEquals(userPickerFactory.factoredUserPickers.size(), 3);
	}

	@Test
	public void testGetAuthTokenForUserInfo() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		String authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPickerFactory.factoredUserPickers.get(FIRST_NON_HARDCODED).usedUserInfo,
				userInfo);
		logedInUser = gatekeeper.getUserForToken(authToken);
		assertEquals(logedInUser.loginId, "someLoginId");
	}

}
