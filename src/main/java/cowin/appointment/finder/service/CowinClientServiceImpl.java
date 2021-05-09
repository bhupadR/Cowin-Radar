package cowin.appointment.finder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cowin.appointment.finder.response.CenterResponse;
import cowin.appointment.finder.response.DistrictResponse;
import cowin.appointment.finder.response.SessionResponse;
import cowin.appointment.finder.response.StateResponse;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CowinClientServiceImpl implements CowinClientService{

    @Autowired
    ObjectMapper objectMapper;

    @Value("${cowin.api:https://cdn-api.co-vin.in/api/}")
    String url;

    @Value("${cowin.api.all.states:v2/admin/location/states}")
    String allStates;

    @Value("${cowin.api.find.by.pin:v2/appointment/sessions/public/findByPin}")
    String findByPin;

    @Value("${cowin.api.all.district:v2/admin/location/districts/}")
    String allDistricts;

    @Value("${cowin.api.find.by.district:v2/appointment/sessions/public/findByDistrict}")
    String findByDistrictID;

    @Value("${cowin.api.calendar.by.pin:v2/appointment/sessions/public/calendarByPin}")
    String calendarByPin;

    @Value("${cowin.api.calendar.by.district:v2/appointment/sessions/public/calendarByDistrict}")
    String calendarByDistrict;

    public StateResponse getAllStates() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url+allStates)
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            return objectMapper.readValue(body.string(), StateResponse.class);
        }else return null;
    }

    public DistrictResponse getAllDistricts(int id) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url+allDistricts+id)
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            return objectMapper.readValue(body.string(), DistrictResponse.class);
        }else {
            return null;
        }
    }

    public SessionResponse findByPin(String pincode, String date) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+findByPin).newBuilder();
        builder.addQueryParameter("pincode",pincode);
        builder.addQueryParameter("date",date);
        Request request = new Request.Builder()
                .url(builder.build())
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            return objectMapper.readValue(body.string(), SessionResponse.class);
        }else {
            return null;
        }
    }

    public SessionResponse findByDistrict(int districtId,String date) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+findByDistrictID).newBuilder();
        builder.addQueryParameter("district_id", String.valueOf(districtId));
        builder.addQueryParameter("date",date);
        Request request = new Request.Builder()
                .url(builder.build())
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            return objectMapper.readValue(body.string(), SessionResponse.class);
        }else {
            return null;
        }
    }

    public CenterResponse calendarByPin(String pincode, String date) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+calendarByPin).newBuilder();
        builder.addQueryParameter("pincode",pincode);
        builder.addQueryParameter("date",date);
        Request request = new Request.Builder()
                .url(builder.build())
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();


        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            return objectMapper.readValue(body.string(), CenterResponse.class);
        }else {
            return null;
        }
    }

    public CenterResponse calendarByDistrict(String districtId, String date) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+calendarByDistrict).newBuilder();
        builder.addQueryParameter("district_id",districtId);
        builder.addQueryParameter("date",date);
        Request request = new Request.Builder()
                .url(builder.build())
                .method("GET", null)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody body = response.body();
            assert body != null;
            String responseBody = body.string();
            return objectMapper.readValue(responseBody, CenterResponse.class);
        }else {
            return null;
        }
    }
}
