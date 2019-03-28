package org.opensrp.web.cros;

import com.google.gson.Gson;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The purpose of this class is to emulate the Spring 4 annotation @CORSFilter - using the power of Spring 3
 * Note that is is constrained to non-prod environments
 */
public class Spring3CorsFilterHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {


        System.out.println("COR : handler called");
        if (handler instanceof HandlerMethod) {
            System.out.println("COR : Spring3CorsFilterHandlerInterceptor");
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // Test if the controller-method is annotated with @Spring3CORSFilter
            Spring3CorsFilter filter = handlerMethod.getMethod().getAnnotation(Spring3CorsFilter.class);
            if (filter != null ) {
                System.out.println("COR : filtering headers");
                // ... do the filtering
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "POST, GET, HEAD,PUT, OPTIONS, DELETE");
                response.addHeader("Access-Control-Max-Age", "3600");
                response.addHeader("Access-Control-Allow-Headers", "x-requested-with");
                response.addHeader("Access-Control-Allow-Headers", "Content-Type");

            }
        }
        return true;
    }
}