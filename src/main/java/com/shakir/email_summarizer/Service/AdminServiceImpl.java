package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Authority;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements  AdminService{

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users =  new ArrayList<>();
        List<UserResponse> userResponses = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        for(User user : users){
            userResponses.add(convertToUserResponse(user));
        }
        return userResponses;
    }

    private UserResponse convertToUserResponse(User user){
        return new UserResponse(
                user.getFirstName() + " " + user.getLastName(),
                user.getId(),
                user.getEmail(),
                user.getAuthorities().stream().map(auth -> (Authority) auth).toList()
        );
    }
    @Transactional
    @Override
    public UserResponse promoteToAdmin(long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User does not Exist or user is an admin");
        }
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_USER"));
        authorities.add(new Authority("ROLE_ADMIN"));
        user.get().setAuthorities(authorities);
        User savedUser = userRepository.save(user.get());
        return convertToUserResponse(savedUser);
    }
    @Transactional
    @Override
    public void deleteNonAdminUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty() || user.get().getAuthorities().stream().anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()))){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist or user is an admin");
        }

        userRepository.delete(user.get());

    }
}
