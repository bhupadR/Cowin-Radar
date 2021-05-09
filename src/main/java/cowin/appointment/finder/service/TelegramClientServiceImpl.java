package cowin.appointment.finder.service;

import cowin.appointment.finder.entity.User;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TelegramClientServiceImpl implements TelegramClientService{

    @Value("${token}")
    private String botId;

    @Override
    public void sendTelegramText( String text, List<User> users) {
        for (User u :
                users) {
            if (null != u.getEmail()) {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                HttpUrl.Builder builder = HttpUrl.parse("https://api.telegram.org/bot" + botId + "/sendMessage").newBuilder();
                builder.addQueryParameter("chat_id", u.getPhoneNumber());
                builder.addQueryParameter("text", text);
                Request request = new Request.Builder()
                        .url(builder.build())
                        .method("GET", null)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                        .addHeader("Host", "api.telegram.org")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
