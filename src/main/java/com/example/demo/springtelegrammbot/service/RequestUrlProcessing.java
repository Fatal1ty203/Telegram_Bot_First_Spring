package com.example.demo.springtelegrammbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RequestUrlProcessing {
    //    public static StringBuffer test(String orderNumber){
//        StringBuffer response = new StringBuffer();
//        if (orderNumber != null && isNumeric(orderNumber)){
//            String url =  "https://catery.ru/api/internal/v1/orders/" + orderNumber + "?secret=4a3c12c3a6a219849bb0c85054ccc7867953387afb411356bda87452a50c39344";
//            URL urlObj;
//            HttpURLConnection connection;
//            BufferedReader reader = null;
//            String input;
//
//            try {
//                urlObj = new URL(url);
//                connection = (HttpURLConnection) urlObj.openConnection();
//                connection.
//                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//                while (((input = reader.readLine()) != null)) {
//                    response.append(input);
//                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return response;
//    }
    public static StringBuffer test(String orderNumber) {
        if (orderNumber != null && isNumeric(orderNumber)) {
            String url = "https://catery.ru/api/internal/v1/orders/" + orderNumber + "?secret=4a3c12c3a6a219849bb0c85054ccc7867953387afb411356bda87452a50c39344";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET,null,String.class);
            String json = exchange.getBody();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode rootNode = null;
            try {
                rootNode = mapper.readValue(json,ObjectNode.class);
                JsonNode dateNode = rootNode.get("data");
                Double com = (double) (dateNode.get("attributes").get("commission_cents").asInt() / 100);
                System.out.println(dateNode.get("id") + " сумма комиссии: " + com);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        test("299499");
    }

        public static boolean isNumeric (String string){
            int intValue;

            if (string == null || string.equals("")) {
                System.out.println("String cannot be parsed, it is null or empty.");
                return false;
            }

            try {
                intValue = Integer.parseInt(string);
                return true;
            } catch (NumberFormatException e) {
                System.out.println("Input String cannot be parsed to Integer.");
            }
            return false;
        }
    }
