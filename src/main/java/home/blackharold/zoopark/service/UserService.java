package home.blackharold.zoopark.service;

import home.blackharold.zoopark.dto.UserDTO;
import home.blackharold.zoopark.entity.User;
import home.blackharold.zoopark.entity.enums.Role;
import home.blackharold.zoopark.exceptions.UserExistException;
import home.blackharold.zoopark.payload.request.SignupRequest;
import home.blackharold.zoopark.repository.UserRepository;
import home.blackharold.zoopark.utility.UserFromPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService implements UserFromPrincipal {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest signupUserInfo) {
        User user = new User();
        user.setEmail(signupUserInfo.getEmail());
        user.setFirstname(signupUserInfo.getFirstname());
        user.setLastname(signupUserInfo.getLastname());
        user.setUsername(signupUserInfo.getUsername());
        user.setPassword(passwordEncoder.encode(signupUserInfo.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        try {
            LOG.info("Saving User {}", signupUserInfo.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            LOG.error("Error during registration. {}", e.getMessage());
            throw new UserExistException(
                    String.format(
                            "The user %s already exist. Please check credentials",
                            user.getUsername()));
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(userRepository, principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());

        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(userRepository, principal);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

