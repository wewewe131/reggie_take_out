import com.reggie.ReggieSpringApplication;
import com.reggie.Utils.SmsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = ReggieSpringApplication.class)
public class ApplicationTest {
    @Test
    void contextLoads() {
        new SmsUtils(). singleSend("18229833471","10013","3");
    }
}
