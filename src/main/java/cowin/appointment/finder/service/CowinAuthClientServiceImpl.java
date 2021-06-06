package cowin.appointment.finder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.GenerateOtpResponse;
import cowin.appointment.finder.response.Session;
import cowin.appointment.finder.response.ValidateOtpResponse;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

@Service
public class CowinAuthClientServiceImpl implements CowinAuthClientService{

    @Value("${cowin.api.generateOtp:v2/auth/generateMobileOTP}")
    private String generateOtp;

    @Value("${cowin.api.validateOtp:v2/auth/validateMobileOtp}")
    private String validateOtp;

    @Value("${cowin.api:https://cdn-api.co-vin.in/api/}")
    private String url;

    @Value("${generate.otp.secret:U2FsdGVkX18jgVMLXdH8YxfSapanIAlcPscovzSfwLdfmKtkxw0EBaa9rn7uHga2m2yudKc65tatfkC0/nwXRQ==}")
    private String secret;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;

    @Autowired
    CowinClientService cowinClientService;

    @Value("${cowin.api.calendar.by.district:v2/appointment/sessions/calendarByDistrict}")
    String calendarByDistrict;

    Scanner sc = new Scanner(System.in);

    @Value("${appointment.url:v2/appointment/schedule}")
    String bookAppointment;

    @Override
    public GenerateOtpResponse generateOtp(String phoneNumber) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String json = "{\"mobile\":\""+phoneNumber+"\",\"secret\":" +
                "\""+secret+"\"}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url.concat(generateOtp))
                .post(body);
        requestBuilder.addHeader("Content-Length",String.valueOf(json.length()));
        addHeader(requestBuilder);
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody responseBodyody = response.body();
            assert body != null;
            return objectMapper.readValue(responseBodyody.string(), GenerateOtpResponse.class);
        }
        return null;
    }

    @Override
    public ValidateOtpResponse validateOtp(String otp,GenerateOtpResponse generateOtpResponse) throws IOException {
        String sha256Hex = DigestUtils.sha256Hex(otp);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String json = "{\"otp\":\""+sha256Hex+"\"," +
                "\"txnId\":\""+generateOtpResponse.getTxnId()+"\"}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url.concat(validateOtp))
                .post(body);
        requestBuilder.addHeader("Content-Length",String.valueOf(json.length()));
        addHeader(requestBuilder);
        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if(response.code()==200) {
            ResponseBody responseBodyody = response.body();
            assert body != null;
            return objectMapper.readValue(responseBodyody.string(), ValidateOtpResponse.class);
        }
        return null;
    }

    @Override
    public String authenticateAndToken(String phoneNumber) {
        try {
            GenerateOtpResponse generateOtpResponse = generateOtp(phoneNumber);
            System.out.println(" Enter otp received at time: "+Calendar.getInstance().getTime());
            String otp = String.valueOf(sc.nextInt());
            sc.nextLine();
            ValidateOtpResponse validateOtpResponse = validateOtp(otp, generateOtpResponse);
            if(validateOtpResponse != null) {
                this.authToken = validateOtpResponse.getToken();
                return validateOtpResponse.getToken();
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }finally {
            //sc.close();
        }
        return null;
    }

    @Override
    public String getToken() {
        return this.authToken;
    }

    private void addHeader(Request.Builder builder){
        builder.addHeader("authority","cdn-api.co-vin.in")
        .addHeader("user-agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36``")
                .addHeader("origin","https://apisetu.gov.in").
                addHeader("referer","https://apisetu.gov.in/public/api/cowin")
                .addHeader("accept","application/json")
                .addHeader("content-type","application/json")
                .addHeader("Host","cdn-api.co-vin.in")
                .addHeader("Connection","keep-alive")
        .addHeader("accept-language","en-GB,en-US;q=0.9,en;q=0.8");
        if(null != authToken){
            builder.addHeader("authorization","Bearer "+authToken);
        }
    }

    @Override
    public  Response  findCenterBySessionDistrict(String districtId) throws IOException {
        Calendar instance = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+calendarByDistrict).newBuilder();
        builder.addQueryParameter("district_id",districtId);
        builder.addQueryParameter("date",sdf.format(instance.getTime()));
        Request.Builder requestBuilder = new Request.Builder()
                .url(builder.build())
                .method("GET", null);
        addHeader(requestBuilder);
        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }

    @Override
    public void setToken(String token) {
        this.authToken = token;
    }

    @Override
    public Response findCenterBySessionDistrictByDate(String date, String districtId) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        HttpUrl.Builder builder = HttpUrl.parse(url+calendarByDistrict).newBuilder();
        builder.addQueryParameter("district_id",districtId);
        builder.addQueryParameter("date",date);
        Request.Builder requestBuilder = new Request.Builder()
                .url(builder.build())
                .method("GET", null);
        addHeader(requestBuilder);
        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }

    @Override
    public Response bookSlot(Center center, Integer doseNumber, List<String> beneficiaries) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Response response = null;
        for(Session session : center.getSessions()) {
            for(String slot : session.getSlots()) {
                HttpUrl.Builder builder = HttpUrl.parse(url + bookAppointment).newBuilder();
                String json = "{\"center_id\":"+center.getCenterId()+",\"session_id\":" +
                        "\"+"+session.getSessionId()+"\"," +
                        "\"beneficiaries\":[";
                for(int i=0;i<beneficiaries.size();i++){
                    json += "\""+beneficiaries.get(i)+"\"";
                    if(i<beneficiaries.size()-1){
                        json+=",";
                    }
                }
                json+="],\"slot\":\""+slot+"\",\"dose\":"+doseNumber+"}";
                System.out.println(" book slot request body "+json);
                RequestBody body = RequestBody.create(
                        MediaType.parse("application/json"), json);
                Request.Builder requestBuilder = new Request.Builder()
                        .url(builder.build())
                        .post(body);
                addHeader(requestBuilder);
                Request request = requestBuilder.build();
                response = client.newCall(request).execute();
                if(response.code()==200){
                    return response;
                }
                response.close();
            }
        }
        return response;
    }
}
