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

package se.uu.ub.cora.gatekeeper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.authentication.AuthenticationException;
import se.uu.ub.cora.gatekeeper.tokenprovider.AuthToken;
import se.uu.ub.cora.gatekeeper.user.User;
import se.uu.ub.cora.gatekeeper.user.UserInfo;

public class GatekeeperTest {
	private static final int FIRST_NON_HARDCODED = 0;
	private UserPickerProviderSpy userPickerProvider;
	private GatekeeperImp gatekeeper;
	private User logedInUser;

	@BeforeMethod
	public void setUp() {
		Map<String, String> initInfo = new HashMap<>();
		initInfo.put("storageOnDiskBasePath", "");
		userPickerProvider = new UserPickerProviderSpy(initInfo);
		GatekeeperImp.INSTANCE.setUserPickerProvider(userPickerProvider);
		gatekeeper = GatekeeperImp.INSTANCE;
	}

	@Test
	public void testEnum() {
		// small hack to get 100% coverage on enum
		GatekeeperImp.valueOf(GatekeeperImp.INSTANCE.toString());
	}

	@Test
	public void testNoTokenAlsoKnownAsGuest() {
		logedInUser = gatekeeper.getUserForToken(null);
		assertPluggedInUserPickerWasUsedToPickGuest();

		User expectedGuest = userPickerProvider.factoredUserPickers.get(0).returnedUser;
		assertEquals(logedInUser, expectedGuest);
	}

	private void assertPluggedInUserPickerWasUsedToPickGuest() {
		assertTrue(
				userPickerProvider.factoredUserPickers.get(FIRST_NON_HARDCODED).pickGuestWasCalled);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testNonAuthenticatedUser() {
		gatekeeper.getUserForToken("dummyNonAuthenticatedToken");
	}

	@Test
	public void testUserOnlyPickedOncePerAuthToken() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		logedInUser = gatekeeper.getUserForToken(authToken.token);
		assertPluggedInUserPickerWasUsedOnce();
		logedInUser = gatekeeper.getUserForToken(authToken.token);
		assertPluggedInUserPickerWasUsedOnce();

	}

	private void assertPluggedInUserPickerWasUsedOnce() {
		assertEquals(userPickerProvider.factoredUserPickers.size(), 1);
	}

	@Test
	public void testGetAuthTokenForUserInfo() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPickerProvider.factoredUserPickers.get(FIRST_NON_HARDCODED).usedUserInfo,
				userInfo);
		assertNotNull(authToken.token);
		assertEquals(authToken.validForNoSeconds, 600);
		assertEquals(authToken.idInUserStorage, "12345");
		assertEquals(authToken.idFromLogin, "someLoginId");
	}

	@Test
	public void testGetAuthTokenForUserInfoWithIdStorage() {
		UserInfo userInfo = UserInfo.withIdInUserStorage("someIdInStorage");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPickerProvider.factoredUserPickers.get(FIRST_NON_HARDCODED).usedUserInfo,
				userInfo);

		assertNotNull(authToken.token);
		assertEquals(authToken.validForNoSeconds, 600);
		assertEquals(authToken.idInUserStorage, "someIdInStorage");
		assertEquals(authToken.idFromLogin, "loginIdFromUserPickerSpy");
	}

	@Test
	public void testGetAuthTokenForUserInfoWithIdStorageThatReturnsName() {
		UserInfo userInfo = UserInfo.withIdInUserStorage("someIdInStorageReturningName");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPickerProvider.factoredUserPickers.get(FIRST_NON_HARDCODED).usedUserInfo,
				userInfo);

		assertNotNull(authToken.token);
		assertEquals(authToken.validForNoSeconds, 600);
		assertEquals(authToken.idInUserStorage, "someIdInStorageReturningName");
		assertEquals(authToken.idFromLogin, "loginIdFromUserPickerSpy");
		assertEquals(authToken.firstName, "firstNameFromUserPickerSpy");
		assertEquals(authToken.lastName, "lastNameFromUserPickerSpy");
	}

	@Test
	public void testGetUserForToken() {
		AuthToken authToken = getTokenFromLoginToUseInTestNormallySentFromClient();

		logedInUser = gatekeeper.getUserForToken(authToken.token);
		assertEquals(logedInUser.loginId, "someLoginId");
	}

	private AuthToken getTokenFromLoginToUseInTestNormallySentFromClient() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);
		return authToken;
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testGetAuthTokenWithProblem() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginIdWithProblem",
				"someLoginDomain");
		gatekeeper.getAuthTokenForUserInfo(userInfo);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testRemoveAuthTokenForUser() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		logedInUser = gatekeeper.getUserForToken(authToken.token);
		assertEquals(logedInUser.loginId, "someLoginId");

		gatekeeper.removeAuthTokenForUser(authToken.token, "12345");
		gatekeeper.getUserForToken(authToken.token);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testRemoveAuthTokenForUserTokenDoesNotExist() {
		gatekeeper.removeAuthTokenForUser("someNonExistingToken", "someLoginId");
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testRemoveAuthTokenForUserUserIdNotTheSame() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		logedInUser = gatekeeper.getUserForToken(authToken.token);
		assertEquals(logedInUser.loginId, "someLoginId");

		gatekeeper.removeAuthTokenForUser(authToken.token, "someOtherLoginId");
	}
}
