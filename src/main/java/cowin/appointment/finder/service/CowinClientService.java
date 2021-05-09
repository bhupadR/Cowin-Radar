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


public interface CowinClientService {

    public StateResponse getAllStates() throws IOException ;

    public DistrictResponse getAllDistricts(int id) throws IOException ;

    public SessionResponse findByPin(String pincode, String date) throws IOException ;

    public SessionResponse findByDistrict(int districtId,String date) throws IOException ;

    public CenterResponse calendarByPin(String pincode, String date) throws IOException ;

    public CenterResponse calendarByDistrict(String districtId, String date) throws IOException ;
}
