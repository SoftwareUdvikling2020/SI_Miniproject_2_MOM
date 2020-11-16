package dk.dd.rabbit;
/*
 * Message Producer
 *
 * Produces a simple message, which will be delivered to a specific consumer
 * 1) Creates a queue
 * 2) Sends the message to it
 */

import com.google.gson.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.net.*;
import java.util.Map;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.json.JSONObject;
import org.json.XML;

@SpringBootApplication
public class RabbitApplication {
    private final static String QUEUE_NAME = "mailqueue";
    static Gson gson = new Gson();
    static RecipientList rl;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(RabbitApplication.class, args);

        while (true) {
            sendEmails();
            Thread.sleep(30*1000);
        }

    }

    private static void sendEmails() throws Exception {
        //hent text til mail body fra fil
        String bodyText = "";
        try {
            bodyText = new String(Files.readAllBytes(Paths.get("mailtosend.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //hent liste af modtagere som json og lav til RecipientList
        try {
            rl = gson.fromJson(new String(Files.readAllBytes(Paths.get("recipients.json"))), RecipientList.class);
        } catch (Exception e) {
            System.out.println("something weird " + e.getLocalizedMessage());
        }


        for (String name : rl.getNames()) {

            String genderLetters = getGenderLetters(name);
            String finalMessage = bodyText.replace("XX", genderLetters).replace("NN", name);

            String[] recipients = {name.toLowerCase() + "@example.com"};

            Mail mail = new Mail(recipients, finalMessage, "testssubject");

            String toPublish = gson.toJson(mail);

            createQueue(toPublish);
            System.out.println(" [x] Sent json: " + toPublish);

        }
    }

    public static String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();
        return result.toString();
    }


    public static String getGenderLetters(String name) throws Exception {
        String genderLetters = "";
        String response = getHTML("http://www.thomas-bayer.com/restnames/name.groovy?name=" + name);
        JSONObject json = XML.toJSONObject(response);
        boolean b = (boolean) json.getJSONObject("restnames").getJSONObject("nameinfo").get("male");
        return b ? "Mr." : "Ms.";
    }

    public static void createQueue(String message) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        }
    }
}