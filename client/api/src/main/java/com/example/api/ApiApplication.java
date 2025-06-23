package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
// import com.example.api.dto.UserRegisterDto;
// import com.example.api.service.UserService;

@SpringBootApplication
@EnableScheduling
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    // 仮ユーザー登録用（必要なときだけ使う）
    
    //@Bean
    // public CommandLineRunner init(UserService userService) {
    //     return args -> {
    //         UserRegisterDto user = new UserRegisterDto();
    //         user.setName("仮ユーザー4");
    //         user.setEmail("2admin_test@example.com"); // ←重複しないように
    //         user.setPassword("Test1234!");     // 平文でOK（中でハッシュ化される）
    //         user.setAdmin(true);
    //         user.setRole("マネージャー");

    //         userService.registerUser(user);
    //         System.out.println("✅ 仮ユーザー4を登録しました！");
    //     };
    // }
    
}
