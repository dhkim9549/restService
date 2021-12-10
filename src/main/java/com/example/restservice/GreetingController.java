package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.net.URI;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingController {

	private static final Logger log = LoggerFactory.getLogger(GreetingController.class);

	private static final String API_KEY = "6ESlzwjwUPd3w56tc4CmFuGtXEmlRhYiIpC1SljwhKU3iyaDjaVWHtgzNcyfHmt3EytO2cPE%2BrFNA3E6BA6XpA%3D%3D";
	private static final String API_KEY_DEC = "6ESlzwjwUPd3w56tc4CmFuGtXEmlRhYiIpC1SljwhKU3iyaDjaVWHtgzNcyfHmt3EytO2cPE+rFNA3E6BA6XpA==";
	private static final String API_URL = "http://apis.data.go.kr/B551408/rg-mon-pmt-amt/pmt-info";

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@GetMapping("/greeting2")
        public JsonNode greeting2(
			@RequestParam(value = "housePrc", defaultValue = "100000000") String housePrc,
			@RequestParam(value = "age", defaultValue = "60") int age 
			) {

		log.info("housePrc = " + housePrc);
		log.info("age = " + age);

		Calendar cal = new GregorianCalendar(Locale.KOREA);
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, -age);

		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
		String brthDy = fm.format(cal.getTime());
		log.info("brthDy = " + brthDy);	

		final String reqUrl = UriComponentsBuilder.fromUriString(API_URL)
					.queryParam("serviceKey", API_KEY)
					.queryParam("pageNo", "1")
					.queryParam("numOfRows", "10")
					.queryParam("housePrc", housePrc)
					.queryParam("houseDvcd", "01")
					.queryParam("pnsnPayMthdDvcd", "01")
					.queryParam("mmPayAmtIndcDvcd", "01")
					.queryParam("joinPersBrthDy", brthDy)
					.queryParam("sposBrthDy", brthDy)
					.queryParam("payTermCd", "01")
					.queryParam("wdrwLmtSetpAmt", "0")
					.queryParam("dataType", "JSON")
					.build()
					.toUriString();

		log.info("reqUrl = " + reqUrl);	

		RestTemplate restTemplate = new RestTemplate();
		JsonNode jsonNode = null;

		try {
			ResponseEntity<JsonNode> re = restTemplate.getForEntity(new URI(reqUrl), JsonNode.class);

			HttpHeaders headers = re.getHeaders();
			log.info("headers = " + headers);

			jsonNode = re.getBody();
			log.info("jsonNode = " + jsonNode);

			JsonNode jn1 = jsonNode.at("/header");
			log.info("jn1 = " + jn1);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return jsonNode;
	}
}
