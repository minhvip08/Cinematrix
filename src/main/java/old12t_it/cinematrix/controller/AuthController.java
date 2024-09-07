package old12t_it.cinematrix.controller;

import jakarta.validation.Valid;
import lombok.*;
import old12t_it.cinematrix.dtos.RequestPasswordResetDto;
import old12t_it.cinematrix.dtos.ResetPasswordDto;
import old12t_it.cinematrix.entity.*;
import old12t_it.cinematrix.repository.NotificationRepo.NotiMessageBoxRepositionry;
import old12t_it.cinematrix.service.*;
import old12t_it.cinematrix.service.exception.Exception.InvalidInviteCodeException;
import old12t_it.cinematrix.service.exception.Exception.LoginFailException;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import old12t_it.cinematrix.dtos.response.LoginSuccessResponse;
import old12t_it.cinematrix.dtos.request.GiveICDto;
import old12t_it.cinematrix.dtos.request.LoginDto;
import old12t_it.cinematrix.dtos.request.RegisterDto;
import old12t_it.cinematrix.repository.InviteCodeRepository;
import old12t_it.cinematrix.repository.UserRepository;
import old12t_it.cinematrix.security.service.UserDetailImp;
import old12t_it.cinematrix.security.jwt.JwtTokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@Setter
@Getter
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final InviteCodeRepository invCodeRepo;
    private final NotiMessageBoxRepositionry notiMsgRepo;

    private final SequenceGeneratorService seqGenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtService;
    private final AuthService authService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    ///
    Logger logger = org.slf4j.LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@RequestBody RegisterDto userDto) {
        // business logic validation
        // check invite code is valid or not
        InviteCode invCode = invCodeRepo.findByCode(userDto.getInviteCode());
        if (!userService.isInviteCodeValid(invCode)) {
            return new ResponseEntity<>("Invalid or used invite code", HttpStatus.ALREADY_REPORTED);
        }
        // check email is already exist or not
        if (userRepo.findUserByEmail(userDto.getEmail()) != null) {
            return new ResponseEntity<>("Email is already exist", HttpStatus.ALREADY_REPORTED);
        }

//        // password hashing
//        String hashString = passwordEncoder.encode(userDto.getPassword());
//        User newUser = new User(seqGenService.generateSequence(User.SEQUENCE_NAME), userDto.getFullName(),
//                userDto.getEmail(), hashString);
//        userRepo.save(newUser);
//        // set owner for invite code
//        userService.setOwnerForInviteCode(invCode, newUser);
//        //create notification message box for user
//        List<NotiMessage> initMsgList = new ArrayList<>();
//
//        NotiMessageBox notiMessageBox = new NotiMessageBox(seqGenService.generateSequence(NotiMessageBox.SEQUENCE_NAME), newUser, initMsgList);
//        notiMsgRepo.save(notiMessageBox);

        authService.saveUser(userDto.getFullName(), userDto.getEmail(), userDto.getPassword(), invCode);
        return new ResponseEntity<>("Add user " + userDto.getFullName() + " successfully", HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
//        Authentication authen;
//        try {
//            authen = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginDto.getEmail(),
//                            loginDto.getPassword()));
//        } catch (Exception e) {
//            // TODO: handle exception
//            throw new LoginFailException(e.getMessage());
//        }
//        SecurityContextHolder.getContext().setAuthentication(authen);
//        String jwt = jwtService.generateToken(authen);
//        UserDetailImp userDetails = (UserDetailImp) authen.getPrincipal();
        Authentication authen = authService.login(loginDto.getEmail(), loginDto.getPassword());
        String jwt = jwtService.generateToken(authen);
        UserDetailImp userDetails = (UserDetailImp) authen.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        System.out.println("Login successfully");
        return ResponseEntity.ok(new LoginSuccessResponse(jwt, userDetails.getId().toString(), userDetails.getEmail(), roles));
    }

    @PostMapping("/add_invitecode")
    public ResponseEntity<String> saveInviteCode(@RequestBody String code) {
        if (this.invCodeRepo.findByCode(code) != null) {
            return new ResponseEntity<>("This code is already exist", HttpStatus.BAD_REQUEST);
        }
        InviteCode newInvCode = new InviteCode(seqGenService.generateSequence(InviteCode.SEQUENCE_NAME), code);
        this.invCodeRepo.save(newInvCode);
        return new ResponseEntity<>("Create new invite code successfully", HttpStatus.CREATED);
    }

    // Testing: Give code to a specific user
    @PostMapping("/give_invitecodes")
    public ResponseEntity<String> giveInviteCod2User(@RequestBody GiveICDto giveICDto) {
        Optional<User> getUser = userRepo.findUserById(Long.parseLong(giveICDto.getUserId()));
        if (getUser.isEmpty()) throw new RecordNotFoundException("User not found for id:" + giveICDto.getUserId());
        User user = getUser.get();
        InviteCode invCode = invCodeRepo.findById(Long.parseLong(giveICDto.getInviteCodeId()));
        if (invCode == null)
            throw  new InvalidInviteCodeException("Invite Code not found for id:" + giveICDto.getInviteCodeId());
        if (userService.isInviteCodeIsGiven(invCode)) {
            return new ResponseEntity<String>("This code is already given", HttpStatus.BAD_REQUEST);
        }
        // check if user already have this code
//        boolean userAlHaveCode = false;
//        for (InviteCode ic : user.getInviteCodes()) {
//            if (ic.getId() == invCode.getId()) {
//                userAlHaveCode = true;
//                break;
//            }
//        }
        boolean userAlHaveCode = authService.doesUserHaveCode(user, invCode);
        if (userAlHaveCode) {
            return new ResponseEntity<>("User already have this code", HttpStatus.BAD_REQUEST);
        }
        userService.giveInviteCode2User(invCode, user);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @GetMapping("/invitecodes")
    public ResponseEntity<Object> allInviteCodes() {
        return new ResponseEntity<>(invCodeRepo.allCodes(), HttpStatus.OK);
    }

    @PostMapping("/init_password_reset")
    public ResponseEntity<Object> initPasswordReset(@RequestBody RequestPasswordResetDto dto) {
//        String email = dto.getEmail();
//        User user = userRepo.findUserByEmail(email);
//        if (user != null) {
//            Otp generatedOtp = otpService.addNewOtp(user.getId());
//            emailService.sendTextEmail(email, "Password reset OTP", generatedOtp.getOtp());
//        }
////        else {
////            return new ResponseEntity<>("No matching email found", HttpStatus.BAD_REQUEST);
////        }

        authService.initPasswordReset(dto.getEmail());

        return new ResponseEntity<>(String.format("OTP for password reset sent to %s", dto.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDto dto) {
        System.out.println(dto);
        if (userService.verifyOtpAndResetPassword(dto)) {
            return new ResponseEntity<>("Password's reset.", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Password reset failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
