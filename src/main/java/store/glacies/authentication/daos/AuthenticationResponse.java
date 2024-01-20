package store.glacies.authentication.daos;

public class AuthenticationResponse {
    private String idToken;
    private String accessToken;
    private String refreshToken;
    private String uuid;
    private String email;

    private String group;

    public AuthenticationResponse(String idToken, String accessToken, String refreshToken, String uuid, String email, String group) {
        this.idToken = idToken;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.uuid = uuid;
        this.email = email;
        this.group = group;
    }

    public AuthenticationResponse() {
    }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public String getIdToken() {
        return this.idToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getEmail() {
        return this.email;
    }

    public String getGroup() {
        return this.group;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public static class AuthenticationResponseBuilder {
        private String idToken;
        private String accessToken;
        private String refreshToken;
        private String uuid;
        private String email;
        private String group;

        AuthenticationResponseBuilder() {
        }

        public AuthenticationResponseBuilder idToken(String idToken) {
            this.idToken = idToken;
            return this;
        }

        public AuthenticationResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthenticationResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationResponseBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public AuthenticationResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationResponseBuilder group(String group) {
            this.group = group;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(this.idToken, this.accessToken, this.refreshToken, this.uuid, this.email, this.group);
        }

        public String toString() {
            return "AuthenticationResponse.AuthenticationResponseBuilder(idToken=" + this.idToken + ", accessToken=" + this.accessToken + ", refreshToken=" + this.refreshToken + ", uuid=" + this.uuid + ", email=" + this.email + ", group=" + this.group + ")";
        }
    }
}
