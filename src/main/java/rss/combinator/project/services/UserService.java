package rss.combinator.project.services;

import rss.combinator.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import rss.combinator.project.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import rss.combinator.project.exceptions.EntryDuplicateException;
import rss.combinator.project.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new EntryDuplicateException("User with username: " + user.getUsername() + " already exists");
        }
        return userRepository.save(new User(user.getUsername(), user.getPassword()));
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User by id " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllActive(Pageable pageable) {
        return userRepository.findAllByActiveIsTrue(pageable);
    }

    @Transactional
    public User update(User user) {
        User targetUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User by id " + user.getId() + " not found"));
        targetUser.setUsername(user.getUsername());
        targetUser.setPassword(user.getPassword());
        // TODO: 29.10.2018 set tags
        return targetUser;
    }

    @Transactional
    public void deactivate(Long id) {
        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User by id " + id + " not found"));
        targetUser.setActive(false);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username: " + username + " not found"));
        return new UserPrincipal(user.getUsername(), user.getPassword(), user.getActive());
    }

    class UserPrincipal implements UserDetails {

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
