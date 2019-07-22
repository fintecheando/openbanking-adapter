/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 * https://mozilla.org/MPL/2.0/.
 */
package hu.dpc.ob.rest.dto.ob.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import hu.dpc.ob.rest.dto.psp.PspClientResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public class SinglePartyData {

    @JsonProperty(value = "Party")
    @Valid
    private PartyData party;

    public SinglePartyData(PartyData party) {
        this.party = party;
    }

    @NotNull
    static SinglePartyData transform(@NotNull PspClientResponseDto pspClient) {
        PartyData data = PartyData.transform(pspClient);
        return new SinglePartyData(data);
    }
}
