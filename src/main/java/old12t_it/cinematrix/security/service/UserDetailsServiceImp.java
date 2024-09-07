package old12t_it.cinematrix.security.service;

import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.entity.User;
import old12t_it.cinematrix.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User Not Found with email: " + email);
        return UserDetailImp.build(user);
    }
}
