package ExampleCode.Date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * SimpleDateFormatExample
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2024. 02. 25.
 */
public class SimpleDateFormatExample {
    public static void main(String[] args) {
        LocalDate parseLocalDate = LocalDate.parse("2024-02-25");
        LocalTime parseLocalTime = LocalTime.parse("23:59:59");
        LocalDateTime parseLocalDateTime = LocalDateTime.parse("2024-02-25T19:54:54");

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parseLocalDateTimeByPattern = LocalDateTime.parse("2022-02-25 20:03:21", pattern);

        System.out.println("parseLocalDate: " + parseLocalDate);
        System.out.println("parseLocalTime: " + parseLocalTime);
        System.out.println("parseLocalDateTime: " + parseLocalDateTime);
        System.out.println("parseLocalDateTimeByPattern: " + parseLocalDateTimeByPattern);
    }
}
