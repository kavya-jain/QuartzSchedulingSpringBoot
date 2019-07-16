package controller;

import dto.EmailDto;
import dto.ScheduleEmailResponse;
import jobs.EmailJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@RestController
public class ScheduleEmailController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleEmailController.class);

    @Autowired Scheduler scheduler;

    @PostMapping(value = "/schedule-mail")
    public ResponseEntity<ScheduleEmailResponse> scheduleEmail(@Valid @RequestBody EmailDto emailDto) {
        try {
            ScheduleEmailResponse scheduleEmailResponse;
            ZonedDateTime dateTime = ZonedDateTime.of(emailDto.getDateTime(), emailDto.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())) {
                scheduleEmailResponse = new ScheduleEmailResponse(false, "Invalid date time");
                return ResponseEntity.badRequest().body(scheduleEmailResponse);
            }

            JobDetail jobDetail = buildJobDetail(emailDto);
            Trigger trigger = buildjobTrigger(jobDetail, dateTime);
            scheduler.scheduleJob(jobDetail, trigger);
            scheduleEmailResponse = new ScheduleEmailResponse(true, "Mail scheduled successfully", jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
            return ResponseEntity.ok(scheduleEmailResponse);

        } catch (SchedulerException exception) {
            ScheduleEmailResponse scheduleEmailResponse = new ScheduleEmailResponse(false, "Mail not scheduled");
            return ResponseEntity.badRequest().body(scheduleEmailResponse);
        }
    }

    private JobDetail buildJobDetail(EmailDto emailDto) {
        JobDataMap map = new JobDataMap();
        map.put("email", emailDto.getEmailId());
        map.put("subject", emailDto.getSubject());
        map.put("body", emailDto.getBody());

        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-jobs")
                .withDescription("Send Email Job")
                .usingJobData(map)
                .storeDurably()
                .build();
    }

    private Trigger buildjobTrigger(JobDetail jobDetail, ZonedDateTime dateTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "email-jobs")
                .withDescription("Send Email Trigger")
                .startAt(Date.from(dateTime.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
