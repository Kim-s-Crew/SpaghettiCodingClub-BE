package wercsmik.spaghetticodingclub.global.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AwsProperties {

    @Value("${db_url}")
    private String databaseUrl;

    @Value("${db_user}")
    private String databaseUser;

    @Value("${db_password}")
    private String databasePassword;
}
