package org.unamedgroup.conference.service.impl;

import org.springframework.stereotype.Component;
import org.unamedgroup.conference.service.DevicesControlService;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * DevicesControlSimulateImpl
 *
 * @author liumengxiao
 * @date 2019/06/28
 */

@Component
public class DevicesSimulateImpl implements DevicesControlService {
    @Override
    public Boolean openDoor(Integer userID) {
        try {
            post("http://raspberrypi.local/web/door", "");
            return true;
        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }


    public Boolean openAirCondition() {
        try {
            post("http://raspberrypi.local/web/infrared", "");
            return true;
        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }
    }


    private static final String USER_AGENT = "Mozilla/5.0";

    public HttpURLConnection post(String url, String parameter) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        if (parameter != null) {
            wr.writeBytes(parameter);
        }
        wr.flush();
        wr.close();

        return connection;
    }

    public int doPost(String url, String parameter) throws Exception {
        HttpURLConnection connection = post(url, parameter);
        int responseCode = connection.getResponseCode();
        return responseCode;
    }

}
