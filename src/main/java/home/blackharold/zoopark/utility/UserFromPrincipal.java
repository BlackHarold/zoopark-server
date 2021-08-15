package home.blackharold.zoopark.utility;

import home.blackharold.zoopark.entity.User;
import home.blackharold.zoopark.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.security.Principal;

public interface UserFromPrincipal {

    default User getUserByPrincipal(UserRepository userRepository, Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found with username "));

    }
}
