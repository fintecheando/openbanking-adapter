/*
 * This Source Code Form is subject to the terms of the Mozilla
 * Public License, v. 2.0. If a copy of the MPL was not distributed
 * with this file, You can obtain one at
 *
 * https://mozilla.org/MPL/2.0/.
 */
package hu.dpc.ob.config.type;

import java.util.Base64;

import org.apache.commons.lang3.StringUtils;

public enum AuthEncodeType {
    NONE,
    BASE64 {
        @Override
        public String encode(String value) {
            return value == null ? null : new String(Base64.getEncoder().encode(value.getBytes()));
        }
    },
    ;

    public static AuthEncodeType forConfig(String config) {
        if (StringUtils.isEmpty(config))
            return NONE;
        return AuthEncodeType.valueOf(config.toUpperCase());
    }

    public String encode(String value) {
        return value;
    }
}
