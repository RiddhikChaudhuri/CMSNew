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

import java.io.OutputStream;
import java.io.IOException;

class ConvertedOutputStream extends OutputStream {
	OutputStream outstream;

	ConvertedOutputStream(OutputStream outstream) {
		this.outstream = outstream;
	}

	public void flush() throws IOException {
		outstream.flush();
	}

	private int prev = 0;

	public void write(int b) throws IOException {
		if (b == '\n') {
			if (prev != '\r')
				outstream.write('\r');
		} else if (b == '\r') {
			if (prev != '\n')
				outstream.write('\n');
		}
		prev = b;
		outstream.write(b);
	}
}
