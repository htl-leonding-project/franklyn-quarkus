package at.htl.util;

import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import org.jboss.logmanager.Logger;

public class UtilClass {


    public static String getIpAddress(HttpServerRequest request) {

        var clientIp = request.getHeader("X-Original-Forwarded-For");
        String ip = null;

        if (clientIp != null && !clientIp.isEmpty()) {
            ip = clientIp;

        } else {
            clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp != null && !clientIp.isEmpty()) {
                ip = clientIp;
            } else {

                ip = request.remoteAddress().hostAddress();

            }
        }
        return ip;
    }
}
