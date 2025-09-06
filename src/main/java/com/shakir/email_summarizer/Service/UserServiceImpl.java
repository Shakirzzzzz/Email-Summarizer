package com.shakir.email_summarizer.Service;

import com.shakir.email_summarizer.Entity.Authority;
import com.shakir.email_summarizer.Entity.User;
import com.shakir.email_summarizer.Repository.UserRepository;
import com.shakir.email_summarizer.Request.UpdatePasswordRequest;
import com.shakir.email_summarizer.Response.UserResponse;
import com.shakir.email_summarizer.Util.FindAuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FindAuthenticatedUser findAuthenticatedUser;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, FindAuthenticatedUser findAuthenticatedUser, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.findAuthenticatedUser = findAuthenticatedUser;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse getUserInfo() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        return new UserResponse(
                user.getFirstName() + " " + user.getLastName(),
                user.getId(),
                user.getEmail(),
                user.getAuthorities().stream().map(auth -> (Authority) auth).toList()
        );
    }

    @Override
    public void deleteUser() throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        if(isLastAdmin(user)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Admin cannot delete itself");
        }

        userRepository.delete(user);

    }

    private boolean isLastAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        if(isAdmin){
            long adminCount = userRepository.countAdminUsers();
            return adminCount <= 1;
        }
        return false;
    }

    @Override
    public void updatePassword(UpdatePasswordRequest updatePasswordRequest) throws AccessDeniedException {
        User user = findAuthenticatedUser.getAuthenticatedUser();
        if(!isOldPasswordCorrect(user.getPassword(),updatePasswordRequest.getOldPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Current Password is Incorrect");
        }

        if(!isNewPasswordConfirmed(updatePasswordRequest.getNewPassword(),updatePasswordRequest.getNewPassword2())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"New Passwords do not match");
        }

        if(!isNewPasswordDifferent(updatePasswordRequest.getOldPassword(),updatePasswordRequest.getNewPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"New Password is same as old password");
        }

        user.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

    }

    private boolean isOldPasswordCorrect(String currentPassword, String oldPassword){
        return passwordEncoder.matches(oldPassword,currentPassword);
    }
    private boolean isNewPasswordConfirmed(String newPassword,String newPasswordConfirmation){
        return newPassword.equals(newPasswordConfirmation);
    }
    private boolean isNewPasswordDifferent(String oldPassword, String newPassword){
        return !oldPassword.equals(newPassword);
    }
}
