/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 * https://mozilla.org/MPL/2.0/.
 */
package hu.dpc.ob.rest.component;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;

@Component
public class RestClient {

    private static Logger log = LoggerFactory.getLogger(RestClient.class);

    static final DateTimeFormatter HEADER_DATE_TIME_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    private RestTemplate restTemplate;

    @Autowired
    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String call(String endpointUrl, HttpMethod httpMethod, Map<String, String> headers, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        headers.forEach(httpHeaders::set);

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);

        log.debug(String.format("Sending request %s, method: %s, \nheader: %s, \nbody: %s, ", endpointUrl, httpMethod, StringUtils.join(headers, ","), body));

        ResponseEntity<String> response = restTemplate.exchange(endpointUrl, httpMethod, entity, String.class);

        return response.getBody();
    }

    public static String formatHeaderDate(ZonedDateTime date) {
        return date == null ? null : date.format(HEADER_DATE_TIME_FORMATTER);
    }

    public static String formatServerHeaderDate() {
        return formatHeaderDate(ZonedDateTime.now(ZoneId.of("UTC")));
    }
}
