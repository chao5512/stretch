
import org.junit.Test;

import org.springframework.security.jwt.Jwt;

import org.springframework.security.jwt.JwtHelper;



public class JwtTest {

    @Test

    public void test() {

        String token = "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE1MjQwNzE5MzYsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiYWRtaW4iXSwianRpIjoiMWRmNDViZWItYzUzMy00MzYzLWE2YzUtNTJhMDAwM2RiOWQ3IiwiY2xpZW50X2lkIjoiY2xpZW50Iiwic2NvcGUiOlsiYXBwIl19.J62CWw0yHYZU3YGitjm5sHLjWlym9NTGZTjJkYN3-vY7kOhf1pdVmEdnzNQ7My5GPomnqUnxrK53dmNIjHNN7NMrr7Ws-7wLtOFZyMOC_gyFb3OoFAviFc9nzkZkiqjFwHus8P-iCIkUi7vX0T-xS9ng5hNRQAaL7CcxkddHJEWnEz3B4OEvpUICTMpNGeD4MHNjryu2XJD3AlIrWVgmKlxzLY46a2gad9OfV3q_VqntQOwSDPJLt2wGDJF-_2RlKTROtS2YMTKWXA5vfTn3i8HgogvcvLP71HOcHSTft780hFUVbKyPAwj4ojMVQQ-qXBC0bToF85aei6UQjsP5WQ";
        Jwt jwt = JwtHelper.decode(token);

        System.out.println(jwt.toString());

    }

}
