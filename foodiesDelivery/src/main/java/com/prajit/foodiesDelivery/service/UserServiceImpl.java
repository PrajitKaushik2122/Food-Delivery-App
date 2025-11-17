package com.prajit.foodiesDelivery.service;


import com.prajit.foodiesDelivery.entity.UserEntity;
import com.prajit.foodiesDelivery.io.UserRequest;
import com.prajit.foodiesDelivery.io.UserResponse;
import com.prajit.foodiesDelivery.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity user = convertToEntity(request);
        user = userRepository.save(user);
        return convertToResponse(user);
    }

    private UserEntity convertToEntity(UserRequest request){
        return UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();
    }

    private UserResponse convertToResponse(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
