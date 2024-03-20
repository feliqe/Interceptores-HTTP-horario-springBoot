package com.feliqe.springboot.calendar.interceptor.springboothorario.interceptors;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CalendarInterceptor implements HandlerInterceptor{

    //exportamos los datos de application.properties
    @Value("${config.calendar.open}")
    private Integer open;

    @Value("${config.calendar.close}")
    private Integer close;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        //vamos atner los datos de calendario por el componente calendario
        Calendar calendar = Calendar.getInstance();
        //extraimas la hora del dia
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        //imprimir hora en la terminal
        System.out.println(hour);

        //realizamos la condicion de la hora de apertura y la de cierre
        if (hour >= open && hour < close) {
            //mostramos unos mensajes indicando el horario de la tienda
            StringBuilder message = new StringBuilder("Bienvenidos al horario de atencion a clientes");
            message.append(", atendemos desde las ");
            message.append(open);
            message.append("hrs.");
            message.append(" hasta las ");
            message.append(close);
            message.append("hrs.");
            message.append(" Gracias por si visita!");
            request.setAttribute("message", message.toString());
            return true;
        }

        //cuando no se cumple con la condicion se enviara esta respuesta
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> data = new HashMap<>();
        StringBuilder message = new StringBuilder("Cerrado, fuera del horario de atencion");
        message.append(" por favor visitenos desde las ");
        message.append(open);
        message.append(" y las ");
        message.append(close);
        message.append("hrs. Gracias!");
        data.put("message", message.toString());
        data.put("date", new Date().toString());
        response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(mapper.writeValueAsString(data));
        return false;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) throws Exception {

    }
}
