package old12t_it.cinematrix.controller;

import lombok.AllArgsConstructor;
import old12t_it.cinematrix.dtos.request.BanUserDto;
import old12t_it.cinematrix.dtos.request.DeviceTokenDto;
import old12t_it.cinematrix.dtos.response.UserRespDto;
import old12t_it.cinematrix.dtos.response.UserWithBanStatusDto;
import old12t_it.cinematrix.entity.User;
import old12t_it.cinematrix.repository.UserRepository;
import old12t_it.cinematrix.service.UserService;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserRepository userRepo;
    private UserService userService;

    @GetMapping("/detail")
    public EntityModel<User> getUserById(@RequestParam Long id) {
        Optional<User> getUser = userRepo.findUserById(id);
        if (getUser.isPresent())
            return EntityModel.of(getUser.get(),
                    linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                    linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
            );
        else {
            throw new RecordNotFoundException("User not found for id:" + id);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getUser(@RequestParam String username, @RequestParam String email) {
        List<UserWithBanStatusDto> userDtos = userService.getUsersByUsernameOrEmail(username, email);
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PostMapping("/ban")
    public ResponseEntity<Object> banUser(@RequestBody BanUserDto dto) {
        if (!userService.banUser(dto.getUserId()))
            return new ResponseEntity<>(String.format("Failed to ban user %d", dto.getUserId()), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(String.format("Banned user %d", dto.getUserId()), HttpStatus.ACCEPTED);
    }

    @PostMapping("/unban")
    public ResponseEntity<Object> unbanUser(@RequestBody BanUserDto dto) {
        if (!userService.unbanUser(dto.getUserId()))
            return new ResponseEntity<>(String.format("Failed to ban user %d", dto.getUserId()), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(String.format("Banned user %d", dto.getUserId()), HttpStatus.ACCEPTED);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<UserRespDto>> getAllUsers() {
        List<EntityModel<UserRespDto>> users = userRepo.findAll().stream()
                .map(user -> {
                    ModelMapper mapper = new ModelMapper();
                    UserRespDto data = mapper.map(user, UserRespDto.class);
                    return EntityModel.of(data,
                            linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
                })
                .collect(Collectors.toList());
        return CollectionModel.of(users, linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"));
    }

    @PostMapping("/register/device-token")
    public ResponseEntity<?> registerDeviceToken(@RequestBody DeviceTokenDto deviceDto) {
        Optional<User> getUser = userRepo.findUserById(Long.parseLong(deviceDto.getUserId()));
        if (getUser.isEmpty())
            throw new RecordNotFoundException("User not found for id:" + deviceDto.getUserId());
        User user =getUser.get();
        userService.addDeviceTokens(deviceDto.getDeviceToken(), user);

        return new ResponseEntity<>("register success", HttpStatus.OK);
    }
}
