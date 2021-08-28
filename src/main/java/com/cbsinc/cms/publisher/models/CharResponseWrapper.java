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
import org.springframework.stereotype.Component;

@Component
public class CharResponseWrapper extends HttpServletResponseWrapper {

	private CharArrayWriter output;
	private PrintWriter printWriter;

	public String toString() {
		return output.toString();
	}

	public CharResponseWrapper(HttpServletResponse response) {
		super(response);
		output = new CharArrayWriter();
	}

	public PrintWriter getWriter() {
		return new PrintWriter(output);
	}

	public char[] getData() {
		return output.toCharArray();
	}

	// toCharArray()

	public void close() {
		if (output != null)
			output.close();
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
