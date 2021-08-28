package com.cbsinc.cms.publisher.models;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * You can not use it and you cannot change it without written permission from Konstantin Grabko
 * Email: konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2014
 * </p>
 * <p>
 * Company: CENTER BUSINESS SOLUTIONS INC 
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */

import java.io.*;

import javax.servlet.http.*;

public class ByteResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream output;
	private PrintWriter printWriter;

	public String toString() {
		return output.toString();
	}

	public ByteResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new ByteArrayOutputStream();
	}

	public PrintWriter getWriter() {
		return new PrintWriter(output);
	}

	public byte[] getData() {
		return output.toByteArray();
	}

	// toCharArray()

	public void close() {
		if (output != null)
			try {
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (printWriter != null)
			printWriter.close();
	}

	protected void finalize() throws Throwable {
		if (output != null)
			output.close();
		if (printWriter != null)
			printWriter.close();
		super.finalize();
	}

}
