package fun.diasonti.autochessweb.config.security;

import com.google.common.collect.ImmutableMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new DelegatingPasswordEncoder("bcrypt", ImmutableMap.of(
                "bcrypt", new BCryptPasswordEncoder(),
                "noop", NoOpPasswordEncoder.getInstance()
        ));
    }
}
