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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.UserPickerProviderSpy;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;

public class GatekeeperInitializerTest {
	private GatekeeperInitializer gatekeeperInitializer;
	private ServletContext source;
	private ServletContextEvent context;

	@BeforeMethod
	public void setUp() {
		// Thread.currentThread().setContextClassLoader(new DummyClassLoader());
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
		assertNotNull(GatekeeperInstanceProvider.getGatekeeper());
		assertSame(GatekeeperInstanceProvider.getGatekeeper(),
				GatekeeperInstanceProvider.getGatekeeper());
	}

	@Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ""
			+ "Error starting Gatekeeper: "
			+ "Context must have a userPickerProviderClassName set.")
	public void testInitializeSystemWithoutUserPickerFactoryClassName() {
		gatekeeperInitializer.contextInitialized(context);
	}

	@Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ""
			+ "Error starting Gatekeeper: "
			+ "Invocation exception from UserPickerProviderThrowsInvocationTargetOnStartupSpy")
	public void testHandlingAndGettingCorrectErrorMessageFromErrorsThrowsOnStartup() {
		source.setInitParameter("userPickerProviderClassName",
				"se.uu.ub.cora.gatekeeper.UserPickerProviderThrowsInvocationTargetOnStartupSpy");
		gatekeeperInitializer.contextInitialized(context);
		assertTrue(GatekeeperImp.INSTANCE.getUserPickerProvider() instanceof UserPickerProviderSpy);
	}

	@Test
	public void testInitializeSystemInitInfoSetInDependencyProvider() {
		source.setInitParameter("userPickerProviderClassName",
				"se.uu.ub.cora.gatekeeper.UserPickerProviderSpy");
		source.setInitParameter("storageOnDiskBasePath", "/mnt/data/basicstorage");
		gatekeeperInitializer.contextInitialized(context);

		UserPickerProviderSpy userPickerProviderSpy = (UserPickerProviderSpy) gatekeeperInitializer.userPickerProvider;
		assertEquals(userPickerProviderSpy.getInitInfo().get("storageOnDiskBasePath"),
				"/mnt/data/basicstorage");
	}

	@Test
	public void testLoadingUserPickerUsingServiceProvider() throws Exception {
		source.setInitParameter("userPickerProviderClassName",
				"se.uu.ub.cora.gatekeeper.UserPickerProviderSpy");
		source.setInitParameter("storageOnDiskBasePath", "/mnt/data/basicstorage");
		// ServiceLoader<UserPickerProvider> sl = new ServiceLoaderSpy();
		// Thread.currentThread().setContextClassLoader(new DummyClassLoader());
		// URL[] urls = new URL[] {};
		// urls[0] = new URL("se/uu/ub/cora/gatekeeper/user/");
		//
		// URLClassLoader urlClassLoader = new URLClassLoader(urls);
		// Thread.currentThread().setContextClassLoader(urlClassLoader);
		gatekeeperInitializer.contextInitialized(context);
		// TOOD: here
		// assertTrue(GatekeeperImp.INSTANCE.getUserPickerProvider() instanceof
		// UserPickerProviderSpy);
	}

	@Test
	public void testDestroySystem() {
		GatekeeperInitializer gatekeeperInitializer = new GatekeeperInitializer();
		gatekeeperInitializer.contextDestroyed(null);
		// TODO: should we do something on destroy?
	}
}
