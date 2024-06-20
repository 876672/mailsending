//package com.mailsendingusingspringbacth.batchConfig;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//@Slf4j
//public class SchedulerConfig {
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private Job job;
//
//    private int executionCount = 0;
//
//    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
//    public void scheduleJob() throws Exception {
//        if (executionCount < 1) {
//        	executionCount++;
//            log.info("Job scheduler started to work");
//            jobLauncher.run(job, new JobParametersBuilder()
//                    .addLong("uniqueness", System.nanoTime()).toJobParameters());
//            log.info("Job scheduler finished to work");
//            System.out.println("executionCount--------------" +executionCount);
//            
//        } else {
//            log.info("Job scheduler stopped after 1 executions.");
//        }
//    }
//}
