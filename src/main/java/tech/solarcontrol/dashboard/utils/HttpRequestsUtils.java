package tech.solarcontrol.dashboard.utils;

import kong.unirest.HttpRequestWithBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class HttpRequestsUtils {

    public static String createQueryStringForParam(HttpServletRequest request) {
        StringBuffer stringBuffer=new StringBuffer();
        Map<String, String[]> parameterMap = request.getParameterMap();
        if(parameterMap.size()>0){
            stringBuffer.append("?");
            parameterMap.forEach((k,v)->{
                Arrays.asList(v).forEach(value->{
                    stringBuffer.append(k);
                    stringBuffer.append("=");
                    stringBuffer.append(value);
                });
                stringBuffer.append("&");
            });
        }
        String queryString = stringBuffer.toString();
        queryString = replaceEndingFromBuffer(queryString);
        return queryString;
    }

    public static String replaceEndingFromBuffer(String queryString) {
        if(queryString.endsWith("&")){
            queryString = queryString.substring(0, queryString.length()-1);
        }
        return queryString;
    }

    public static void addAuthorizationAndContentTypeHeader(HttpServletRequest request, HttpRequestWithBody httpRequestWithBody) {
        enumerationAsStream(request.getHeaderNames())
                .filter(header-> header.equalsIgnoreCase("authorization") || header.equalsIgnoreCase("content-type"))
                .forEach(headerName-> httpRequestWithBody.header(headerName, request.getHeader(headerName)));
    }

    public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                        new Iterator<T>() {
                            public T next() {
                                return e.nextElement();
                            }
                            public boolean hasNext() {
                                return e.hasMoreElements();
                            }
                        },
                        Spliterator.ORDERED), false);
    }
}
