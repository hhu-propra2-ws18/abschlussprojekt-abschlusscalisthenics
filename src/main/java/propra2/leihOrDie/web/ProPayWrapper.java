package propra2.leihOrDie.web;


import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import propra2.leihOrDie.model.Account;
import propra2.leihOrDie.model.Reservation;


public class ProPayWrapper {
    public static double getBalanceOfUser(String username) {
        try {
            return getAccountOfUser(username).getAmount();
        } catch (Exception e) {
            return - 1;
        }
    }

    public static void createUser(String username) {
        try {
            getAccountOfUser(username);
        } catch (Exception e) {
            return;
        }
    }

    public static Account raiseBalanceOfUser(String username, double amount) {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/account/" + username;
        MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<>();
        postParams.add("amount", amount);
        ResponseEntity<Account> result = rt.postForEntity(url, postParams, Account.class);

        return result.getBody();
    }

    public static String transferMoney(String senderUsername, String recipientUsername,
                                        double amount) throws Exception {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/account/" + senderUsername + "/transfer/" + recipientUsername;
        MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<>();
        postParams.add("amount", amount);
        ResponseEntity<String> result = rt.postForEntity(url, postParams, String.class);

        return result.getBody();
    }

    public static Account punishAccount(String username, Long reservationId) throws Exception {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/reservation/punish/" + username;

        return request(reservationId, url, rt).getBody();
    }

    public static Account freeReservationOfUser(String username, Long reservationId) throws Exception {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/reservation/release/" + username;
        MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<>();
        postParams.add("reservationId", reservationId);
        ResponseEntity<Account> result = rt.postForEntity(url, postParams, Account.class);

        return request(reservationId, url, rt).getBody();
    }

    public static Reservation reserve(String senderUsername, String recipientUsername,
                                      double amount) throws Exception {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/reservation/reserve/" + senderUsername + "/" + recipientUsername;
        MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<>();
        postParams.add("amount", amount);
        ResponseEntity<Reservation> result = rt.postForEntity(url, postParams, Reservation.class);

        return result.getBody();
    }

    private static Account getAccountOfUser(String username) throws Exception {
        RestTemplate rt = new RestTemplate();
        String url = "http://localhost:8888/account/" + username;
        ResponseEntity<Account> result = rt.getForEntity(url, Account.class);
        return result.getBody();
    }

    private static ResponseEntity<Account> request(Long reservationId, String url, RestTemplate restTemplate) {
        MultiValueMap<String, Object> postParams = new LinkedMultiValueMap<>();
        postParams.add("reservationId", reservationId);
        return restTemplate.postForEntity(url, postParams, Account.class);
    }

}
