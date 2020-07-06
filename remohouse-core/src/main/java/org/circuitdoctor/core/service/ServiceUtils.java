package org.circuitdoctor.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceUtils {

    private static final Logger log = LoggerFactory.getLogger(ServiceUtils.class);
    public ServiceUtils() {
    }
    public String sendMessage(String message,String phoneNumber) {
        log.trace("sendMessage - method entered");
        String user="579042";
        String parola="ca00ddee9a532243928f16c22b49002b";
        String telefon=phoneNumber;
        String text=message;


        StringBuilder command = new StringBuilder("curl -X POST https://www.clickphone.ro/api/sms");

        StringBuilder param=new StringBuilder();
        param.append(" --data ").append("user=").append(user);
        param.append(" --data ").append("parola=").append(parola);
        param.append(" --data ").append("telefon=").append(telefon);
        param.append(" --data ").append("text=").append(text);
        command.append(param);
        System.out.println(command);


        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command.toString());
            process.waitFor();
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            StringBuilder response=new StringBuilder();
            try {
                while ((line = input.readLine()) != null)
                    response.append(line).append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.toString().contains("<result>success</result>")){
                return "ok";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }



        log.trace("sendMessage API- something is wrong");
        return "something went wrong";
    }
}
