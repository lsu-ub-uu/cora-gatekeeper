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

import java.util.Map;

import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

public class GatekeeperModuleStarter {

	public static GatekeeperModuleStarter usingInitInfoAndImplementations(
			Map<String, String> initInfo, Iterable<UserPickerProvider> implementations) {
		return new GatekeeperModuleStarter(initInfo, implementations);
	}

	private Map<String, String> initInfo;
	private Iterable<UserPickerProvider> implementations;

	private GatekeeperModuleStarter(Map<String, String> initInfo,
			Iterable<UserPickerProvider> implementations) {
		this.initInfo = initInfo;
		this.implementations = implementations;
	}

	public void startProvider() {
		// Iterator<UserPickerProvider> implementationsIterator =
		// implementations.iterator();
		// boolean noImplementationsFound = !implementationsIterator.hasNext();
		// if (noImplementationsFound) {
		// throw new GatekeeperInitializationException(
		// "No implementations found for UserPickerProvider");
		// }
		// UserPickerProvider userPickerProvider = implementationsIterator.next();
		// userPickerProvider.startUsingInitInfo(initInfo);
		// if (implementationsIterator.hasNext()) {
		// throw new GatekeeperInitializationException(
		// "More than one implementation found for UserPickerProvider");
		// }

		UserPickerProvider userPickerProvider = getImplementationThrowErrorIfNoneOrMoreThanOne();
		userPickerProvider.startUsingInitInfo(initInfo);

	}

	private UserPickerProvider getImplementationThrowErrorIfNoneOrMoreThanOne() {
		UserPickerProvider userPickerProvider = null;
		int noOfImplementationsFound = 0;
		for (UserPickerProvider userPickerProvider2 : implementations) {
			noOfImplementationsFound++;
			userPickerProvider = userPickerProvider2;
		}
		throwErrorIfNone(noOfImplementationsFound);
		throwErrorIfMoreThanOne(noOfImplementationsFound);
		return userPickerProvider;
	}

	private void throwErrorIfNone(int noOfImplementationsFound) {
		if (noOfImplementationsFound == 0) {
			throw new GatekeeperInitializationException(
					"No implementations found for UserPickerProvider");
		}
	}

	private void throwErrorIfMoreThanOne(int noOfImplementationsFound) {
		if (noOfImplementationsFound > 1) {
			throw new GatekeeperInitializationException(
					"More than one implementation found for UserPickerProvider");
		}
	}

	public Map<String, String> getInitInfo() {
		// needed for test
		return initInfo;
	}

	public Iterable<UserPickerProvider> getImplementations() {
		// needed for test
		return implementations;
	}

}
