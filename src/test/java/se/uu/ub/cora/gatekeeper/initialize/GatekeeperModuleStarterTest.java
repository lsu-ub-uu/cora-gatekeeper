/*
 * Copyright 2019 Olov McKie
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
package se.uu.ub.cora.gatekeeper.initialize;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.UserPickerProviderSpy;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;
import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;
import se.uu.ub.cora.gatekeeper.user.UserStorage;

public class GatekeeperModuleStarterTest {

	private Map<String, String> initInfo;
	private List<UserPickerProvider> userPickerProviders;
	private List<UserStorage> userStorages;

	@BeforeMethod
	public void beforeMethod() {
		initInfo = new HashMap<>();
		initInfo.put("guestUserId", "someGuestUserId");
		userPickerProviders = new ArrayList<>();
		userPickerProviders.add(new UserPickerProviderSpy(null));
		userStorages = new ArrayList<>();
		userStorages.add(new UserStorageSpy());

	}

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "No implementations found for UserPickerProvider")
	public void testStartModuleThrowsErrorIfNoUserPickerProviderImplementations() throws Exception {
		userPickerProviders.clear();
		startGatekeeperModuleStarter();
	}

	private void startGatekeeperModuleStarter() {
		GatekeeperModuleStarter starter = GatekeeperModuleStarter
				.usingInitInfoAndUserPickerProvidersAndUserStorages(initInfo, userPickerProviders,
						userStorages);
		starter.start();
	}

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "More than one implementation found for UserPickerProvider")
	public void testStartModuleThrowsErrorIfMoreThanOneUserPickerProviderImplementations()
			throws Exception {
		userPickerProviders.add(new UserPickerProviderSpy(null));
		startGatekeeperModuleStarter();
	}

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "InitInfo must contain guestUserId")
	public void testStartModuleThrowsErrorIfMissingGuestUserId() throws Exception {
		initInfo.clear();
		startGatekeeperModuleStarter();
	}

	@Test()
	public void testStartModuleGuestUserIdSentToImplementation() throws Exception {
		UserPickerProviderSpy userPickerProviderSpy = (UserPickerProviderSpy) userPickerProviders
				.get(0);
		startGatekeeperModuleStarter();
		assertEquals(userPickerProviderSpy.guestUserId(), "someGuestUserId");
	}

	// **
	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "No implementations found for UserStorage")
	public void testStartModuleThrowsErrorIfNoUserStorageImplementations() throws Exception {
		userStorages.clear();
		startGatekeeperModuleStarter();
	}

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "More than one implementation found for UserStorage")
	public void testStartModuleThrowsErrorIfMoreThanOneUserStorageImplementations()
			throws Exception {
		userStorages.add(new UserStorageSpy());
		startGatekeeperModuleStarter();
	}

	@Test()
	public void testStartModuleUserStorageSentToImplementation() throws Exception {
		UserPickerProviderSpy userPickerProviderSpy = (UserPickerProviderSpy) userPickerProviders
				.get(0);
		UserStorageSpy userStorageSpy = (UserStorageSpy) userStorages.get(0);
		startGatekeeperModuleStarter();
		assertEquals(userPickerProviderSpy.getUserStorage(), userStorageSpy);
	}

	@Test
	public void testGatekeeperInstanceProviderSetUpWithLocator() throws Exception {
		UserPickerProviderSpy userPickerProviderSpy = (UserPickerProviderSpy) userPickerProviders
				.get(0);
		startGatekeeperModuleStarter();
		assertTrue(GatekeeperImp.INSTANCE.getUserPickerProvider() instanceof UserPickerProviderSpy);
		assertSame(GatekeeperImp.INSTANCE.getUserPickerProvider(), userPickerProviderSpy);
		assertNotNull(GatekeeperInstanceProvider.getGatekeeper());
	}
}
