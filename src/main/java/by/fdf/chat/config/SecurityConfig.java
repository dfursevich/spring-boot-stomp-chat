package by.fdf.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Dzmitry Fursevich
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception{
        managerBuilder.inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER").and()
                .withUser("user1").password("{noop}user1").roles("USER").and()
                .withUser("user2").password("{noop}user2").roles("USER");
    }
}
