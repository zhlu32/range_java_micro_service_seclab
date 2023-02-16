package com.mdsec.microserviceseclab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/redirect")
public class OpenRedirectorController {

    @RequestMapping(value = "/v1")
    public ModelAndView V1(@RequestParam(value = "url") String url) {
        return new ModelAndView("redirect:" + url);
    }

    @RequestMapping(value = "/v2")
    public void V2(@RequestParam(value = "url") String url, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }

    @RequestMapping(value = "/v3")
    public void V3(@RequestParam(value = "url") String url, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", url);
    }
}
