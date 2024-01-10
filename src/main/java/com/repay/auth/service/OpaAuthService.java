package com.repay.auth.service;

import com.repay.auth.dto.opa.OpaAuthRequest;
import com.repay.auth.dto.opa.OpaAuthResponse;
import com.repay.auth.enums.Effect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class OpaAuthService {

    private static RestTemplate restTemplate;

    {
        restTemplate = new RestTemplate();
    }

    public  Effect validateToken(OpaAuthRequest opaAuthRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String opaUrl = "https://opa-auth.internal.uat.repay.net/v1/data/http/authz";
        try {
            ResponseEntity<OpaAuthResponse> responseEntity = restTemplate.postForEntity(opaUrl, opaAuthRequest, OpaAuthResponse.class);
            OpaAuthResponse opaAuthResponse = responseEntity.getBody();
            // Extract and print the response body
            log.error("Opa AUTH response:{}", responseEntity.getBody());
            return opaAuthResponse.getResult().isAuthenticated() && opaAuthResponse.getResult().isAuthorized() ? Effect.Allow : Effect.Deny;

        } catch (Exception e) {
            log.error("Error while fetching opa auth details from opa url:{}",opaUrl);
            return Effect.Deny;
        }
    }

    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        // Handle invalid or missing "Bearer " prefix
        return null;
    }

}
