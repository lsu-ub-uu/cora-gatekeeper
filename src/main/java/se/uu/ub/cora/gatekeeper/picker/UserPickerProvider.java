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
package se.uu.ub.cora.gatekeeper.picker;

import se.uu.ub.cora.initialize.ModuleInitializer;
import se.uu.ub.cora.initialize.ModuleInitializerImp;
import se.uu.ub.cora.initialize.SelectOrder;

/**
 * UserPickerProvider provides access to a {@link UserPicker}.
 * </p>
 * Implementing {@link UserPickerInstanceProvider}s are found using javas module system, and the one
 * with the higest {@link SelectOrder} is used to provide access to record storage.
 */
public class UserPickerProvider {

	private static UserPickerInstanceProvider instanceProvider;
	private static ModuleInitializer moduleInitializer = new ModuleInitializerImp();

	private UserPickerProvider() {
		// prevent call to constructor
		throw new UnsupportedOperationException();
	}

	/**
	 * getUserPicker returns a new UserPicker that can be used by anything that needs access to a
	 * UserPicker.
	 * <p>
	 * <i>Code using the returned UserPicker instance MUST consider the returned instance as NOT
	 * thread safe.</i>
	 * 
	 * @return A UserPicker that can be used to pick a user.
	 */
	public static synchronized UserPicker getUserPicker() {
		locateAndChooseRecordStorageInstanceProvider();
		return instanceProvider.getUserPicker();
	}

	private static void locateAndChooseRecordStorageInstanceProvider() {
		if (instanceProvider == null) {
			instanceProvider = moduleInitializer
					.loadOneImplementationBySelectOrder(UserPickerInstanceProvider.class);
		}
	}

	static void onlyForTestSetModuleInitializer(ModuleInitializer moduleInitializer) {
		UserPickerProvider.moduleInitializer = moduleInitializer;

	}

	static ModuleInitializer onlyForTestGetModuleInitializer() {
		return moduleInitializer;
	}

	/**
	 * onlyForTestSetUserPickerInstanceProvider sets a UserPickerInstanceProvider that will be used
	 * to return instances for the {@link #getUserPicker()} method. This possibility to set a
	 * UserPickerInstanceProvider is provided to enable testing of getting a record storage in other
	 * classes and is not intented to be used in production.
	 * <p>
	 * The UserPickerInstanceProvider to use in production should be provided through an
	 * implementation of {@link UserPickerInstanceProvider} in a seperate java module.
	 * 
	 * @param instanceProvider
	 *            A UserPickerInstanceProvider to use to return UserPicker instances for testing
	 */
	public static void onlyForTestSetUserPickerInstanceProvider(
			UserPickerInstanceProvider instanceProvider) {
		UserPickerProvider.instanceProvider = instanceProvider;

	}

}
