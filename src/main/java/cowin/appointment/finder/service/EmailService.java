package cowin.appointment.finder.service;

public interface EmailService {

    public void sendSimpleMessage(String [] to, String message,String subject);
}
