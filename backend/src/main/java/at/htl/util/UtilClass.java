package at.htl.util;

import io.vertx.core.http.HttpServerRequest;

public class UtilClass {

    public static String getIpAddress(HttpServerRequest request) {
        var clientIp = request.getHeader("X-Original-Forwarded-For");
        String ip = null;

        if (clientIp != null && !clientIp.isEmpty()) {
            ip = clientIp;

        }
        return ip;
    }
}
