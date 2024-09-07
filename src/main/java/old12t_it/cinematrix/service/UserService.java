package old12t_it.cinematrix.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import old12t_it.cinematrix.dtos.ResetPasswordDto;
import old12t_it.cinematrix.dtos.response.UserWithBanStatusDto;
import old12t_it.cinematrix.entity.InviteCode;
import old12t_it.cinematrix.entity.User;
import old12t_it.cinematrix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class UserService {
    private final MongoTemplate template;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private OtpService otpService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // This function gives invite code to user which can be used to invite other
    // users
    public void giveInviteCode2User(InviteCode inviteCode, User user) {
        template.update(User.class)
                .matching(where("id").is(user.getId()))
                .apply(new Update().push("inviteCodesId", inviteCode.getId()))
                .first();
        template.update(InviteCode.class)
                .matching(where("id").is(inviteCode.getId()))
                .apply(new Update().set("inviterId", user.getId()))
                .first();
    }

    public boolean isInviteCodeValid(InviteCode inviteCode) {
        return !inviteCode.isCanceled() && inviteCode.getOwnerId() == null;
    }

    public boolean isInviteCodeIsGiven(InviteCode inviteCode) {
        return inviteCode.getInviterId() != null || inviteCode.getOwnerId() != null;
    }

    public void setOwnerForInviteCode(InviteCode inviteCode, User user) {
        template.update(InviteCode.class)
                .matching(where("id").is(inviteCode.getId()))
                .apply(new Update().set("ownerId", user.getId()))
                .first();
        template.update(User.class)
                .matching(where("id").is(user.getId()))
                .apply(new Update().set("ownInviteCodeId", inviteCode.getId()))
                .first();
    }
    public void addDeviceTokens(String deviceToken, User user) {
        template.update(User.class)
                .matching(where("id").is(user.getId()))
                .apply(new Update().push("deviceTokens", deviceToken))
                .first();
    }

    private void updatePassword(long userId, String hashString) {
        template.update(User.class)
                .matching(where("_id").is(userId))
                .apply(new Update().set("hashString", hashString))
                .first();
    }

    public boolean verifyOtpAndResetPassword(ResetPasswordDto dto) {
        User user = userRepo.findUserByEmail(dto.getEmail());
        if (user == null) {
            return false;
        }
        if (!otpService.verifyOtp(user.getId(), dto.getOtp())) {
            return false;
        }
        String hashString = passwordEncoder.encode(dto.getNewPassword());
        updatePassword(user.getId(), hashString);
        return true;
    }

    public boolean banUser(long userId) {
        return (template.updateFirst(
                new Query(Criteria.where("_id").is(userId)),
                new Update().set("isBanned", true),
                User.class
        ).getModifiedCount()) == 1;
    }

    public boolean unbanUser(long userId) {
        return (template.updateFirst(
                new Query(Criteria.where("_id").is(userId)),
                new Update().set("isBanned", false),
                User.class
        ).getModifiedCount()) == 1;
    }

    public List<UserWithBanStatusDto> getUsersByUsernameOrEmail(String username, String email) {
        List<User> users = userRepo.findByNameOrEmail(username, email);
        return users.stream()
                .map(user -> new UserWithBanStatusDto(user.getId(), user.getFullName(), user.getEmail(), user.getLastActiveDate(), user.getRole(), user.isBanned()))
                .collect(Collectors.toList());
    }
}
