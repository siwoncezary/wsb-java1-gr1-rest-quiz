package pl.wsb.restquiz.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .realmName("Realm")
                .and()
                .csrf()
                .disable()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .authorizeRequests()
                .antMatchers("/api/quiz/**", "/api/quizzes/**").hasRole("USER")
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/register/**").hasRole("USER")
                .anyRequest().permitAll()
                .and();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("$2y$12$nDPSdp6ua4iHNXW9.OiV2umVT39JUN5ti.B58ybHJVl/OkvToxRSq").roles("USER")
                .and()
                .withUser("admin").password("$2y$12$nDPSdp6ua4iHNXW9.OiV2umVT39JUN5ti.B58ybHJVl/OkvToxRSq").roles("ADMIN");
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}
