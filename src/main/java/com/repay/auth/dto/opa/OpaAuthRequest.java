package com.repay.auth.dto.opa;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpaAuthRequest {
    private Input input;
}
