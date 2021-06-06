package cowin.appointment.finder.utility;

import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class EncryptUtil {

    public static void main (String... args) throws IOException {

        CronExpression cronExpression = CronExpression.parse("1 * * * * *");
        LocalDateTime next = cronExpression.next(LocalDateTime.now());
        System.out.println(next.toLocalTime());
    }
}
