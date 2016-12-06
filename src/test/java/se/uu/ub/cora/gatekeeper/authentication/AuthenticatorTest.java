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

package se.uu.ub.cora.gatekeeper.authentication;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.beefeater.authentication.User;
import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.UserInfo;
import se.uu.ub.cora.gatekeeper.UserPickerFactorySpy;
import se.uu.ub.cora.spider.authentication.AuthenticationException;
import se.uu.ub.cora.spider.authentication.Authenticator;

public class AuthenticatorTest {
	private UserPickerFactorySpy userPickerFactory;
	private Authenticator authenticator;
	private User logedInUser;

	@BeforeMethod
	public void setUp() {
		userPickerFactory = new UserPickerFactorySpy();
		authenticator = new AuthenticatorImp();
		GatekeeperImp.INSTANCE.setUserPickerFactory(userPickerFactory);
	}

	@Test
	public void testHardCodedTokens() {
		assertEquals(userPickerFactory.factoredUserPickers.get(0).usedUserInfo.idInUserStorage,
				"99999");
		logedInUser = authenticator.getUserForToken("dummySystemAdminAuthenticatedToken");
		assertEquals(logedInUser.id, "999999");

		assertEquals(userPickerFactory.factoredUserPickers.get(1).usedUserInfo.idInUserStorage,
				"121212");
		logedInUser = authenticator.getUserForToken("fitnesseUserToken");
		assertEquals(logedInUser.id, "121212");

		assertEquals(userPickerFactory.factoredUserPickers.get(2).usedUserInfo.idInUserStorage,
				"131313");
		logedInUser = authenticator.getUserForToken("fitnesseAdminToken");
		assertEquals(logedInUser.id, "131313");
	}

	@Test
	public void testNoToken() {
		logedInUser = authenticator.getUserForToken(null);
		assertPluggedInUserPickerWasUsed();
		assertUsedUserInfoIdInUserStorage(3, "12345");
		assertReturnedUserIsFromUserPicker(3);
	}

	private void assertPluggedInUserPickerWasUsed() {
		assertTrue(userPickerFactory.factoredUserPickers.get(0).userPickerWasCalled);
	}

	private void assertUsedUserInfoIdInUserStorage(int factored, String expectedIdInUserStorage) {
		UserInfo usedUserInfo = userPickerFactory.factoredUserPickers.get(factored).usedUserInfo;
		assertEquals(usedUserInfo.idInUserStorage, expectedIdInUserStorage);
	}

	private void assertReturnedUserIsFromUserPicker(int factored) {
		assertEquals(logedInUser, userPickerFactory.factoredUserPickers.get(factored).returnedUser);
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
		assertEquals(userPickerFactory.factoredUserPickers.size(), 3);
	}
}
