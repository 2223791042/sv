package guet.sgc.sv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@MapperScan("guet.sgc.sv.dao")
@ImportResource(locations={"classpath:kaptcha.xml"})
public class SvApplication {

    public static void main(String[] args) {
        SpringApplication.run(SvApplication.class, args);
    }

}
