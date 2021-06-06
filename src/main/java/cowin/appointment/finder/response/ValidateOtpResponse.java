package cowin.appointment.finder.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateOtpResponse {

    String token;

    String isNewAccount;
}
