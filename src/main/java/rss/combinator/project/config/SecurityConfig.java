package rss.combinator.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import rss.combinator.project.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityEncoder securityEncoder;
    private final UserService userService;

    @Autowired
    public SecurityConfig(SecurityEncoder securityEncoder, UserService userService) {
        this.securityEncoder = securityEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService)
                .passwordEncoder(securityEncoder.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll();
        http.csrf().disable();

//        http.antMatcher("/**").authorizeRequests()
//                .antMatchers("/js/**", "/css/**", "/views/**", "/img/**", "/fonts/**", "/components/**").permitAll()
//                .antMatchers("/", "/login**", "/error**", "/time**").permitAll().anyRequest()
//                .authenticated()

        http.headers().frameOptions().disable();
    }
}
