package old12t_it.cinematrix.service;

import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.entity.*;
import old12t_it.cinematrix.repository.InviteCodeRepository;
import old12t_it.cinematrix.repository.NotificationRepo.NotiMessageBoxRepositionry;
import old12t_it.cinematrix.repository.UserRepository;
import old12t_it.cinematrix.security.jwt.JwtTokenService;
import old12t_it.cinematrix.security.service.UserDetailImp;
import old12t_it.cinematrix.service.exception.Exception.LoginFailException;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtService;
    private final UserService userService;
    private final SequenceGeneratorService seqGenService;
    private final UserRepository userRepo;
    private final NotiMessageBoxRepositionry notiMsgRepo;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final EmailService emailService;
    private final InviteCodeRepository invCodeRepo;



    public Authentication login(String email, String password) {
        Authentication authen;
        try {
            authen = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            email,
                            password));
        } catch (Exception e) {
            // TODO: handle exception
            throw new LoginFailException(e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authen);
        return authen;
    }

    public void saveUser(String fullname, String email, String password, InviteCode invCode) {
        // password hashing
        String hashString = passwordEncoder.encode(password);
        User newUser = new User(seqGenService.generateSequence(User.SEQUENCE_NAME), fullname,
                email, hashString);
        userRepo.save(newUser);
        // set owner for invite code
        userService.setOwnerForInviteCode(invCode, newUser);
        //create notification message box for user
        List<NotiMessage> initMsgList = new ArrayList<>();

        NotiMessageBox notiMessageBox = new NotiMessageBox(seqGenService.generateSequence(NotiMessageBox.SEQUENCE_NAME), newUser, initMsgList);
        notiMsgRepo.save(notiMessageBox);
    }

    public boolean doesUserHaveCode(User user, InviteCode invCode) {
        boolean userAlHaveCode = false;
        for (InviteCode ic : user.getInviteCodes()) {
            if (ic.getId() == invCode.getId()) {
                userAlHaveCode = true;
                break;
            }
        }
        return userAlHaveCode;
    }

    public void initPasswordReset(String email) {
        User user = userRepo.findUserByEmail(email);
        if (user != null) {
            Otp generatedOtp = otpService.addNewOtp(user.getId());
            emailService.sendTextEmail(email, "Password reset OTP", generatedOtp.getOtp());
        }
//        else {
//            return new ResponseEntity<>("No matching email found", HttpStatus.BAD_REQUEST);
//        }
    }

}
