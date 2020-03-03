package ru.iprustam.trainee.simbirchat.auth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.iprustam.trainee.simbirchat.entity.ChatUser;
import ru.iprustam.trainee.simbirchat.entity.ChatUserRole;
import ru.iprustam.trainee.simbirchat.service.UserService;
import ru.iprustam.trainee.simbirchat.util.role.ChatAuthorities;

import java.util.Arrays;
import java.util.List;

@Component
public class ChatAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public ChatAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        String username = token.getPrincipal().toString();
        String password = token.getCredentials().toString();

        ChatUser user = (ChatUser) userService.loadUserByUsername(username);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean passwordsMatch = encoder.matches(password, user.getPassword());
        if(!passwordsMatch)
            throw new UsernameNotFoundException("Passwords don't match");

        ChatUserRole role = user.getRole();
        // Add role
        StringBuilder rolesAndAuthorities = new StringBuilder(role.getRoleName()+",");
        // Add authorities
        ChatAuthorities[] allAuthorities = ChatAuthorities.values();
        Arrays.stream(allAuthorities).filter(a->role.hasAuthority(a)).forEach(a->rolesAndAuthorities.append(a+","));

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                rolesAndAuthorities.toString());

        return new UsernamePasswordAuthenticationToken(user, token.getCredentials(), authorities);
    }
}
