package old12t_it.cinematrix.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.dtos.Notice;
import old12t_it.cinematrix.dtos.request.UserNotiMsgDto;
import old12t_it.cinematrix.entity.NotiMessage;
import old12t_it.cinematrix.entity.NotiMessageBox;
import old12t_it.cinematrix.entity.User;
import old12t_it.cinematrix.repository.NotificationRepo.NotiMessageBoxRepositionry;
import old12t_it.cinematrix.repository.NotificationRepo.NotiMessageRepository;
import old12t_it.cinematrix.repository.UserRepository;
import old12t_it.cinematrix.service.exception.Exception.RecordNotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final MongoTemplate template;

    private final UserRepository userRepo;
    private final NotiMessageBoxRepositionry notiMsgBoxRepo;
    private final NotiMessageRepository notiMsgRepo;

    private final SequenceGeneratorService seqGenSer;

    public BatchResponse sendNotification(Notice notice) {
        List<String> registrationTokens = notice.getRegistrationTokens();
        Notification notification = Notification.builder()
                .setTitle(notice.getSubject())
                .setBody(notice.getContent())
                .setImage(notice.getImgUrl())
                .build();
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(registrationTokens)
                .setNotification(notification)
                .putAllData(notice.getData())
                .build();
        BatchResponse batchResp = null;
        try {
            batchResp = firebaseMessaging.sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            System.out.println("Firebase error " + e.getMessage());
        }
        if (batchResp != null && batchResp.getFailureCount() > 0) {
            List<SendResponse> responses = batchResp.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    failedTokens.add(registrationTokens.get(i));
                }
            }
            System.out.println("List of tokens that caused failures: " + failedTokens);
        }
        return batchResp;
    }


    public boolean sendNoti2User(UserNotiMsgDto msgDto) {
        Optional<User> getUser = userRepo.findUserById(msgDto.getUserId());
        if (getUser.isEmpty())
            throw new RecordNotFoundException("User not found for id:" + msgDto.getUserId());
        else {
            User user = getUser.get();
            List<String> userDeviceTokens = user.getDeviceTokens().stream().toList();

            Notice notice = new Notice(msgDto.getSubject(), msgDto.getContent(), msgDto.getImgUrl(), msgDto.getData(),
                    userDeviceTokens);
            BatchResponse resp = sendNotification(notice);
            NotiMessage notiMessage = new NotiMessage(seqGenSer.generateSequence(NotiMessage.SEQUENCE_NAME),
                    msgDto.getSubject(), msgDto.getContent(), msgDto.getImgUrl(), LocalDate.now(), false);
            notiMsgRepo.save(notiMessage);
            NotiMessageBox msgBox = notiMsgBoxRepo.findByUser(user.getId());
            if (msgBox == null) {
                List<NotiMessage> initMsgLst = new ArrayList<>();
                initMsgLst.add(notiMessage);
                msgBox = new NotiMessageBox(seqGenSer.generateSequence(NotiMessageBox.SEQUENCE_NAME), user, initMsgLst);
                notiMsgBoxRepo.save(msgBox);
            } else {
                template.update(NotiMessageBox.class)
                        .matching(where("id").is(msgBox.getId()))
                        .apply(new Update().push("messages", notiMessage.getId()))
                        .first();
            }
            return resp.getFailureCount() <= 1;
        }
    }

    public List<NotiMessage> getMessageEntityLst(List<Long> msgIdLst) {
        List<NotiMessage> msgEnLst = new ArrayList<>();
        for (long l : msgIdLst) {
            NotiMessage findingEn = this.notiMsgRepo.findById(l);
            if (findingEn != null)
                msgEnLst.add(findingEn);
        }
        return msgEnLst;
    }

    public void confirmReadMessage(NotiMessage msg) {
        template.update(NotiMessage.class)
                .matching(where("id").is(msg.getId()))
                .apply(new Update().set("isSeen", true))
                .first();
    }
}
