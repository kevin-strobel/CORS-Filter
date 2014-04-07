package de.dhbw.corsfilter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet filter for Cross-Origin resource sharing requests.
 * 
 * @author Kevin Strobel
 * @author Marcel Härle
 * 
 */
public class CorsFilter implements Filter {

    private static final String CORS_PREFLIGHT_METHOD = "OPTIONS";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    private static final String ALLOW_ORIGIN_PARAM = "allowOrigin";
    private static final String ALLOW_METHODS_PARAM = "allowMethods";
    private static final String ALLOW_HEADERS_PARAM = "allowHeaders";
    private static final String MAX_AGE_PARAM = "maxAge";

    private String accessControlAllowOriginValue = "*";
    private String accessControlAllowMethodsValue = "GET, POST, PUT, DELETE, OPTIONS";
    private String accessControlAllowHeadersValue = "authorization, origin, content-type, accept, x-requested-with, sid, mycustom, smuser";
    private String accessControlMaxAgeValue = "1800";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final String allowOriginValueParam = filterConfig
                .getInitParameter(ALLOW_ORIGIN_PARAM);
        if (allowOriginValueParam != null) {
            accessControlAllowOriginValue = allowOriginValueParam;
        }

        final String allowMethodsParam = filterConfig
                .getInitParameter(ALLOW_METHODS_PARAM);
        if (allowMethodsParam != null) {
            accessControlAllowMethodsValue = allowMethodsParam;
        }

        final String allowHeadersParam = filterConfig
                .getInitParameter(ALLOW_HEADERS_PARAM);
        if (allowHeadersParam != null) {
            accessControlAllowHeadersValue = allowHeadersParam;
        }

        final String maxAgeParam = filterConfig.getInitParameter(MAX_AGE_PARAM);
        if (maxAgeParam != null) {
            accessControlMaxAgeValue = maxAgeParam;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)
                || !(response instanceof HttpServletResponse)) {
            throw new ServletException(
                    "CorsFilter just supports HTTP requests");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getHeader(ACCESS_CONTROL_REQUEST_METHOD) != null
                && CORS_PREFLIGHT_METHOD.equals(httpRequest.getMethod())) {
            // CORS "pre-flight" request (using OPTIONS)
            httpResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN,
                    accessControlAllowOriginValue);
            httpResponse.addHeader(ACCESS_CONTROL_ALLOW_METHODS,
                    accessControlAllowMethodsValue);
            httpResponse.addHeader(ACCESS_CONTROL_ALLOW_HEADERS,
                    accessControlAllowHeadersValue);
            httpResponse.addHeader(ACCESS_CONTROL_MAX_AGE,
                    accessControlMaxAgeValue);
        }

        if (!CORS_PREFLIGHT_METHOD.equals(httpRequest.getMethod())) {
            // not an OPTIONS request (pre-flight) - only send access control
            // allow origin
            httpResponse.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN,
                    accessControlAllowOriginValue);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nothing to do.
    }

}