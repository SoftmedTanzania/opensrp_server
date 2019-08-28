package org.opensrp.web.listener;

import java.util.concurrent.TimeUnit;

import org.opensrp.common.AllConstants;
import org.opensrp.connector.dhis2.DHIS2DatasetPush;
import org.opensrp.connector.openmrs.constants.OpenmrsConstants;
import org.opensrp.scheduler.RepeatingSchedule;
import org.opensrp.scheduler.TaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {
	public static final String APPLICATION_ID = "/opensrp";
    public static final String APPLICATION_ID_FULL = "org.springframework.web.context.WebApplicationContext:"+APPLICATION_ID;

    private TaskSchedulerService scheduler;
    
    private RepeatingSchedule formSchedule;
    private RepeatingSchedule eventsSchedule;
    //private RepeatingSchedule anmReportScheduler;
    //private RepeatingSchedule mctsReportScheduler;
    private RepeatingSchedule openmrsScheduleSyncerScheduler;
    private RepeatingSchedule atomfeedSchedule;
    private RepeatingSchedule encounterSchedule;
    private RepeatingSchedule checkReferralStatusSchedule;
    private RepeatingSchedule dhis2Schedule;
    
    @Autowired
    public ApplicationStartupListener(TaskSchedulerService scheduler, 
    		@Value("#{opensrp['form.poll.time.interval']}") int formPollInterval,
    		@Value("#{opensrp['mcts.poll.time.interval.in.minutes']}") int mctsPollIntervalInHours,
    		@Value("#{opensrp['openmrs.scheduletracker.syncer.interval-min']}") int openmrsSchSyncerMin) {
        this.scheduler = scheduler;
        formSchedule = new RepeatingSchedule(AllConstants.FORM_SCHEDULE_SUBJECT, 2, TimeUnit.MINUTES, formPollInterval, TimeUnit.HOURS);
        //anmReportScheduler = new RepeatingSchedule(DrishtiScheduleConstants.ANM_REPORT_SCHEDULE_SUBJECT, 10, TimeUnit.MINUTES, 6, TimeUnit.HOURS);
        //mctsReportScheduler = new RepeatingSchedule(DrishtiScheduleConstants.MCTS_REPORT_SCHEDULE_SUBJECT, 10, TimeUnit.MINUTES, mctsPollIntervalInHours, TimeUnit.HOURS);
        eventsSchedule = new RepeatingSchedule(AllConstants.EVENTS_SCHEDULE_SUBJECT, 2, TimeUnit.MINUTES, formPollInterval, TimeUnit.HOURS);

        // TODO openmrsScheduleSyncerScheduler = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_TRACKER_SYNCER_SUBJECT, 2, TimeUnit.MINUTES, openmrsSchSyncerMin, TimeUnit.MINUTES);
        atomfeedSchedule = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_OPENMRS_ATOMFEED_SYNCER_SUBJECT, 5, TimeUnit.MINUTES, 2, TimeUnit.HOURS);
        encounterSchedule = new RepeatingSchedule(OpenmrsConstants.SCHEDULER_OPENMRS_DATA_PUSH_SUBJECT, 5, TimeUnit.MINUTES, 2, TimeUnit.HOURS);
        checkReferralStatusSchedule = new RepeatingSchedule(AllConstants.OpenSRPEvent.CHECK_REFERRAL_STATUS, 5, TimeUnit.MINUTES, 1, TimeUnit.DAYS);
        dhis2Schedule = new RepeatingSchedule(DHIS2DatasetPush.SCHEDULER_DHIS2_DATA_PUSH_SUBJECT, 5, TimeUnit.MINUTES, 2, TimeUnit.HOURS);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    	System.out.println(contextRefreshedEvent.getApplicationContext().getId());
        if (contextRefreshedEvent.getApplicationContext().getId().endsWith(APPLICATION_ID)) {
            scheduler.startJob(formSchedule);
            scheduler.startJob(eventsSchedule);
            //scheduler.startJob(anmReportScheduler);
            //scheduler.startJob(mctsReportScheduler);
           // scheduler.startJob(openmrsScheduleSyncerScheduler);
            scheduler.startJob(atomfeedSchedule);
            scheduler.startJob(encounterSchedule);
            scheduler.startJob(dhis2Schedule);
            scheduler.startJob(checkReferralStatusSchedule);
            
        	System.out.println("STARTED ALL SCHEDULES");
        }
    }
}
