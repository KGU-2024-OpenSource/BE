package com.be_provocation.auth.service;

import com.be_provocation.auth.domain.EmailTask;
import com.be_provocation.auth.util.LogSanitizerUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAsyncService {

    private static final int MAX_BATCH_SIZE = 10;
    private static final BlockingQueue<EmailTask> emailQueue = new LinkedBlockingQueue<>();
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendEmailAsync(String to, String verificationCode) throws MessagingException {
        StringBuilder body = new StringBuilder();
        body.append("<h3>안녕하세요 CheckMate입니다.</h3>");
        body.append("<h3>요청하신 인증 번호입니다.</h3>");
        body.append("<h1>").append(verificationCode).append("</h1>");
        body.append("<h3>감사합니다.</h3>");
        String content = body.toString();
        log.debug("Sending email to: {}", LogSanitizerUtil.sanitizeForLog(to));
        emailQueue.add(new EmailTask(to, "Check Mate 인증번호입니다.", content));
    }

    @Async
    @Scheduled(fixedRate = 1000)
    public void processEmailQueue() {
        try {
            List<EmailTask> batch = new ArrayList<>();
            EmailTask task;
            while ((task = emailQueue.poll()) != null) {
                batch.add(task);
                if (batch.size() >= MAX_BATCH_SIZE) {
                    sendBatchEmail(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                sendBatchEmail(batch);
            }
        } catch (Exception e) {
            log.debug("Error processing email queue: {}", e.getMessage(), e);
        }
    }

    public void sendBatchEmail(List<EmailTask> emailTasks) throws MessagingException {
        MimeMessage[] mimeMessages = new MimeMessage[emailTasks.size()];
        for (int i = 0; i < emailTasks.size(); i++) {
            EmailTask task = emailTasks.get(i);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, false, "UTF-8");
            messageHelper.setFrom(sender);
            messageHelper.setTo(task.getTo());
            messageHelper.setSubject(task.getSubject());
            messageHelper.setText(task.getContent(), true);

            mimeMessages[i] = message;
        }

        try {
            javaMailSender.send(mimeMessages);
        } catch (Exception e) {
            log.error("Error sending batch email: {}", e.getMessage());
        }
        log.debug("Batch email sent successfully.");
    }
}
