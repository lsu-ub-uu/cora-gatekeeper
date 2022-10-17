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
package se.uu.ub.cora.gatekeeper.storage;

import se.uu.ub.cora.initialize.ModuleInitializer;
import se.uu.ub.cora.initialize.ModuleInitializerImp;

/**
 * UserStorageProvider provides view access to User data stored in storage.
 */
public class UserStorageProvider {

	private static UserStorageViewInstanceProvider instanceProvider;
	private static ModuleInitializer moduleInitializer = new ModuleInitializerImp();

	private UserStorageProvider() {
		// prevent call to constructor
		throw new UnsupportedOperationException();
	}

	/**
	 * getStorageView returns a new UserStorageView that can be used by anything that needs access
	 * user data.
	 * <p>
	 * <i>Code using the returned UserStorageView instance MUST consider the returned instance as
	 * NOT thread safe.</i>
	 * 
	 * @return A UserStorageView that gives access to User data.
	 */
	public static synchronized UserStorageView getStorageView() {
		locateAndChooseRecordStorageInstanceProvider();
		return instanceProvider.getStorageView();
	}

	private static void locateAndChooseRecordStorageInstanceProvider() {
		if (instanceProvider == null) {
			instanceProvider = moduleInitializer
					.loadOneImplementationBySelectOrder(UserStorageViewInstanceProvider.class);
		}
	}

	static void onlyForTestSetModuleInitializer(ModuleInitializer moduleInitializer) {
		UserStorageProvider.moduleInitializer = moduleInitializer;

	}

	static ModuleInitializer onlyForTestGetModuleInitializer() {
		return moduleInitializer;
	}

	/**
	 * onlyForTestSetUserStorageViewInstanceProvider sets a UserStorageViewInstanceProvider that
	 * will be used to return instances for the {@link #getStorageView()} method. This possibility
	 * to set a UserStorageViewInstanceProvider is provided to enable testing of getting a record
	 * storage in other classes and is not intented to be used in production.
	 * <p>
	 * The UserStorageViewInstanceProvider to use in production should be provided through an
	 * implementation of {@link UserStorageViewInstanceProvider} in a seperate java module.
	 * 
	 * @param instanceProvider
	 *            A UserStorageViewInstanceProvider to use to return UserStorageView instances for
	 *            testing
	 */
	public static void onlyForTestSetUserStorageViewInstanceProvider(
			UserStorageViewInstanceProvider instanceProvider) {
		UserStorageProvider.instanceProvider = instanceProvider;

	}

}
