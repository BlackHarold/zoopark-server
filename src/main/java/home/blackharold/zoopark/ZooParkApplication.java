package home.blackharold.zoopark;

import home.blackharold.zoopark.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZooParkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZooParkApplication.class, args);

        User user = new User(1l, "username", "email", "password");
    }


}
