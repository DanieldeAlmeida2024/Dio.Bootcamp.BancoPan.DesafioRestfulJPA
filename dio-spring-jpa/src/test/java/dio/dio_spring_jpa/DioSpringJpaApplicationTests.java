package dio.dio_spring_jpa;

import me.dio.academia.digital.AcademiaDigitalApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = AcademiaDigitalApplication.class)
class DioSpringJpaApplicationTests {
    @Test
    void contextLoads() {
    }
}
