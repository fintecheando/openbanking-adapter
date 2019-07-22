/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 * https://mozilla.org/MPL/2.0/.
 */
package hu.dpc.ob.rest.processor.ob.api;

import hu.dpc.ob.domain.entity.Consent;
import hu.dpc.ob.model.service.ConsentService;
import hu.dpc.ob.rest.dto.ob.api.PisApiConsentResponseDto;
import hu.dpc.ob.util.ContextUtils;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;

@Component("api-ob-pis-funds-processor")
public class PisFundsRequestProcessor extends ApiRequestProcessor {

    private ConsentService consentService;

    @Autowired
    public PisFundsRequestProcessor(ConsentService consentService) {
        this.consentService = consentService;
    }

    @Override
    @Transactional
    public void process(Exchange exchange) throws Exception {
        super.process(exchange);

        String consentId = ContextUtils.getPathParam(exchange, ContextUtils.PARAM_CONSENT_ID);
        @NotNull Consent consent = consentService.getConsentById(consentId);

        PisApiConsentResponseDto response = null; // PisApiConsentResponseDto.create(consent);
        exchange.getIn().setBody(response);
    }
}
