package rss.combinator.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rss.combinator.project.config.SecurityEncoder;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import rss.combinator.project.model.User;
import rss.combinator.project.repository.UserRepository;

import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SecurityEncoder securityEncoder;

    @Autowired
    public UserService(UserRepository userRepository, SecurityEncoder securityEncoder) {
        this.userRepository = userRepository;
        this.securityEncoder = securityEncoder;
    }

    @Transactional
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new EntryDuplicateException("User with username: " + user.getUsername() + " already exists");
        }
        return userRepository.save(new User(user.getUsername(),
                securityEncoder.passwordEncoder().encode(user.getPassword())));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User by id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Page<User> getAllActive(Pageable pageable) {
        return userRepository.findAllByActiveIsTrue(pageable);
    }

    @Transactional
    public void deactivate(Long id) {
        User targetUser = getById(id);
        targetUser.setActive(false);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
        return new UserPrincipal(user.getUsername(), user.getPassword(), user.getActive());
    }

    public static class UserPrincipal implements UserDetails {

        private String username;
        private String password;
        private Boolean active;

        public UserPrincipal(String username, String password, Boolean active) {
            this.username = username;
            this.password = password;
            this.active = active;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return active;
        }
    }
}
