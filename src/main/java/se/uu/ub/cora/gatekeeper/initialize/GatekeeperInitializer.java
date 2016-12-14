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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperLocator;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperLocatorImp;
import se.uu.ub.cora.gatekeeperinterface.UserPickerFactory;

@WebListener
public class GatekeeperInitializer implements ServletContextListener {
	private UserPickerFactory userPickerFactory;
	private ServletContext servletContext;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext = arg0.getServletContext();
		try {
			tryToInitialize();
		} catch (Exception e) {
			throw new RuntimeException("Error starting Gatekeeper: " + e.getMessage());
		}
	}

	private void tryToInitialize()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		String userPickerFactoryString = getClassNameToInitializeAsUserPickerFactoryFromContext();
		createInstanceOfUserPickerFactoryClass(userPickerFactoryString);

		GatekeeperLocator locator = new GatekeeperLocatorImp();
		GatekeeperInstanceProvider.setGatekeeperLocator(locator);
		GatekeeperImp.INSTANCE.setUserPickerFactory(userPickerFactory);
	}

	private String getClassNameToInitializeAsUserPickerFactoryFromContext() {
		String initParameter = servletContext.getInitParameter("userPickerFactoryClassName");
		if (initParameter == null) {
			throw new RuntimeException("Context must have a userPickerFactoryClassName set.");
		}
		return initParameter;
	}

	private void createInstanceOfUserPickerFactoryClass(String userPickerFactoryString)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Object newInstance = Class.forName(userPickerFactoryString).newInstance();
		userPickerFactory = (UserPickerFactory) newInstance;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// not sure we need anything here
	}
}
