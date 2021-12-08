package com.example.restservice;

import java.util.concurrent.atomic.AtomicLong;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
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
        public String greeting2(@RequestParam(value = "name", defaultValue = "World") String name) {
                return doThis();
        }

	private String doThis() {

		String res = "BLANK";

		final String reqUrl = UriComponentsBuilder.fromUriString(API_URL)
					.queryParam("serviceKey", API_KEY)
					.queryParam("pageNo", "1")
					.queryParam("numOfRows", "10")
					.queryParam("housePrc", "100000000")
					.queryParam("houseDvcd", "01")
					.queryParam("pnsnPayMthdDvcd", "01")
					.queryParam("mmPayAmtIndcDvcd", "01")
					.queryParam("joinPersBrthDy", "19511201")
					.queryParam("sposBrthDy", "19511201")
					.queryParam("payTermCd", "01")
					.queryParam("wdrwLmtSetpAmt", "0")
					.queryParam("dataType", "JSON")
					.build()
					.toUriString();

		log.info("reqUrl = " + reqUrl);	

		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<String> re = restTemplate.getForEntity(new URI(reqUrl), String.class);
			res = re.getBody();

			log.info("res = " + res);

		} catch(Exception e) {
			e.printStackTrace();
		}

		return res;
	}
}
