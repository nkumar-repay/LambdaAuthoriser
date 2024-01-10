package com.repay.auth.dto.opa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Input {
    private String jwt;
    @JsonProperty("requested_permission")
    private String requestedPermission;
    @JsonProperty("requested_org_id")
    private String requestedOrgId;

}