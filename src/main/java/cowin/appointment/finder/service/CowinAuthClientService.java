package cowin.appointment.finder.service;

import cowin.appointment.finder.response.Center;
import cowin.appointment.finder.response.GenerateOtpResponse;
import cowin.appointment.finder.response.ValidateOtpResponse;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public interface CowinAuthClientService {

    GenerateOtpResponse generateOtp(String phoneNumber) throws IOException;

    ValidateOtpResponse validateOtp(String otp,GenerateOtpResponse generateOtpResponse) throws IOException;

    String authenticateAndToken(String phoneNumber);

    String getToken();

    Response findCenterBySessionDistrict(String districtId) throws IOException;

    void setToken(String token);

    Response findCenterBySessionDistrictByDate(String date, String districtId) throws IOException;

    public Response bookSlot(Center center, Integer doseNumber, List<String> beneficiary) throws IOException;
}

