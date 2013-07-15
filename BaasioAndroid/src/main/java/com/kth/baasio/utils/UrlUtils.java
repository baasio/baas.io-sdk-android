
package com.kth.baasio.utils;

import static java.net.URLEncoder.encode;

import com.kth.baasio.exception.BaasioRuntimeException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UrlUtils {

    public static URL url(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new BaasioRuntimeException("Incorrect URL format", e);
        }
    }

    public static URL url(URL u, String s) {
        try {
            return new URL(u, s);
        } catch (MalformedURLException e) {
            throw new BaasioRuntimeException("Incorrect URL format", e);
        }
    }

    public static String path(Object... segments) {
        String path = "";
        boolean first = true;
        for (Object segment : segments) {
            if (segment instanceof Object[]) {
                segment = path((Object[])segment);
            }
            if (!ObjectUtils.isEmpty(segment)) {
                if (first) {
                    path = segment.toString();
                    first = false;
                } else {
                    if (!path.endsWith("/")) {
                        path += "/";
                    }
                    path += segment.toString();
                }
            }
        }
        return path;
    }

    @SuppressWarnings("rawtypes")
    public static String encodeParams(Map<String, Object> params) {
        if (params == null) {
            return "";
        }
        boolean first = true;
        StringBuilder results = new StringBuilder();
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof List) {
                for (Object o : (List)entry.getValue()) {
                    if (!ObjectUtils.isEmpty(o)) {
                        if (!first) {
                            results.append('&');
                        }
                        first = false;
                        results.append(entry.getKey());
                        results.append("=");
                        try {
                            results.append(encode(o.toString(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            throw new BaasioRuntimeException("Unknown encoding", e);
                        }
                    }
                }
            } else if (!ObjectUtils.isEmpty(entry.getValue())) {
                if (!first) {
                    results.append('&');
                }
                first = false;
                results.append(entry.getKey());
                results.append("=");
                try {
                    results.append(encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new BaasioRuntimeException("Unsupported string encoding", e);
                }
            }
        }
        return results.toString();
    }

    public static String addQueryParams(String url, Map<String, Object> params) {
        if (params == null) {
            return url;
        }
        if (!url.contains("?")) {
            url += "?";
        }
        url += encodeParams(params);
        return url;
    }

}
