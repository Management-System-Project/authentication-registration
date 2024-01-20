package store.glacies.authentication.sevices;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import store.glacies.authentication.config.CognitoIdentityProviderConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@State(Scope.Thread)
@SpringBootTest(classes = {AuthenticationService.class})
@ExtendWith(SpringExtension.class)
@Import(CognitoIdentityProviderConfig.class)
public class AuthenticationServiceTest {


    @Autowired
    private AuthenticationService service;


    /*TODO ADD CODES TO ENVIRONMENTAL VALUES
    *  RECONSIDER ADDING STATE*/
    @Test
    public void testAuthenticateCognito() {

        String authorizationCode = "17139fd9-4721-423f-96ec-7f991a2034cf";
        String state = "H4sIAAAAAAAAAJWQy1KDMBSG3yXrUkNTILCzN62j2Ju01nGckAtEAgFCa1vHdzeudOvyn_P9lzmfgIAI8INT6bbLHfdtKuNdq-BzCHogtacbrTPFraBWuBDi1C8GeVBDkae6YbkZ-sKn0gLMAnnX1Sa6usoUoZKbPjl0ef83vU9KctEV1VklO92nurQ-bn1Us58O8bcwA9EL4CWRygpd80oy8NoDhWUWPNtOxbZuJ3s0WjSPpEzx_CbpLhPjPc1vdWzMOtQwJIzF-wadqKq67Vpc8vM8mFwno1l9Sp_2Vbusd-EHL70kuIfwPIFFIReL1k3MQYxmKPSyu-N5i0anTUvHK-Q_LN2HdZPsgkG5Wa5WZHqdj09oqfH0HZ9lp-Lb43vcrBPpb4xHMlWIhtrtyi7-z0NKELkB9HzPDYfDHqhBJIgyvAdaG0S8wBMcDhwXc-gMGWIOHgTI4QILlPocY4bA1zcAfoFb1wEAAA.H4sIAAAAAAAAAAEgAN__TtI7b9UfVhGjhCpuv6__T02gmzWqpyQwqVAje_rY1PSIJTT0IAAAAA.2";

        JSONObject tokens = service.sendAuthenticate(authorizationCode);

        // Assert that the accessToken is equal to expectedAccessToken
        assertNotNull(tokens);
    }

    @Test
    public void testRegisterOptimized(){
        String code = "17139fd9-4721-423f-96ec-7f991a2034cf";
        String group = "Sellers";

        String jwtToken="eyJraWQiOiJUUUQrc3p3b3JzTFNWMkxZVFFzRUZyd3VEd2g3d1VJMERGcUFESnRiYzd3PSIsImFsZyI6IlJTMjU2In0.eyJhdF9oYXNoIjoiS05TU2pmUE5Zci1TVDFpRlhfR1dWZyIsInN1YiI6IjI5MTA2ODY5LTk4YmEtNDczMC05YzQyLTExNDliNjRjZjk3NSIsImNvZ25pdG86Z3JvdXBzIjpbImV1LW5vcnRoLTFfRWlOWHJsMFk5X0ZhY2Vib29rIl0sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LW5vcnRoLTEuYW1hem9uYXdzLmNvbVwvZXUtbm9ydGgtMV9FaU5YcmwwWTkiLCJjb2duaXRvOnVzZXJuYW1lIjoiRmFjZWJvb2tfMTIyMTAzMjIwNDY2MTg2NTk3IiwiZ2l2ZW5fbmFtZSI6IkdsYWNpZXMiLCJvcmlnaW5fanRpIjoiZjMxMTZlNTMtNGY1OS00YWJiLWIyNTgtM2FiOTJjNDhmYmE3IiwiYXVkIjoiMTAwOGI2azJoN3AwZmhib3FkaHM0NmY2Y2kiLCJpZGVudGl0aWVzIjpbeyJ1c2VySWQiOiIxMjIxMDMyMjA0NjYxODY1OTciLCJwcm92aWRlck5hbWUiOiJGYWNlYm9vayIsInByb3ZpZGVyVHlwZSI6IkZhY2Vib29rIiwiaXNzdWVyIjpudWxsLCJwcmltYXJ5IjoidHJ1ZSIsImRhdGVDcmVhdGVkIjoiMTcwNTc1MjA2NTQ1MSJ9XSwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE3MDU3NTI2MTMsImV4cCI6MTcwNTc1NjIxMywiaWF0IjoxNzA1NzUyNjEzLCJmYW1pbHlfbmFtZSI6IkdsYWNpZXMiLCJqdGkiOiI0YWFkY2NhYy1hNDQxLTQwMzktOGI2Ni05MDg3YTRiMjVlYmIiLCJlbWFpbCI6ImluZm9AZ2xhY2llcy5zdG9yZSJ9.ao2Rfjv_9QVFxdsHE_FxrLTJ0rrSs-Di6rlax4KzJJLKZVNvRkvILUnCa3WK1WDS_Lmqs3vrzx75U0hDSJuE66F9IXtq0rrKZbgezkobKEWryjXjimAQb1wHIIyTcH7QMxjQ31lo3FT70TWv2peUNKCCWVjmT0KY2bAC3Ao0NXIWm2QqNbzbSkT4f5MvC3TopdGth3C17NEUtgOY1Hz40v2oiuoC39DS8F_2bWsFQhGH1QxRw5HkdFihQcGwn1BCzEUUpoX_IkpY3D34keMAJDyWvYx902tBmOSiAVzDdJ2bdUWRN_-6K3Cgzbe2Q_A-tnM_IDoqFuLM4wrKaIyz2w";

        service.registerOptimized(code,group);
    }

}
