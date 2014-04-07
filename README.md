CORS filter
=

Description:
-
This servlet filter adds CORS-specific headers to any server responses.

Init-Params:
-
Possible init parameters are:

| param name | refers to header | default (applies if init-param not set) |
| ------------- |-----------    | ----- |
| allowOrigin | Access-Control-Allow-Origin | * |
| allowMethods | Access-Control-Allow-Methods | GET, POST, PUT, DELETE, OPTIONS |
| allowHeaders | Access-Control-Allow-Headers (HTTP authentication requires value "authorization") | authorization, origin, content-type, accept, x-requested-with, sid, mycustom, smuser |
| maxAge | Access-Control-Max-Age | 1800 |



Usage:
-
Register the filter in your web.xml as follows:

```
<filter>
	<filter-name>CORSFilter</filter-name>
	<filter-class>de.dhbw.corsfilter.CorsFilter</filter-class>
	<init-param>
		<param-name>allowOrigin</param-name>
		<param-value>mydomain.de</param-value>
	</init-param>
</filter>
<filter-mapping>
	<filter-name>CORSFilter</filter-name>
	<url-pattern>/*</url-pattern>
</filter-mapping>
```
