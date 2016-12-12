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

package se.uu.ub.cora.gatekeeper.authentication;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import se.uu.ub.cora.gatekeeper.http.HttpHandler;
import se.uu.ub.cora.gatekeeper.http.HttpHandlerImp;

public class HttpHandlerFactoryImp implements HttpHandlerFactory {

	@Override
	public HttpHandler factor(String urlString) {
		// TODO Auto-generated method stub
		URL url;
		HttpURLConnection urlConnection = null;
		try {
			url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return HttpHandlerImp.usingURLConnection(urlConnection);
	}

}
