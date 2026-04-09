package com.oxigeno.portal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

 
 

@Configuration
@EnableScheduling
public class Scheduler {
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);
 
	
	@Value("${scheduler.email.datesfrom}")
	private int sendmail_datesfrom;
	
	@Value("${scheduler.email.maxcount}")
	private int maxcount;

	@Value("${email.enabled}")
	private boolean emailEnabled;
	/*
	 * Envio de mails programado
	 * 
	 * Cron expression is represented by six fields:
	 * second, minute, hour, day of month, month, day(s) of week
	 * (*) means match any
	 * /X means "every X"
	 * cron="${scheduler.cron}"
	 * "0 0 * * * *" = the top of every hour of every day.
	 * "* /10 * * * * *" = every ten seconds.
	 * "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	 * "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
	 * "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	 * "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	 * "0 0 0 25 12 ?" = every Christmas Day at midnight
	 */
	
}
