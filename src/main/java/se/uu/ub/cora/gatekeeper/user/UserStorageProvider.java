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

import se.uu.ub.cora.initialize.ModuleInitializer;
import se.uu.ub.cora.initialize.ModuleInitializerImp;

/**
 * AppTokenStorageViewInstanceProvider provides view access to apptoken data stored in storage.
 */
public class UserStorageProvider {

	private static UserStorageViewInstanceProvider instanceProvider;
	private static ModuleInitializer moduleInitializer = new ModuleInitializerImp();

	private UserStorageProvider() {
		// prevent call to constructor
		throw new UnsupportedOperationException();
	}

	/**
	 * getStorageView returns a new AppTokenStorageView that can be used by anything that needs
	 * access apptoken data.
	 * <p>
	 * <i>Code using the returned AppTokenStorageView instance MUST consider the returned instance
	 * as NOT thread safe.</i>
	 * 
	 * @return A AppTokenStorageView that gives access to apptoken data.
	 */
	public static UserStorageView getStorageView() {
		locateAndChooseRecordStorageInstanceProvider();
		return instanceProvider.getStorageView();
	}

	private static void locateAndChooseRecordStorageInstanceProvider() {
		if (instanceProvider == null) {
			instanceProvider = moduleInitializer
					.loadOneImplementationBySelectOrder(UserStorageViewInstanceProvider.class);
		}
	}

	public static void onlyForTestSetModuleInitializer(ModuleInitializer moduleInitializer) {
		UserStorageProvider.moduleInitializer = moduleInitializer;

	}

	public static ModuleInitializer onlyForTestGetModuleInitializer() {
		return moduleInitializer;
	}

	/**
	 * onlyForTestSetAppTokenViewInstanceProvider sets a AppTokenStorageViewInstanceProvider that
	 * will be used to return instances for the {@link #getStorageView()} method. This possibility
	 * to set a AppTokenStorageViewInstanceProvider is provided to enable testing of getting a
	 * record storage in other classes and is not intented to be used in production.
	 * <p>
	 * The AppTokenStorageViewInstanceProvider to use in production should be provided through an
	 * implementation of {@link UserStorageViewInstanceProvider} in a seperate java module.
	 * 
	 * @param instanceProvider
	 *            A AppTokenStorageViewInstanceProvider to use to return AppTokenStorageView
	 *            instances for testing
	 */
	public static void onlyForTestSetAppTokenViewInstanceProvider(
			UserStorageViewInstanceProvider instanceProvider) {
		UserStorageProvider.instanceProvider = instanceProvider;

	}

}
