package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import org.apache.http.protocol.HTTP;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		isPublishVersion = false,
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaUrlConfig(
		authType = AuthType.NONE,
		invokeMode = InvokeMode.BUFFERED
)
public class HelloWorld implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
		APIGatewayV2HTTPResponse response;
		try {
			String requestPath = event.getRawPath();
			String httpMethod = (event.getRequestContext() != null && event.getRequestContext().getHttp() != null)
					? event.getRequestContext().getHttp().getMethod()
					: null;


				response = APIGatewayV2HTTPResponse.builder()
						.withStatusCode(200)
						.withBody("{\"statusCode\": 200, \"message\": \"Hello from Lambda\"}")
						.build();

		} catch (Exception ex) {
			context.getLogger().log("Error: " + ex.getMessage());
			response = APIGatewayV2HTTPResponse.builder()
					.withStatusCode(500)
					.withBody("{\"statusCode\": 500, \"message\": \"Internal Server Error\"}")
					.build();
		}
		return response;
	}
}
