package cvut.fit.cz.blog.service;
import cvut.fit.cz.blog.controller.dto.UserDto;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements CrudService<UserDto, Long>{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto save(UserDto userDto) {

        User entity = new User(userDto.getUsername(), userDto.getDisplay_name(), userDto.getPassword());
        User newUser = userRepository.save(entity);
        return new UserDto(newUser.getId_user(), newUser.getUsername(), newUser.getDisplayname(), newUser.getPassword());

    }

    @Override
    public Iterable<UserDto> getAll() {
        Iterable<User> users = userRepository.findAll();
        List<UserDto> returnUsers = new ArrayList<>();

        for(User user : users){
            returnUsers.add(new UserDto(user.getId_user(), user.getUsername(), user.getDisplayname(), user.getPassword()));
        }
        return returnUsers;
    }

    @Override
    public UserDto findById(Long aLong) {
        Optional<User> foundUser = userRepository.findById(aLong);
        return new UserDto(foundUser.get().getId_user(), foundUser.get().getUsername(), foundUser.get().getDisplayname(), foundUser.get().getPassword());
    }

    @Override
    public boolean deleteById(Long aLong) {
        userRepository.deleteById(aLong);
        Optional<User> isDeleted = userRepository.findById(aLong);
        return isDeleted.isEmpty();
    }

    @Override
    public UserDto update(Long aLong, UserDto userDto) {
        User user = userRepository.findById(aLong).orElseThrow(RuntimeException::new);
        user.setUsername(user.getUsername());
        user.setDisplayname(userDto.getDisplay_name());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
        return new UserDto(user.getId_user(), user.getUsername(), user.getDisplayname(), user.getPassword());
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return new UserDto(user.get().getId_user(), user.get().getUsername(), user.get().getDisplayname(), user.get().getPassword());
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


}
