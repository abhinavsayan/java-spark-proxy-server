package spark.proxy;

import static spark.Spark.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import spark.utils.IOUtils;

public class SparkProxy {

	public static void enableProxy() {

		get("/*", (req, res) -> {
			Request request = Request.Get(url(req));
			addHeader(request, req);
			HttpResponse response = go(request);
			System.out.println(response.getStatusLine().getStatusCode());
			mapHeaders(response, res);
			mapStatus(response, res);
			// mapContentType(res, req.pathInfo());
			extractResponse(response, res.raw());
			// return result(response);
			return res.raw();
		});

		post("/*", (req, res) -> {
			Request request = Request.Post(url(req));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			// mapContentType(res, req.pathInfo());
			// extractResponse(response, res.raw());
			return result(response);
			// return res.raw();
		});
	}

	private static String url(spark.Request req) {
		String proxyUrl = "http://localhost:4200" + req.pathInfo();// .replace(settings.getString("server.proxy"), "")
		System.out.println(proxyUrl);
		return (req.queryString() == null) ? proxyUrl : (proxyUrl + "?" + req.queryString());
	}

	private static void addHeader(Request request, spark.Request req) {
		for (String header : req.headers()) {
			if (!header.equals("Content-Length"))
				request.setHeader(header, req.headers(header));
		}
	}

	private static void addBody(Request request, spark.Request req) {
		request.bodyByteArray(req.bodyAsBytes());
	}

	private static HttpResponse go(Request request) throws ClientProtocolException, IOException {
		return request.execute().returnResponse();
	}

	private static void mapHeaders(HttpResponse response, spark.Response res) {
		for (Header header : response.getAllHeaders()) {
			res.header(header.getName(), header.getValue());
		}
	}

	private static void mapStatus(HttpResponse response, spark.Response res) {
		res.status(response.getStatusLine().getStatusCode());
	}

	private static void mapContentType(spark.Response res, String path) {

		System.out.println("++" + path);
		if (path.endsWith("woff"))
			res.type("font/woff");
		if (path.endsWith("font/woff"))
			res.type("font/woff2");
		if (path.endsWith("ttf"))
			res.type("application/octet-stream");
	}

	private static String result(HttpResponse response) throws ParseException, IOException {
		HttpEntity entity = response.getEntity();
		return entity == null ? "" : EntityUtils.toString(entity);
	}

	private static void extractResponse(HttpResponse httpResponse, HttpServletResponse response) {
		InputStream inputStream = null;
		try {

			HttpEntity entity = httpResponse.getEntity();
			if (entity == null)
				return;
			inputStream = entity.getContent();

			// String responseStr = EntityUtils.toString(httpResponse.getEntity()); //Get
			// the contect from httpresponse, it has the value I want

			copyStream(inputStream, response.getOutputStream());
		} catch (IllegalStateException | IOException e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static void copyStream(InputStream input, OutputStream output) throws IOException {

		// input.transferTo(output);
		
		// output.write(input.readAllBytes());

		//ByteArrayOutputStream buffer = (ByteArrayOutputStream) output;
		//byte[] bytes = buffer.toByteArray();
		//input = new ByteArrayInputStream(bytes);

		IOUtils.copy(input, output);
	}
}
