package jobs;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Component
public class EmailJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(EmailJob.class);

    @Autowired JavaMailSender mailSender;

    @Autowired MailProperties mailProperties;

    public EmailJob() {
        super();
    }

    @Override protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("executing job : {}", context.getJobDetail().getKey().getName());

        JobDataMap jobDataMap = context.getMergedJobDataMap();
        sendMail(jobDataMap.get("subject").toString(), jobDataMap.get("body").toString(), mailProperties.getUsername(), jobDataMap.get("email").toString());

    }

    private void sendMail(String subject, String body, String sender, String reciever) {
        try {
            logger.info("Sending mail");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
            messageHelper.setFrom(sender);
            messageHelper.setTo(reciever);
            messageHelper.setText(body, true);
            messageHelper.setSubject(subject);
            mailSender.send(message);
        } catch(MessagingException e) {
            logger.error(e.getLocalizedMessage());
        }
    }
}
