package cowin.appointment.finder.service;

import cowin.appointment.finder.response.CenterResponse;
import cowin.appointment.finder.response.DistrictResponse;
import cowin.appointment.finder.response.SessionResponse;
import cowin.appointment.finder.response.StateResponse;

import java.io.IOException;


public interface CowinClientService {

    public StateResponse getAllStates() throws IOException ;

    public DistrictResponse getAllDistricts(int id) throws IOException ;

    public SessionResponse findByPin(String pincode, String date) throws IOException ;

    public SessionResponse findByDistrict(int districtId,String date) throws IOException ;

    public CenterResponse calendarByPin(String pincode, String date) throws IOException ;

    public CenterResponse calendarByDistrict(String districtId, String date) throws IOException ;

    void setAuthToken(String authToken);
}
