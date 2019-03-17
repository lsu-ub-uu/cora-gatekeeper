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
import static org.testng.Assert.assertTrue;

import java.util.Map;
import java.util.ServiceLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

public class GatekeeperModuleInitializerTest {
	@Test
	public void testInitUsesGatekeeperModuleStarter() throws Exception {
		ServletContext source = new ServletContextSpy();
		source.setInitParameter("initParam1", "initValue1");
		source.setInitParameter("initParam2", "initValue2");
		ServletContextEvent context = new ServletContextEvent(source);
		GatekeeperModuleInitializer initializer = new GatekeeperModuleInitializer();

		makeSureErrorIsThrownAsNoImplementationsExistInThisModule(context, initializer);
		GatekeeperModuleStarter starter = initializer.getStarter();
		assertStarterIsGatekeeperModuleStarter(starter);
		assertInitParametersArePassedOnToStarter(starter);
		assertImplementationsAreCollectedUsingServiceLoader(starter);
	}

	private void makeSureErrorIsThrownAsNoImplementationsExistInThisModule(
			ServletContextEvent context, GatekeeperModuleInitializer initializer) {
		try {
			initializer.contextInitialized(context);
		} catch (Exception e) {
			assertTrue(e instanceof GatekeeperInitializationException);
			assertEquals(e.getMessage(), "No implementations found for UserPickerProvider");
		}
	}

	private void assertStarterIsGatekeeperModuleStarter(GatekeeperModuleStarter starter) {
		assertTrue(starter instanceof GatekeeperModuleStarter);
	}

	private void assertInitParametersArePassedOnToStarter(GatekeeperModuleStarter starter) {
		Map<String, String> initInfo = starter.getInitInfo();
		assertEquals(initInfo.size(), 2);
		assertEquals(initInfo.get("initParam1"), "initValue1");
		assertEquals(initInfo.get("initParam2"), "initValue2");
	}

	private void assertImplementationsAreCollectedUsingServiceLoader(
			GatekeeperModuleStarter starter) {
		Iterable<UserPickerProvider> iterable = starter.getImplementations();
		assertTrue(iterable instanceof ServiceLoader);
	}

}
