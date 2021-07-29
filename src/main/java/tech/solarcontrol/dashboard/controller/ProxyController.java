package tech.solarcontrol.dashboard.controller;

import kong.unirest.HttpRequestWithBody;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tech.solarcontrol.dashboard.models.LoginDTO;
import tech.solarcontrol.dashboard.utils.HttpRequestsUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
public class ProxyController {

    @Value("${balena.api}")
    private String balenaURL;

    @RequestMapping(value = "/login_", method = RequestMethod.POST)
    public ResponseEntity<?> loginRoute(@RequestBody LoginDTO loginAndPassword) {
        HttpResponse<String> stringHttpResponse = Unirest.post(balenaURL + "/login_")
                .body(loginAndPassword).header("content-type", "application/json").asString();
        return new ResponseEntity<>(stringHttpResponse.getBody(), HttpStatus.valueOf(stringHttpResponse.getStatus()));
    }

    @RequestMapping(value = {"/v6/**","/device-types/**", "/device/**","/supervisor/**"}, method = {RequestMethod.POST,RequestMethod.PUT, RequestMethod.PATCH}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public String processRouteForPost (HttpServletRequest request, @RequestBody Object body) throws IOException {
        HttpResponse<String> httpResponse = proxyRequestToOpenBalena(request, body);
        return httpResponse.getBody();
    }

    @RequestMapping(value = {"/v6/**","/device-types/**", "/device/**","/supervisor/**"}, method = {RequestMethod.GET,RequestMethod.DELETE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public String processRouteForGet (HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpResponse<String> httpResponse = proxyRequestToOpenBalena(request, response);
        return httpResponse.getBody();
    }

    private HttpResponse<String> proxyRequestToOpenBalena(HttpServletRequest request, Object body) {
        String queryString = HttpRequestsUtils.createQueryStringForParam(request);
        String finalURL = balenaURL + request.getRequestURI() + queryString;
        HttpRequestWithBody httpRequestWithBody = Unirest.request(request.getMethod(), finalURL);
        HttpRequestsUtils.addAuthorizationAndContentTypeHeader(request, httpRequestWithBody);
        return httpRequestWithBody.body(body).asString();
    }

    private HttpResponse<String> proxyRequestToOpenBalena(HttpServletRequest request, HttpServletResponse response) {
        String queryString = HttpRequestsUtils.createQueryStringForParam(request);
        String finalURL = balenaURL + request.getRequestURI() + queryString;
        HttpRequestWithBody httpRequestWithBody = Unirest.request(request.getMethod(), finalURL);
        HttpRequestsUtils.addAuthorizationAndContentTypeHeader(request, httpRequestWithBody);
        HttpResponse<String> httpResponse = httpRequestWithBody.asString();
        response.setContentType(httpResponse.getHeaders().getFirst("contentType"));
        return httpResponse;
    }



}
