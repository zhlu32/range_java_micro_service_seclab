package com.mdsec.microserviceseclab.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rce")
public class RceController {

    // POC: curl -d "command=/bin/bash" -X POST localhost:8080/rce/one
    // processBuilder导致的RCE
    @RequestMapping(value = "one")
    public StringBuffer One(@RequestParam(value = "command") String command) {
        StringBuffer sb = new StringBuffer();
        List<String> commands = new ArrayList<>();
        commands.add(command);

        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            sb.append(e.toString());
        }
        return sb;
    }

    // POC: curl -d "command=ls -al" -X POST localhost:8080/rce/two
    // Runtime.getRuntime().exec(args)导致的RCE
    @RequestMapping(value = "two")
    public StringBuffer Two(@RequestParam(value="command") String command) {
        String cmd = "";
        StringBuffer result = new StringBuffer();
        try {
            cmd = String.format("%s", command);
            System.out.println(cmd);
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream stdIn = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdIn);
            String line = null;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null){
                result.append(line + "\n");
            }
            process.waitFor(50, TimeUnit.SECONDS);
        } catch (Throwable e) {
            return null;
        }
        System.out.println(result);
        return result;
    }
}

