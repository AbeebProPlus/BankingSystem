package com.system.moneybank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
            title = "Money Bank: A Banking Management System",
            description = "Backend REST APIS for a banking mangement system",
            version = "v1.0",
            contact = @Contact(
                    name = "Abeeb Ahmad",
                    email = "abeebahmad24@gmail.com",
                    url = "pending"
            ),
                license = @License(
                        name = "My property"
                )
        )
)
public class MoneyBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyBankApplication.class, args);
    }

}
