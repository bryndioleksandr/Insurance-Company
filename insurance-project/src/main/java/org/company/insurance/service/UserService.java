package org.company.insurance.service;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.company.insurance.dto.UserCreationDto;
import org.company.insurance.dto.UserDto;
import org.company.insurance.entity.User;
import org.company.insurance.mapper.UserMapper;
import org.company.insurance.repository.UserRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }

    public UserDto createUser(UserCreationDto userDto) {
        User user = userMapper.toEntity(userDto);

        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
