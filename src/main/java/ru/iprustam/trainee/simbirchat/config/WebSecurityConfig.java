package ru.iprustam.trainee.simbirchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.iprustam.trainee.simbirchat.auth.ChatAuthenticationProvider;
import ru.iprustam.trainee.simbirchat.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private ChatAuthenticationProvider chatAuthenticationProvider;

    @Autowired
    public WebSecurityConfig(UserService userService, ChatAuthenticationProvider chatAuthenticationProvider) {
        this.userService = userService;
        this.chatAuthenticationProvider = chatAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/chat")
                .permitAll()
                .and()
            .logout()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
            .authorizeRequests()
                .antMatchers("/chat").hasAnyRole("USER", "ADMIN", "MODERATOR")
                .antMatchers("/**", "/webjars").permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAnyRole("USER", "ADMIN", "MODERATOR")
                .anyRequest().authenticated();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());

        auth.authenticationProvider(chatAuthenticationProvider);
    }
}
