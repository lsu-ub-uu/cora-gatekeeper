/*
 * Copyright 2016, 2018 Uppsala University Library
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import se.uu.ub.cora.gatekeeper.GatekeeperImp;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperInstanceProvider;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperLocator;
import se.uu.ub.cora.gatekeeper.dependency.GatekeeperLocatorImp;
import se.uu.ub.cora.gatekeeper.user.UserPicker;
import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

@WebListener
public class GatekeeperInitializer implements ServletContextListener {
	public UserPickerProvider userPickerProvider;
	private ServletContext servletContext;
	private HashMap<String, String> initInfo;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext = arg0.getServletContext();
		try {
			tryToInitialize();
		} catch (InvocationTargetException e) {
			throw new RuntimeException(
					"Error starting Gatekeeper: " + e.getTargetException().getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Error starting Gatekeeper: " + e.getMessage());
		}
	}

	private void tryToInitialize() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		createInstanceOfUserPickerProvider();
		String userPickerProviderString = getClassNameToInitializeAsUserPickerProviderFromContext();
		collectInitInformation();
		createInstanceOfUserPickerProviderClass(userPickerProviderString);

		GatekeeperLocator locator = new GatekeeperLocatorImp();
		GatekeeperInstanceProvider.setGatekeeperLocator(locator);
		GatekeeperImp.INSTANCE.setUserPickerProvider(userPickerProvider);
	}

	private void createInstanceOfUserPickerProvider() {
		// TODO Auto-generated method stub
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		ServiceLoader<UserPickerProvider> loader = ServiceLoader.load(UserPickerProvider.class,
				contextClassLoader);
		for (UserPickerProvider userPickerProvider : loader) {
			// TODO:
			UserPicker userPicker = userPickerProvider.getUserPicker();
			String s = "";
			s += "";
		}

	}

	private String getClassNameToInitializeAsUserPickerProviderFromContext() {
		String initParameter = servletContext.getInitParameter("userPickerProviderClassName");
		if (initParameter == null) {
			throw new RuntimeException("Context must have a userPickerProviderClassName set.");
		}
		return initParameter;
	}

	private void collectInitInformation() {
		initInfo = new HashMap<>();
		Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
		while (initParameterNames.hasMoreElements()) {
			String key = initParameterNames.nextElement();
			initInfo.put(key, servletContext.getInitParameter(key));
		}
	}

	private void createInstanceOfUserPickerProviderClass(String userPickerProviderString)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, InvocationTargetException {
		Constructor<?> constructor = Class.forName(userPickerProviderString)
				.getConstructor(Map.class);
		userPickerProvider = (UserPickerProvider) constructor.newInstance(initInfo);
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// not sure we need anything here
	}
}
