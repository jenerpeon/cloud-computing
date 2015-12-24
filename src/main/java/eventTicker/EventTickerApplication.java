package eventTicker;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import frontend.EventTickerUI;

@SpringBootApplication
//@ComponentScan
public class EventTickerApplication {
//	private static final Logger log = LoggerFactory.getLogger(EventTickerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EventTickerApplication.class, args);
	}



}
