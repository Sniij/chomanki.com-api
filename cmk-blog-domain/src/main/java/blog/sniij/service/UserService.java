package blog.sniij.service;

import blog.sniij.domain.User;
import blog.sniij.domain.UserRepo;
import blog.sniij.exception.BusinessLogicException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(User user){
        return userRepo.save(user);
    }

    public User updateUser(User user){

        User findUser = findVerifiedUserById(user.getUserId());
        User updateUser = User.builder()
                .userId(findUser.getUserId())
                .provider(user.getProvider())
                .nickname(user.getNickname())
                .imgUrl(user.getImgUrl())
                .email(user.getEmail())
                .build();

        return userRepo.save(updateUser);
    }


    public User findUserById(String userId){
        return findVerifiedUserById(userId);
    }
    public void verifyUserById(String userId){
        if(!userRepo.existsById(userId)){
            throw new BusinessLogicException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    public User findUserByEmail(String email){
        return findVerifiedUserByEmail(email);
    }

    public Optional<User> findOpUserByEmail(String email){
        return userRepo.findByEmail(email);
    }


    private User findVerifiedUserById(String userId) {
        return userRepo.findById(userId).orElseThrow( ()->
                new BusinessLogicException(HttpStatus.NOT_FOUND, "User not found")
        );
    }

    private User findVerifiedUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow( ()->
                new BusinessLogicException(HttpStatus.NOT_FOUND, "User not found")
        );
    }

}
