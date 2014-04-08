package de.dhbw.corsfilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Test case for {@link CorsFilter}.
 * 
 * @author Marcel Härle
 * 
 */
public class CorsFilterTest {

    private CorsFilter corsFilter;

    @Mock
    private FilterConfig filterConfig;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;

    private final String allowOriginParam = "mydomain.com";
    private final String allowMethodsParams = "GET";
    private final String allowHeadersParams = "authorization, origin, content-type";
    private final String maxAgeParam = "maxAge";

    @Before
    public void setUp() throws ServletException {
        MockitoAnnotations.initMocks(this);
        corsFilter = new CorsFilter();

        Mockito.when(filterConfig.getInitParameter("allowOrigin")).thenReturn(
                allowOriginParam);
        Mockito.when(filterConfig.getInitParameter("allowMethods")).thenReturn(
                allowMethodsParams);
        Mockito.when(filterConfig.getInitParameter("allowHeaders")).thenReturn(
                allowHeadersParams);
        Mockito.when(filterConfig.getInitParameter("maxAge")).thenReturn(
                maxAgeParam);

        corsFilter.init(filterConfig);
    }

    @Test
    public void testInitAndCorsPreFlightRequest() throws ServletException,
            IOException {
        Mockito.when(request.getHeader("Access-Control-Request-Method"))
                .thenReturn("GET");
        Mockito.when(request.getMethod()).thenReturn("OPTIONS");

        corsFilter.doFilter(request, response, filterChain);

        Mockito.verify(response).addHeader("Access-Control-Allow-Origin",
                allowOriginParam);
        Mockito.verify(response).addHeader("Access-Control-Allow-Methods",
                allowMethodsParams);
        Mockito.verify(response).addHeader("Access-Control-Allow-Headers",
                allowHeadersParams);
        Mockito.verify(response).addHeader("Access-Control-Max-Age",
                maxAgeParam);

        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testInitAndCorsNotPreFlightRequest() throws ServletException,
            IOException {
        Mockito.when(request.getHeader("Access-Control-Request-Method"))
                .thenReturn("GET");
        Mockito.when(request.getMethod()).thenReturn("POST");

        corsFilter.doFilter(request, response, filterChain);

        Mockito.verify(response).addHeader("Access-Control-Allow-Origin",
                allowOriginParam);

        Mockito.verify(filterChain).doFilter(request, response);
    }

}
