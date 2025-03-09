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
	String path = (String) event.getOrDefault("rawPath", "");
	String method = (String) ((Map<String, Object>) event.getOrDefault("requestContext", new HashMap<>()))
			.getOrDefault("httpMethod", "");

	Map<String, Object> response = new HashMap<>();

	if ("/hello".equals(path) && "GET".equals(method)) {
		response.put("statusCode", 200);
		response.put("body", "{\"message\": \"Hello from Lambda!\"}");
	} else {
		response.put("statusCode", 400);
		response.put("body", String.format("{\"error\": \"Bad Request: %s %s\"}", method, path));
	}

	return response;
}
}
