package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTests {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Test
    public void LoginSuccessTest() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder()
                .username("pv-6@gmail.co")
                .password("Price123$")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(), 200);

        AuthResponseDto responseDto = gson.fromJson(response.body().string(), AuthResponseDto.class);
        System.out.println(responseDto.getToken());

        //eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicHYtNkBnbWFpbC5jbyIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjgyNTI0Njk3LCJpYXQiOjE2ODE5MjQ2OTd9.QgOVdYIn7jSKAGzchq9MI-S9ZA0UeO61rPWolWiJxK8

    } @Test
    public void LoginWithWrongEmailTest() throws IOException {

        AuthRequestDto auth = AuthRequestDto.builder()
                .username("pv-6gmail.co")
                .password("Price123$")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/login/usernamepassword")
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
       // System.out.println(errorDto.getError());
       // System.out.println(errorDto.getMessage());
       // System.out.println(errorDto.getPath());
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getMessage(),"Login or Password incorrect");

    }
}
