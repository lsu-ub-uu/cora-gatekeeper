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

package se.uu.ub.cora.gatekeeper.initialize;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.UserPickerProviderSpy;
import se.uu.ub.cora.userpicker.UserPickerProvider;

public class GatekeeperInitializerTest {
	private GatekeeperInitializer gatekeeperInitializer;
	private ServletContext source;
	private ServletContextEvent context;

	@BeforeMethod
	public void setUp() {
		gatekeeperInitializer = new GatekeeperInitializer();
		source = new ServletContextSpy();
		context = new ServletContextEvent(source);

	}

	@Test
	public void testInitializeSystem() {
		source.setInitParameter("userPickerProviderClassName",
				"se.uu.ub.cora.gatekeeper.UserPickerProviderSpy");
		gatekeeperInitializer.contextInitialized(context);
		assertTrue(GatekeeperImp.INSTANCE.getUserPickerProvider() instanceof UserPickerProviderSpy);
	}

	@Test(expectedExceptions = RuntimeException.class)
	public void testInitializeSystemWithoutUserPickerFactoryClassName() {
		gatekeeperInitializer.contextInitialized(context);
	}

	@Test
	public void testInitializeSystemInitInfoSetInDependencyProvider() {
		source.setInitParameter("userPickerProviderClassName",
				"se.uu.ub.cora.gatekeeper.UserPickerProviderSpy");
		source.setInitParameter("storageOnDiskBasePath", "/mnt/data/basicstorage");
		gatekeeperInitializer.contextInitialized(context);

		UserPickerProvider userPickerProviderSpy = gatekeeperInitializer.userPickerProvider;
		assertEquals(userPickerProviderSpy.getInitInfo().get("storageOnDiskBasePath"),
				"/mnt/data/basicstorage");
	}

	@Test
	public void testDestroySystem() {
		GatekeeperInitializer gatekeeperInitializer = new GatekeeperInitializer();
		gatekeeperInitializer.contextDestroyed(null);
		// TODO: should we do something on destroy?
	}
}
