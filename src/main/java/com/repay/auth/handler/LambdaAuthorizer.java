package com.repay.auth.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import com.amazonaws.services.lambda.runtime.events.IamPolicyResponse;
import com.repay.auth.service.OpaAuthService;
import com.repay.auth.util.JsonUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LambdaAuthorizer implements Function<APIGatewayProxyRequestEvent, IamPolicyResponse> {

    private OpaAuthService opaAuthService;

    private static final String GLOBAL_ARN = "arn:aws:execute-api:*:*:*/*/*";

    @Autowired
    public LambdaAuthorizer(OpaAuthService opaAuthService) {
        this.opaAuthService = opaAuthService;
    }

    public IamPolicyResponse apply(APIGatewayProxyRequestEvent event) {
        Map<String, String> headers = event.getHeaders();
        String authorizationToken = headers.get("Authorization");
        String effect = headers.get("effect");
        String permission = headers.get("permission");
        System.out.println("permission:"+permission);
        String auth = "Deny";
        String jwtToken  = OpaAuthService.extractToken(authorizationToken);
        System.out.println("opaAuthService:"+ opaAuthService);
//        Effect finalEffect = opaAuthService.validateToken(OpaAuthRequest.builder()
//                .input(Input.builder().jwt(jwtToken).requestedOrgId("testing11").requestedPermission("repay_logged_in").build())
//                .build());
//        if ("Allow".equalsIgnoreCase(finalEffect.name())) {
//            auth = "Allow";
//
//        }

        Map<String, Object> ctx = new HashMap<String, Object>();
        ctx.put("sub", "9090990");

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext = event.getRequestContext();
        APIGatewayProxyRequestEvent.RequestIdentity identity = proxyContext.getIdentity();


        String arn = String.format("arn:aws:execute-api:%s:%s:%s/%s/%s/%s",System.getenv("AWS_REGION"), proxyContext.getAccountId(),
                proxyContext.getApiId(), proxyContext.getStage(), proxyContext.getHttpMethod(), "*");

//        Statement statement = Statement.builder().effect(auth).resource(arn).build();
//
//        PolicyDocument policyDocument = PolicyDocument.builder().statements(Collections.singletonList(statement))
//                .build();
        IamPolicyResponse.Statement  statement= effect.equalsIgnoreCase("Allow")?IamPolicyResponse.allowStatement(arn) : IamPolicyResponse.denyStatement(arn);
        Map<String, Object> condtion = new HashMap<>();
        condtion.put("context.sub","9090990");
        Map<String, Map<String,Object>> conditionMap = new HashMap<>();
        conditionMap.put("StringEquals",condtion);
        statement.setCondition(new HashMap<>());
        IamPolicyResponse.PolicyDocument awsPolicyDocument = IamPolicyResponse.PolicyDocument.builder().withVersion("2012-10-17").withStatement(List.of(statement)).build();
        IamPolicyResponse iamPolicyResponse =  IamPolicyResponse.builder().withPolicyDocument(awsPolicyDocument).withPrincipalId(UUID.randomUUID().toString()).withContext(ctx).build();
        System.out.println(JsonUtil.getJsonStringFromObject(iamPolicyResponse));
        return iamPolicyResponse;
    }
}