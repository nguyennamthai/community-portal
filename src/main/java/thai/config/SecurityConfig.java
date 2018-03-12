package thai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import thai.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/", "/about", "/css/*", "/js/*").permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/").permitAll().and().logout().logoutSuccessUrl("/login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("user").password("{noop}pass").roles("USER");
        auth.userDetailsService(userService);
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
