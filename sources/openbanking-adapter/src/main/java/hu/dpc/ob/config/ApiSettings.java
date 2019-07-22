/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 *  https://mozilla.org/MPL/2.0/.
 */
package hu.dpc.ob.config;

import hu.dpc.ob.domain.type.ApiScope;
import hu.dpc.ob.domain.type.ConsentActionCode;
import hu.dpc.ob.domain.type.PermissionCode;
import hu.dpc.ob.domain.type.RequestSource;
import hu.dpc.ob.model.internal.ApiSchema;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

import static hu.dpc.ob.domain.type.PermissionCode.*;

@Configuration
@ConfigurationProperties("api-settings")
@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public class ApiSettings extends SchemaSettings<ApiSettings.ApiHeader, ApiSettings.ApiOperation, ApiSettings.ApiBinding> {

    @Autowired
    public ApiSettings(AdapterSettings adapterSettings) {
        super(adapterSettings);
    }

    @PostConstruct
    public void postConstruct() {
        super.postConstruct();
    }

    @Override
    @NotNull
    public RequestSource getSource() {
        return RequestSource.API;
    }

    @Override
    protected ApiHeader[] getHeaders() {
        return ApiHeader.values();
    }

    @Override
    protected ApiBinding[] getBindings() {
        return ApiBinding.values();
    }

    @Override
    protected ApiOperation[] getOperations() {
        return ApiOperation.values();
    }

    @NotNull
    public List<PermissionCode> getValidPermissions(ApiSchema schema, ApiScope scope) {
        return PermissionCode.getPermissions(scope).stream().filter(p -> getSchema(schema).getPermissions().contains(p.getId())).collect(Collectors.toList());
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public enum ApiHeader implements Header {
        AUTH_DATE("auth-date"),
        CUSTOMER_IP_ADDRESS("customer-ip-address"),
        INTERACTION_ID("interaction-id"),
        ;

        private @NotNull final String configName;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public enum ApiBinding implements Binding {
        ACCOUNTS("accounts", ApiScope.AIS, ConsentActionCode.QUERY_ACCOUNT, true, "Read accounts list", new PermissionCode[]{READ_ACCOUNTS_BASIC, READ_ACCOUNTS_DETAIL},
                new PermissionCode[]{READ_ACCOUNTS_DETAIL}),
        ACCOUNT("account", ApiScope.AIS, ConsentActionCode.QUERY_ACCOUNT, true, "Read account information", new PermissionCode[]{READ_ACCOUNTS_BASIC, READ_ACCOUNTS_DETAIL},
                new PermissionCode[]{READ_ACCOUNTS_DETAIL}),
        BALANCES("balances", ApiScope.AIS, ConsentActionCode.QUERY_BALANCE, true, "Read balances of owned accounts", new PermissionCode[]{READ_BALANCES}),
        BALANCE("balance", ApiScope.AIS, ConsentActionCode.QUERY_BALANCE, true, "Read account balance", new PermissionCode[]{READ_BALANCES}),
        PARTY_PSU("psu-party", ApiScope.AIS, ConsentActionCode.QUERY_PARTY, true, "Read party information on the PSU logged in", new PermissionCode[]{READ_PARTY_PSU}),
        PARTY("account-party", ApiScope.AIS, ConsentActionCode.QUERY_PARTY, true, "Read account owner information", new PermissionCode[]{READ_PARTY}),
        AIS_CONSENT_CREATE("ais-consent-create", ApiScope.AIS, ConsentActionCode.CREATE, false, "Create a new AIS consent resourceId"),
        AIS_CONSENT("ais-consent", ApiScope.AIS, ConsentActionCode.QUERY_CONSENT, false, "Retrieve an AIS consent resourceId"),
        AIS_CONSENT_DELETE("ais-consent-delete", ApiScope.AIS, ConsentActionCode.DELETE, false, "User deletes an AIS consent resourceId"),
        PIS_CONSENT_CREATE("pis-consent-create", ApiScope.PIS, ConsentActionCode.CREATE, false, "Create a new domestic PIS consent resourceId"),
        PIS_CONSENT("pis-consent", ApiScope.PIS, ConsentActionCode.QUERY_CONSENT, false, "Retrieve a domestic PIS consent resourceId"),
        PIS_FUNDS("pis-funds", ApiScope.PIS, ConsentActionCode.QUERY_CONSENT, true, "Confirm funds on a domestic PIS consent resourceId",
                new PermissionCode[]{READ_BALANCES, READ_TRANSACTIONS_BASIC, READ_TRANSACTIONS_DETAIL}),
        PIS_PAYMENT_CREATE("pis-payment-create", ApiScope.PIS, ConsentActionCode.PAYMENT, true, "Initiate domestic payment",
                new PermissionCode[]{READ_BALANCES, READ_TRANSACTIONS_BASIC, READ_TRANSACTIONS_DETAIL}),
        PIS_PAYMENT("pis-payment", ApiScope.PIS, ConsentActionCode.QUERY_PAYMENT, false, "Retrieve a domestic PIS resourceId"),
        PIS_CLIENT_PAYMENT("pis-client-payment", ApiScope.PIS, ConsentActionCode.QUERY_PAYMENT, false, "Retrieve a domestic PIS resourceId by client reference identifier"),
        PIS_PAYMENT_DETAILS("pis-payment-details", ApiScope.PIS, ConsentActionCode.QUERY_PAYMENT, false, "Retrieve a domestic PIS details"),
        ;


        private @NotNull final String configName;
        private @NotNull final ApiScope scope;
        private @NotNull final ConsentActionCode actionCode;
        private @NotNull final boolean userRequest;
        private @NotNull final String displayText;
        private @NotNull final PermissionCode[] permissions;
        private @NotNull final PermissionCode[] detailPermissions;

        ApiBinding(@NotNull String configName, @NotNull ApiScope scope, @NotNull ConsentActionCode actionCode, @NotNull boolean userRequest,
                   @NotNull String displayText) {
            this(configName, scope, actionCode, userRequest, displayText, null, null);
        }

        ApiBinding(@NotNull String configName, @NotNull ApiScope scope, @NotNull ConsentActionCode actionCode, @NotNull boolean userRequest,
                   @NotNull String displayText, PermissionCode[] permissions) {
            this(configName, scope, actionCode, userRequest, displayText, permissions, permissions);
        }

        public PermissionCode[] getPermissions(boolean detail) {
            return null;
        }

        @Override
        @NotNull
        public RequestSource getSource() {
            return RequestSource.API;
        }

        @Override
        @NotNull
        public String getDisplayLabel() {
            return configName;
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public enum ApiOperation implements Operation {
        ;

        private @NotNull final String configName;
        private @NotNull final String displayText;

        @Override
        public String getDisplayLabel() {
            return configName;
        }
    }
}
