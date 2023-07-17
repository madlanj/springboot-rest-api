package springboot.restfull.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import springboot.restfull.entity.User;
import springboot.restfull.model.LoginUserRequest;
import springboot.restfull.model.TokenResponse;
import springboot.restfull.repository.UserRepository;
import springboot.restfull.security.BCrypt;

import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public TokenResponse login(LoginUserRequest request){
        validationService.validate(request);

        User user = userRepository.findById(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong"));

        if(BCrypt.checkpw(request.getPassword(), user.getPassword())){
            // sukses
            user.setToken(UUID.randomUUID().toString());
            user.setTokenExpiredAt(next30Days());
            userRepository.save(user);

            return TokenResponse.builder()
                    .token(user.getToken())
                    .expiredAt(user.getTokenExpiredAt())
                    .build();
        }
        else{
            // gagal
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "username or password wrong");
        }

    }

    @Transactional
    public void logout(User user){
        user.setToken(null);
        user.setTokenExpiredAt(null);
        userRepository.save(user);

    }

    private Long next30Days(){
        return System.currentTimeMillis() + (1000 * 16 * 24 * 30);
    }

}
