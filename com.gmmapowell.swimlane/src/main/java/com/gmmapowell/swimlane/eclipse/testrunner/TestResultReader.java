package com.gmmapowell.swimlane.eclipse.testrunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestResultReader implements Runnable {
	private final TestResultAnalyzer analyzer;
	private boolean done;
	private AtomicInteger port = null;
	private CountDownLatch latch = new CountDownLatch(1);
	private ServerSocket sock;
	private List<Socket> open = new ArrayList<>();

	public TestResultReader(TestResultAnalyzer analyzer) {
		this.analyzer = analyzer;
	}

	public int getPort() {
		try {
			latch.await(1, TimeUnit.SECONDS);
			return port.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("The port was not set");
		}
	}
	
	@Override
	public void run() {
		try {
			sock = new ServerSocket(0);
			port = new AtomicInteger(sock.getLocalPort());
			System.out.println("port = " + port);
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (!done && !sock.isClosed()) {
			Socket conn = null;
			try {
				System.out.println("Accepting connection on " + port);
				conn = sock.accept();
			} catch (IOException ex) {
				if (sock.isClosed())
					break;
				ex.printStackTrace();
				continue;
			}
			try {
				System.out.println("Have connection on " + port);
				open.add(conn);
				InputStream is = conn.getInputStream();
				InputStreamReader r = new InputStreamReader(is);
				LineNumberReader lnr = new LineNumberReader(r);
				String s;
				while ((s = lnr.readLine()) != null) {
					analyzer.push(s);
				}
				lnr.close();
				System.out.println("Completed normally");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
						open.remove(conn);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		
		try {
			if (!sock.isClosed())
				sock.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void done() throws IOException {
		this.done = true;
		sock.close();
		System.out.println("Closing " + open.size() + " open connections");
		for (Socket s : open)
			s.close();
	}
}
