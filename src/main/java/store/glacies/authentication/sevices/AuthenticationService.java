package store.glacies.authentication.sevices;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import store.glacies.authentication.models.AuthenticationInfo;
import store.glacies.authentication.repositories.AuthenticationRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    private final CognitoIdentityProviderClient cognitoClient;

    private final CloseableHttpClient httpClient;

    private final AuthenticationRepository repository;

    @Autowired

    public AuthenticationService(CognitoIdentityProviderClient cognitoClient,
                                 CloseableHttpClient httpClient,
                                 AuthenticationRepository repository) {
        this.cognitoClient = cognitoClient;
        this.httpClient = httpClient;
        this.repository = repository;
    }

    @Value("${aws.cognito.client.id}")
    private String APP_CLIENT_ID;

    @Value("${aws.cognito.identity.pool.id}")
    private String IDENTITY_POOL_ID;

    @Value("${aws.cognito.redirect.uri}")
    private String REDIRECT_URI;


    /*uses AWS Cognito to finish authentication process for a user with the help of
     * Federated Identity providers (such as Google and Facebook). The access code
     * granted by one of the mentioned Federated Identity providers is redirected t
     * to AWS Cognito, which handles the exchange and returns the necessary
     * id token, access token and refresh token. If the user is not present in the
     * Cognito User pool, the user is automatically stored in the Pool.
     * Returns the Map of the provided tokens.*/
    public JSONObject sendAuthenticate(String code) {
        try {
            HttpPost httpPost = new HttpPost("https://glacies.auth.eu-north-1.amazoncognito.com/oauth2/token");

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "authorization_code"));
            params.add(new BasicNameValuePair("client_id", APP_CLIENT_ID));
            params.add(new BasicNameValuePair("code", code));
            //params.add(new BasicNameValuePair("state", state));
            params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

            return new JSONObject(responseBody);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred.Try later again.");
        }
    }

    /*authenticates using with Cognito Federated Identity Provider
    * using code grant authentication flow,
    * cashes the necessary information extracted from id token
    * for future use in authentication and authorization low*/

    /*TODO CONSIDER GENERATING MORE SAFE ID TOKEN IN COMPARISON TO PROVIDED ONE*/
    public void loginWithFederatedProvider(String code) {
            JSONObject json = sendAuthenticate(code);
            String accessToken = json.getString("access_token");
            String idToken = json.getString("id_token");
            String refreshToken = json.getString("refresh_token");
            DecodedJWT jwt = JWT.decode(idToken);
            String sub = jwt.getClaim("sub").asString();  // Get the sub claim
            String username = jwt.getClaim("cognito:username").asString();
            List<String> groups = jwt.getClaim("cognito:groups").asList(String.class);
            AuthenticationInfo authInfo = AuthenticationInfo.builder()
                .UUID(sub)
                .username(username)
                .refreshToken(refreshToken)
                .idToken(idToken)
                .accessToken(accessToken)
                //.role(jwt.getClaim())
                .build();
            repository.save(authInfo);
    }


    /*registers your with Cognito Federated Identity Provider,
    adds authenticated user to the necessary group,
    stores the provided data in cash for later authentication
    and authorization use.*/
    public void registerOptimized(String code, String groupName) {
        JSONObject json = sendAuthenticate(code);
        String accessToken = json.getString("access_token");
        String idToken = json.getString("id_token");
        String refreshToken = json.getString("refresh_token");
        DecodedJWT jwt = JWT.decode(idToken);
        String sub = jwt.getClaim("sub").asString();  // Get the sub claim
        String username = jwt.getClaim("cognito:username").asString();

        // Find the user in the Cognito User Pool
        String filter = "username = \"" + username + "\"";
        ListUsersRequest listUsersRequest = ListUsersRequest.builder()
                .userPoolId(IDENTITY_POOL_ID)
                .filter(filter)
                .build();
        ListUsersResponse listUsersResponse = cognitoClient.listUsers(listUsersRequest);

        // Check if the user exists
        if (listUsersResponse.users().size() == 1) {
            // The user exists, add them to the group
            AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                    .userPoolId(IDENTITY_POOL_ID)
                    .username(username)  // Use the username
                    .groupName(groupName)
                    .build();
            cognitoClient.adminAddUserToGroup(addUserToGroupRequest);
        } else {
            // Handle the case where the user does not exist or there are multiple users with the same sub
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user does not exist or there are multiple users with the same username.");
        }

        AuthenticationInfo authInfo = AuthenticationInfo.builder()
                .UUID(sub)
                .username(username)
                .refreshToken(refreshToken)
                .idToken(idToken)
                .accessToken(accessToken)
                .role(groupName)
                .build();
        repository.save(authInfo);
    }


}

