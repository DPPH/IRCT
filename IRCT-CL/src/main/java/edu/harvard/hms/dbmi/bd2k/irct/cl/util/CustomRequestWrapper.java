/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package edu.harvard.hms.dbmi.bd2k.irct.cl.util;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * Class which is used to wrap a request in order that the wrapped request's input stream can be
 * read once and later be read again in a pseudo fashion by virtue of keeping the original payload
 * as a string which is actually what is returned by subsequent calls to getInputStream().
 */
public class CustomRequestWrapper
		extends HttpServletRequestWrapper {

	private static Logger log = Logger.getLogger(CustomRequestWrapper.class.getName());

	public final String payload;

	public CustomRequestWrapper (HttpServletRequest request)
			throws Exception {

		super(request);

		// read the original payload into the payload variable
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			// read the payload into the StringBuilder
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				// make an empty string since there is no payload
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			log.error("Error reading the request payload", ex);
			throw new Exception("Error reading the request payload", ex);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException iox) {
					// ignore
				}
			}
		}
		payload = stringBuilder.toString();
	}

	/**
	 * Override of the getInputStream() method which returns an InputStream that reads from the
	 * stored XML payload string instead of from the request's actual InputStream.
	 */
	@Override
	public ServletInputStream getInputStream ()
			throws IOException {

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(payload.getBytes());
		ServletInputStream inputStream = new ServletInputStream() {
			public int read ()
					throws IOException {
				return byteArrayInputStream.read();
			}
		};
		return inputStream;
	}

}