package com.samples.web;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.samples.S3Counter;

/**
 * Servlet implementation class FileCounterServlet
 */
@WebServlet("/S3CounterServlet")
public class S3CounterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private S3Counter fileCounter;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public S3CounterServlet() {
        super();
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
    	fileCounter = new S3Counter();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.getSession(true);
		response.setContentType("text/html");
		ServletOutputStream out = response.getOutputStream();
		int count = fileCounter.getCounter(true);
		getServletContext().log("New counter value: " + count);
		out.print("" + count);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

}
