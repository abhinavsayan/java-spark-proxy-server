package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

class Util {
	
	static String formatPath(String proxyPath) {
		if (proxyPath == null)
			proxyPath = "/";
		if (!proxyPath.endsWith("/"))
			proxyPath += "/";
		return proxyPath;
	}
	
	static String url(spark.Request req, String proxyServer, String proxyPath) {
		String proxyUrl = proxyServer + "/"  + req.pathInfo().replace(proxyPath, "");
		return (req.queryString() == null) ? proxyUrl : (proxyUrl + "?" + req.queryString());
	}

	static void addHeader(Request request, spark.Request req) {
		for (String header : req.headers()) {
			if (!header.equals("Content-Length"))
				request.setHeader(header, req.headers(header));
		}
	}

	static void addBody(Request request, spark.Request req) {
		request.bodyByteArray(req.bodyAsBytes());
	}

	static HttpResponse go(Request request) throws ClientProtocolException, IOException {
		return request.execute().returnResponse();
	}

	static void mapHeaders(HttpResponse response, spark.Response res) {
		for (Header header : response.getAllHeaders()) {
			res.header(header.getName(), header.getValue());
		}
	}

	static void mapStatus(HttpResponse response, spark.Response res) {
		res.status(response.getStatusLine().getStatusCode());
	}

	/*
	 * This had issues with binary data
	 * private static String result(HttpResponse response) throws ParseException,
	 * IOException { HttpEntity entity = response.getEntity(); return entity == null
	 * ? "" : EntityUtils.toString(entity); }
	 */

	static void extractResponse(HttpResponse httpResponse, HttpServletResponse raw) throws IOException {
		HttpEntity entity = httpResponse.getEntity();
		if (entity == null)
			return;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		entity.writeTo(baos);
		byte[] bytes = baos.toByteArray();
		raw.getOutputStream().write(bytes);
		raw.getOutputStream().flush();
		raw.getOutputStream().close();
	}
}
