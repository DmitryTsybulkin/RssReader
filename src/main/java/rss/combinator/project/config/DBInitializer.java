package rss.combinator.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

@Component
public class DBInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final SecurityEncoder securityEncoder;
    private final UserRepository userRepository;

    @Autowired
    public DBInitializer(SecurityEncoder securityEncoder, UserRepository userRepository) {
        this.securityEncoder = securityEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            userRepository.save(User.builder().username("user").password(securityEncoder.passwordEncoder().encode("user")).role("USER").active(true).build());
        } catch (Throwable e) {
            e.getLocalizedMessage();
        }
    }
}
