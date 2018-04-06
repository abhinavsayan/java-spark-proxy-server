package spark.proxy;

import static spark.Spark.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;

public class SparkProxy {

	public static void enableProxy() {

		get("/*", (req, res) -> {
			Request request = Request.Get(url(req));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		post("/*", (req, res) -> {
			Request request = Request.Post(url(req));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		put("/*", (req, res) -> {
			Request request = Request.Put(url(req));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		delete("/*", (req, res) -> {
			Request request = Request.Delete(url(req));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		options("/*", (req, res) -> {
			Request request = Request.Options(url(req));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
		});

		head("/*", (req, res) -> {
			Request request = Request.Head(url(req));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
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

	/*private static String result(HttpResponse response) throws ParseException, IOException {
		HttpEntity entity = response.getEntity();
		return entity == null ? "" : EntityUtils.toString(entity);
	}*/

	private static void extractResponse(HttpResponse httpResponse, HttpServletResponse raw) throws IOException {
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
