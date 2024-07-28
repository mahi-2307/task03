package com.task03;

import com.amazonaws.services.apigateway.AmazonApiGateway;
import com.amazonaws.services.apigateway.AmazonApiGatewayClientBuilder;
import com.amazonaws.services.apigateway.model.CreateDeploymentRequest;
import com.amazonaws.services.apigateway.model.CreateResourceRequest;
import com.amazonaws.services.apigateway.model.CreateResourceResult;
import com.amazonaws.services.apigateway.model.CreateRestApiRequest;
import com.amazonaws.services.apigateway.model.CreateRestApiResult;
import com.amazonaws.services.apigateway.model.GetResourcesRequest;
import com.amazonaws.services.apigateway.model.GetResourcesResult;
import com.amazonaws.services.apigateway.model.PutIntegrationRequest;
import com.amazonaws.services.apigateway.model.PutMethodRequest;

public class ApiGatewayConfig {
    private static final String API_NAME = "task3_api";
    private static final String STAGE_NAME = "api";
    private static final String region = "eu-central-1"; // replace with your region
    private static final String accountId = "196241772369"; // replace with your account ID

    public static void main(String[] args) {
        AmazonApiGateway apiGatewayClient = AmazonApiGatewayClientBuilder.defaultClient();

        // Create API
        CreateRestApiRequest createRestApiRequest = new CreateRestApiRequest().withName(API_NAME);
        CreateRestApiResult createRestApiResult = apiGatewayClient.createRestApi(createRestApiRequest);
        String restApiId = createRestApiResult.getId();

        // Get Root Resource ID
        GetResourcesRequest getResourcesRequest = new GetResourcesRequest().withRestApiId(restApiId);
        GetResourcesResult getResourcesResult = apiGatewayClient.getResources(getResourcesRequest);
        String rootResourceId = getResourcesResult.getItems().get(0).getId();

        // Create /hello resource
        CreateResourceRequest createResourceRequest = new CreateResourceRequest()
                .withRestApiId(restApiId)
                .withParentId(rootResourceId)
                .withPathPart("hello");
        CreateResourceResult createResourceResult = apiGatewayClient.createResource(createResourceRequest);
        String helloResourceId = createResourceResult.getId();

        // Create GET method
        PutMethodRequest putMethodRequest = new PutMethodRequest()
                .withRestApiId(restApiId)
                .withResourceId(helloResourceId)
                .withHttpMethod("GET")
                .withAuthorizationType("NONE");
        apiGatewayClient.putMethod(putMethodRequest);

        // Create Integration
        PutIntegrationRequest putIntegrationRequest = new PutIntegrationRequest()
                .withRestApiId(restApiId)
                .withResourceId(helloResourceId)
                .withHttpMethod("GET")
                .withType("AWS_PROXY")
                .withIntegrationHttpMethod("POST")
                .withUri("arn:aws:apigateway:" + region + ":lambda:path/2015-03-31/functions/arn:aws:lambda:" + region + ":" + accountId + ":function:hello_world/invocations");
        apiGatewayClient.putIntegration(putIntegrationRequest);

        // Deploy API
        apiGatewayClient.createDeployment(new CreateDeploymentRequest()
                .withRestApiId(restApiId)
                .withStageName(STAGE_NAME));
    }
}
