package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.lambda.url.AuthType;
import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
	roleName = "hello_world-role",
	isPublishVersion = true,
	aliasName = "${lambdas_alias_name}",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(authType = AuthType.NONE )
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {
	@Override
	public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
		System.out.println("Lambda function triggered.");

		String path = (String) event.getOrDefault("rawPath", event.get("path"));
		Map<String, Object> requestContext = (Map<String, Object>) event.get("requestContext");
		Map<String, Object> httpData = requestContext != null ? (Map<String, Object>) requestContext.get("http") : new HashMap<>();
		String method = (String) httpData.getOrDefault("method", event.get("httpMethod"));

		Map<String, Object> response = new HashMap<>();
		response.put("headers", Map.of("Content-Type", "application/json"));

		if ("/hello".equals(path) && "GET".equals(method)) {
			response.put("statusCode", 200);
			response.put("body", "{\"statusCode\": 200, \"message\": \"Hello from Lambda!\"}");
		} else {
			String errorMessage = String.format("Invalid request. Path: %s, Method: %s", path, method);
			response.put("statusCode", 400);
			response.put("body", "{\"statusCode\": 400, \"message\": \"" + errorMessage + "\"}");
		}

		return response;
	}
}