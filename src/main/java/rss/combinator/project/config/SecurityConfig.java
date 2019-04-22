package rss.combinator.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import rss.combinator.project.services.UserService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityEncoder securityEncoder;
    private final UserService userService;

    @Autowired
    public SecurityConfig(SecurityEncoder securityEncoder, UserService userService) {
        this.securityEncoder = securityEncoder;
        this.userService = userService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(securityEncoder.passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/resources/**", "/templates/**", "/static/**", "/css/**", "/js/**",
                            "/images/**", "/webjars/**")
                .permitAll()
                    .antMatchers("/**")
                .authenticated()
                .and()
                .formLogin()
                .successForwardUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

}
