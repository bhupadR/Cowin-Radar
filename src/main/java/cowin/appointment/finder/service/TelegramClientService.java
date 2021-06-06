package cowin.appointment.finder.service;


import java.util.List;

public interface TelegramClientService {

    public void sendMessageToAdmin(String text,String number);

    void sendTelegramMessageToNumbers(String s, List<String> listOfNumbers);
}
