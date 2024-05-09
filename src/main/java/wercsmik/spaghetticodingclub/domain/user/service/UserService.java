package wercsmik.spaghetticodingclub.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wercsmik.spaghetticodingclub.domain.user.dto.ProfileResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.exception.CustomException;
import wercsmik.spaghetticodingclub.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ProfileResponseDTO getProfile(Long userId) {

        User user = getUser(userId);

        return new ProfileResponseDTO(user);
    }

    public User getUser(Long userId) {

        return userRepository.findById(userId).orElseThrow(()
                -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }
}
