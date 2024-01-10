package com.repay.auth.dto.opa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result {
        private boolean authenticated;
        private boolean authorized;
        @JsonProperty("invalid_request")
        private String invalidRequest;
        @JsonProperty("jwt_is_verified")
        private boolean jwtIsVerified;
        @JsonProperty("repay_org_id")
        private String repayOrgId;
        @JsonProperty("requested_org_id")
        private String requestedOrgId;
        @JsonProperty("requested_permission")
        private String requestedPermission;
}