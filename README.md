# java-spark-proxy-server
A proxy server built on spark java.
This is a minimalistic proxy server over java. Allows you to forward and mask requests to other servers.

The server can be initiated as follows in static version : 

```
port(80);
ProxyServer.enableProxy("http://myproxy.site", "/");
```

If you are running spark by creating the service instance.
```
Service service = Service.ignite();
service.port(80);
ProxyServer.enableProxy(service, "http://myproxy.site", "/");
```

The last argument is the path argument that defines the ath on your server for which the requests will be forwarded to the proxy server.

Example Usage :
I created this project for a prticular scenario.
My application backend is on spark java and frondend is on Angular 4. Developing both was a headache as I had to enable CROS and also take care of authentication for serving static content.
So after I had defined all my routes in spark, I add the following statement, so that any unmached requests(the UI requests) will be sent to the Angular 4 instance server.
```
ProxyServer.enableProxy("http://localhost:4200", "/");
```
