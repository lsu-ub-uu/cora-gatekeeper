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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.UserPickerProviderSpy;
import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

public class GatekeeperModuleStarterTest {

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "No implementations found for UserPickerProvider")
	public void testStartModuleThrowsErrorIfNoImplementations() throws Exception {
		Map<String, String> initInfo = new HashMap<>();
		Iterable<UserPickerProvider> implementations = new ArrayList<UserPickerProvider>();
		GatekeeperModuleStarter starter = GatekeeperModuleStarter
				.usingInitInfoAndImplementations(initInfo, implementations);
		starter.startProvider();
	}

	@Test(expectedExceptions = GatekeeperInitializationException.class, expectedExceptionsMessageRegExp = ""
			+ "More than one implementation found for UserPickerProvider")
	public void testStartModuleThrowsErrorIfMoreThanOneImplementations() throws Exception {
		Map<String, String> initInfo = new HashMap<>();
		List<UserPickerProvider> implementations = new ArrayList<UserPickerProvider>();
		implementations.add(new UserPickerProviderSpy(null));
		implementations.add(new UserPickerProviderSpy(null));
		GatekeeperModuleStarter starter = GatekeeperModuleStarter
				.usingInitInfoAndImplementations(initInfo, implementations);
		starter.startProvider();
	}

	@Test()
	public void testStartModuleInitInfoSentToImplementation() throws Exception {
		Map<String, String> initInfo = new HashMap<>();
		List<UserPickerProvider> implementations = new ArrayList<UserPickerProvider>();
		UserPickerProviderSpy userPickerProviderSpy = new UserPickerProviderSpy(null);
		implementations.add(userPickerProviderSpy);
		GatekeeperModuleStarter starter = GatekeeperModuleStarter
				.usingInitInfoAndImplementations(initInfo, implementations);
		starter.startProvider();
		assertEquals(userPickerProviderSpy.getInitInfo(), initInfo);
	}
}
