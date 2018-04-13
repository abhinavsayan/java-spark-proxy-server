package server;

import static spark.Spark.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;

import spark.Service;

import static server.Util.*;

public class ProxyServer {

	/**
	 * 
	 * @param service
	 *            - instance of Service for which proxy has to be enabled
	 * @param proxyServer
	 *            - URL of the server to proxy requests to
	 * @param proxyPath
	 *            - local path for which requests have to be forwarded; null = '/'
	 */
	public static void enableProxy(Service service, String proxyServer, String proxyPath) {

		String path= formatPath(proxyPath);
		String pathFilter = proxyPath + "*";

		service.get(pathFilter, (req, res) -> {
			Request request = Request.Get(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		service.post(pathFilter, (req, res) -> {
			Request request = Request.Post(url(req, proxyServer, path));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		service.put(pathFilter, (req, res) -> {
			Request request = Request.Put(url(req, proxyServer, path));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		service.delete(pathFilter, (req, res) -> {
			Request request = Request.Delete(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		service.options(pathFilter, (req, res) -> {
			Request request = Request.Options(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
		});

		service.head(pathFilter, (req, res) -> {
			Request request = Request.Head(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
		});

	}

	/**
	 * 
	 * @param proxyServer
	 *            - URL of the server to proxy requests to
	 * @param proxyPath
	 *            - local path for which requests have to be forwarded; null = '/'
	 */
	public static void enableProxy(String proxyServer, String proxyPath) {
		
		String path = formatPath(proxyPath);
		String pathFilter = proxyPath + "*";

		get(pathFilter, (req, res) -> {
			Request request = Request.Get(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		post(pathFilter, (req, res) -> {
			Request request = Request.Post(url(req, proxyServer, path));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		put(pathFilter, (req, res) -> {
			Request request = Request.Put(url(req, proxyServer, path));
			addBody(request, req);
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		delete(pathFilter, (req, res) -> {
			Request request = Request.Delete(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			extractResponse(response, res.raw());
			return res.raw();
		});

		options(pathFilter, (req, res) -> {
			Request request = Request.Options(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
		});

		head(pathFilter, (req, res) -> {
			Request request = Request.Head(url(req, proxyServer, path));
			addHeader(request, req);
			HttpResponse response = go(request);
			mapHeaders(response, res);
			mapStatus(response, res);
			return res.raw();
		});

	}
}
