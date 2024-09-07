package old12t_it.cinematrix.controller;

import com.google.firebase.messaging.BatchResponse;
import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.dtos.Notice;
import old12t_it.cinematrix.dtos.request.NotiMessageListDto;
import old12t_it.cinematrix.dtos.request.UserDto;
import old12t_it.cinematrix.dtos.request.UserNotiMsgDto;
import old12t_it.cinematrix.entity.NotiMessage;
import old12t_it.cinematrix.entity.NotiMessageBox;
import old12t_it.cinematrix.entity.User;
import old12t_it.cinematrix.repository.NotificationRepo.NotiMessageBoxRepositionry;
import old12t_it.cinematrix.repository.UserRepository;
import old12t_it.cinematrix.service.NotificationService;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notify")
@RequiredArgsConstructor
public class NotificationController {
    private  final NotificationService notificationService;
    private  final UserRepository userRepo;
    private  final NotiMessageBoxRepositionry msgBoxRepo;

    @PostMapping("/send-notification")
    public BatchResponse sendNotification(@RequestBody Notice notice) {
        return notificationService.sendNotification(notice);
    }
    @PostMapping("/send-notification/user")
    public ResponseEntity<?> sendNoti2User(@RequestBody UserNotiMsgDto dto) {
        boolean sendState = notificationService.sendNoti2User(dto);
        if (sendState) {
            return new ResponseEntity<>("send success to user device", HttpStatus.OK);
        }
        return new ResponseEntity<>("send success but user device is not on", HttpStatus.OK);
    }
    @GetMapping("/get-notification/user")
    public ResponseEntity<?> getNoti4User(UserDto dto) {
        Optional<User> getUser = userRepo.findUserById(Long.parseLong(dto.getId()));
        if (getUser.isEmpty())
            throw new RecordNotFoundException("User not found for id:" + dto.getId());
        User user = getUser.get();
        NotiMessageBox msgBox = msgBoxRepo.findByUser(user.getId());
        if (msgBox == null) {
            return new ResponseEntity<>("cannot find notification message box", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(msgBox.getMessages(), HttpStatus.OK);
    }
    @PostMapping("/confirm-read/user")
    public ResponseEntity<?> confirmReadUser(@RequestBody NotiMessageListDto msgLstDto ) throws IOException {
        List<NotiMessage> msgEntityLst = notificationService.getMessageEntityLst(msgLstDto.getNotiMsgLst());
        for (NotiMessage msg : msgEntityLst) {
            notificationService.confirmReadMessage(msg);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
