/*
 * Copyright 2022 Uppsala University Library
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
package se.uu.ub.cora.gatekeeper.user;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.gatekeeper.spies.UserStorageViewInstanceProviderSpy;
import se.uu.ub.cora.initialize.ModuleInitializer;
import se.uu.ub.cora.initialize.ModuleInitializerImp;
import se.uu.ub.cora.initialize.spies.ModuleInitializerSpy;
import se.uu.ub.cora.logger.LoggerFactory;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.logger.spies.LoggerFactorySpy;

public class UserStorageProviderTest {
	LoggerFactory loggerFactory = new LoggerFactorySpy();
	private ModuleInitializerSpy moduleInitializerSpy;
	private UserStorageViewInstanceProviderSpy instanceProviderSpy;

	@BeforeMethod
	public void beforeMethod() {
		LoggerProvider.setLoggerFactory(loggerFactory);
		UserStorageProvider.onlyForTestSetAppTokenViewInstanceProvider(null);
	}

	private void setupModuleInstanceProviderToReturnAppTokenStorageViewFactorySpy() {
		moduleInitializerSpy = new ModuleInitializerSpy();
		instanceProviderSpy = new UserStorageViewInstanceProviderSpy();
		moduleInitializerSpy.MRV.setDefaultReturnValuesSupplier(
				"loadOneImplementationBySelectOrder",
				((Supplier<UserStorageViewInstanceProvider>) () -> instanceProviderSpy));

		UserStorageProvider.onlyForTestSetModuleInitializer(moduleInitializerSpy);
	}

	@Test
	public void testPrivateConstructor() throws Exception {
		Constructor<UserStorageProvider> constructor = UserStorageProvider.class
				.getDeclaredConstructor();
		assertTrue(Modifier.isPrivate(constructor.getModifiers()));
	}

	@Test(expectedExceptions = InvocationTargetException.class)
	public void testPrivateConstructorInvoke() throws Exception {
		Constructor<UserStorageProvider> constructor = UserStorageProvider.class
				.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testDefaultInitializerIsModuleInitalizer() throws Exception {
		ModuleInitializer initializer = UserStorageProvider.onlyForTestGetModuleInitializer();
		assertNotNull(initializer);
		assertTrue(initializer instanceof ModuleInitializerImp);
	}

	@Test
	public void testGetRecordStorageUsesModuleInitializerToGetFactory() throws Exception {
		setupModuleInstanceProviderToReturnAppTokenStorageViewFactorySpy();

		UserStorageView appTokenStorageView = UserStorageProvider.getStorageView();

		assertNotNull(appTokenStorageView);
		moduleInitializerSpy.MCR.assertParameters("loadOneImplementationBySelectOrder", 0,
				UserStorageViewInstanceProvider.class);
		instanceProviderSpy.MCR.assertReturn("getStorageView", 0, appTokenStorageView);
	}

	@Test
	public void testOnlyForTestSetAppTokenStorageViewInstanceProvider() throws Exception {
		UserStorageViewInstanceProviderSpy instanceProviderSpy2 = new UserStorageViewInstanceProviderSpy();
		UserStorageProvider.onlyForTestSetAppTokenViewInstanceProvider(instanceProviderSpy2);

		UserStorageView appTokenStorageView = UserStorageProvider.getStorageView();

		instanceProviderSpy2.MCR.assertReturn("getStorageView", 0, appTokenStorageView);
	}

	@Test
	public void testMultipleCallsToGetStorageViewOnlyLoadsImplementationsOnce() throws Exception {
		setupModuleInstanceProviderToReturnAppTokenStorageViewFactorySpy();

		UserStorageProvider.getStorageView();
		UserStorageProvider.getStorageView();
		UserStorageProvider.getStorageView();
		UserStorageProvider.getStorageView();

		moduleInitializerSpy.MCR.assertNumberOfCallsToMethod("loadOneImplementationBySelectOrder",
				1);
	}
}
