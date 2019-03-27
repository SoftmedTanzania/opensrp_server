package org.opensrp.web.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class CorsCommon {

    @RequestMapping(method = RequestMethod.OPTIONS)
    public void catchAllOpt(final HttpServletResponse response)
            throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Max-Age", "120"); // in seconds
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods",
                "HEAD, GET, OPTIONS, POST");
        response.addHeader("Access-Control-Allow-Headers",
                "origin, content-type, accept, x-requested-with");
    }
}
