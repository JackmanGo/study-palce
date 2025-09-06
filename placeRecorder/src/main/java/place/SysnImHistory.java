package place;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"place"})
@MapperScan
public class  SysnImHistory {
    public static void main(String[] args){
        SpringApplication.run(SysnImHistory.class, args);
    }
}
