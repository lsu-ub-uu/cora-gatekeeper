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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ServiceLoader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

@WebListener
public class GatekeeperModuleInitializer implements ServletContextListener {
	private ServletContext servletContext;
	private HashMap<String, String> initInfo = new HashMap<>();
	// TODO: make call to a new class InitializerPartTwo
	// that takes the collected initInfo and an Iterable with the found services
	// let this initializerPartTwo set as this currently does
	private GatekeeperModuleStarter starter;
	private Iterable<UserPickerProvider> implementations;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext = arg0.getServletContext();
		initializeGatekeeper();
	}

	private void initializeGatekeeper() {
		collectInitInformation();
		collectImplementations();
		createGatekeeperStarter();
		starter.startProvider();
	}

	private void collectInitInformation() {
		Enumeration<String> initParameterNames = servletContext.getInitParameterNames();
		while (initParameterNames.hasMoreElements()) {
			String key = initParameterNames.nextElement();
			initInfo.put(key, servletContext.getInitParameter(key));
		}
	}

	private void collectImplementations() {
		implementations = ServiceLoader.load(UserPickerProvider.class);
	}

	private void createGatekeeperStarter() {
		starter = GatekeeperModuleStarter.usingInitInfoAndImplementations(initInfo,
				implementations);
	}

	GatekeeperModuleStarter getStarter() {
		// needed for test
		return starter;
	}
}
