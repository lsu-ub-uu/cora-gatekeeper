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

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.authentication.AuthenticationException;
import se.uu.ub.cora.gatekeeper.tokenprovider.AuthToken;
import se.uu.ub.cora.userpicker.User;
import se.uu.ub.cora.userpicker.UserInfo;

import static org.testng.Assert.*;

public class GatekeeperTest {
	private static final int FIRST_NON_HARDCODED = 3;
	private UserPickerProviderSpy userPickerFactory;
	private GatekeeperImp gatekeeper;
	private User logedInUser;

	@BeforeMethod
	public void setUp() {
		Map<String, String> initInfo = new HashMap<>();
		initInfo.put("storageOnDiskBasePath", "");
		userPickerFactory = new UserPickerProviderSpy(initInfo);
		GatekeeperImp.INSTANCE.setUserPickerProvider(userPickerFactory);
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
	public void testNoTokenAlsoKnownAsGuest() {
		logedInUser = gatekeeper.getUserForToken(null);
		assertPluggedInUserPickerWasUsedToPickGuest();
	}

	private void assertPluggedInUserPickerWasUsedToPickGuest() {
		assertTrue(
				userPickerFactory.factoredUserPickers.get(FIRST_NON_HARDCODED).pickGuestWasCalled);
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
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		assertEquals(userPickerFactory.factoredUserPickers.get(FIRST_NON_HARDCODED).usedUserInfo,
				userInfo);
		logedInUser = gatekeeper.getUserForToken(authToken.id);
		assertEquals(logedInUser.loginId, "someLoginId");
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testGetAuthTokenWithProblem() {
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginIdWithProblem",
				"someLoginDomain");
		gatekeeper.getAuthTokenForUserInfo(userInfo);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testRemoveAuthTokenForUser(){
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		logedInUser = gatekeeper.getUserForToken(authToken.id);
		assertEquals(logedInUser.loginId, "someLoginId");
		gatekeeper.removeAuthTokenForUser(authToken.id, userInfo);
		gatekeeper.getUserForToken(authToken.id);
	}

	@Test(expectedExceptions = AuthenticationException.class)
	public void testRemoveAuthTokenForUserTokenDoesNotExist(){
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");

		gatekeeper.removeAuthTokenForUser("someNonExistingToken", userInfo);
		gatekeeper.getUserForToken("someNonExistingToken");
	}

	@Test
	public void testRemoveAuthTokenForUserUserInfoIdNotTheSame(){
		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);

		logedInUser = gatekeeper.getUserForToken(authToken.id);
		assertEquals(logedInUser.loginId, "someLoginId");
		UserInfo someOtherUserInfo = UserInfo.withLoginIdAndLoginDomain("someOtherLoginId", "someLoginDomain");
		gatekeeper.removeAuthTokenForUser(authToken.id, someOtherUserInfo);
		assertNotNull(gatekeeper.getUserForToken(authToken.id));
	}

//	@Test
//	public void testRemoveAuthTokenForUserUserInfoDomainNotTheSame(){
//		UserInfo userInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someLoginDomain");
//		AuthToken authToken = gatekeeper.getAuthTokenForUserInfo(userInfo);
//
//		logedInUser = gatekeeper.getUserForToken(authToken.id);
//		assertEquals(logedInUser.loginId, "someLoginId");
//		UserInfo someOtherUserInfo = UserInfo.withLoginIdAndLoginDomain("someLoginId", "someOtherLoginDomain");
//		gatekeeper.removeAuthTokenForUser(authToken.id, someOtherUserInfo);
//		assertNotNull(gatekeeper.getUserForToken(authToken.id));
//	}
}
