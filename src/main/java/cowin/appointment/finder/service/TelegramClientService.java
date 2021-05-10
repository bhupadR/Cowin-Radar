package cowin.appointment.finder.service;

import cowin.appointment.finder.entity.User;

import java.io.IOException;
import java.util.List;

public interface TelegramClientService {

    public void sendTelegramText( String text, List<User> users);

    public void sendMessageToAdmin(String text,String number);
}
