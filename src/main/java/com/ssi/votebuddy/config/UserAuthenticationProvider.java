package com.ssi.votebuddy.config;

import com.ssi.votebuddy.model.AuthedUser;
import com.ssi.votebuddy.model.Role;
import com.ssi.votebuddy.model.User;
import com.ssi.votebuddy.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        User user = userRepository.findByEmail(email);

        if (user != null) {
            boolean pwdMatches = passwordEncoder.matches(pwd, user.getPwd());
            List<GrantedAuthority> authorities = getGrantedAuthorities(user.getRole());

            if (pwdMatches) {
                return new UsernamePasswordAuthenticationToken(new AuthedUser(user.getId(), email), pwd, authorities);
            } else {
                throw new BadCredentialsException("Incorrect password");
            }
        } else {
            throw new UsernameNotFoundException("User with given email not found");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Role> roleSet) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roleSet) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
        }

        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
