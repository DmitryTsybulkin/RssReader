package rss.combinator.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityEncoder securityEncoder;

    @Autowired
    public SecurityConfig(SecurityEncoder securityEncoder) {
        this.securityEncoder = securityEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(securityEncoder.passwordEncoder().encode("user")).roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll();
        http.csrf().disable();
        http.headers().frameOptions().disable();
//                .authorizeRequests()
//                .antMatchers("/login*").anonymous()
//                .anyRequest().authenticated();
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .defaultSuccessUrl("/index")
//                .failureUrl("/login?error=true")
//                .and()
//                .logout().logoutSuccessUrl("/login");
    }
}
