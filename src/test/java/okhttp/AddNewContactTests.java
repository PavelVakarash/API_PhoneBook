package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ContactResponseDto;
import dto.ErrorDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class AddNewContactTests {

    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoicHYtNkBnbWFpbC5jbyIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNjgyNTI0Njk3LCJpYXQiOjE2ODE5MjQ2OTd9.QgOVdYIn7jSKAGzchq9MI-S9ZA0UeO61rPWolWiJxK8";

    @Test
    public void addNewContactSuccessTest() throws IOException {

        int i = new Random().nextInt(1000) + 1000;

        ContactDto contactDto = ContactDto.builder()
                .name("Oliver")
                .lastName("Kan")
                .email("kan" +i + "@gm.co")
                .phone("1234512345" +i)
                .address("Berlin")
                .description("goalkeeper")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        ContactResponseDto resDto = gson.fromJson(response.body().string(),ContactResponseDto.class);
        System.out.println(resDto.getMessage());
        Assert.assertTrue(resDto.getMessage().contains("Contact was added!"));

    }

    @Test
    public void addNewContactFormatErrorTest() throws IOException {

        int i = new Random().nextInt(1000) + 1000;

        ContactDto contactDto = ContactDto.builder()
                .name("")
                .lastName("Kan")
                .email("kan" +i + "@gm.co")
                .phone("1234512345" +i)
                .address("Berlin")
                .description("goalkeeper")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getError());
         System.out.println(errorDto.getMessage());
         System.out.println(errorDto.getPath());
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getMessage().toString(),"{name=must not be blank}");
        Assert.assertEquals(errorDto.getPath(),"/v1/contacts");
    }

    @Test(enabled = false)  // не забыть убрать первую букву токена (e)
    public void addNewContactUnauthorizedTest() throws IOException {

        int i = new Random().nextInt(1000) + 1000;

        ContactDto contactDto = ContactDto.builder()
                .name("Oliver")
                .lastName("Kan")
                .email("kan" +i + "@gm.co")
                .phone("1234512345" +i)
                .address("Berlin")
                .description("goalkeeper")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .addHeader("Authorization",token)
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        //System.out.println(errorDto.getError());
        //System.out.println(errorDto.getMessage());
        //System.out.println(errorDto.getPath());
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getMessage(),"Malformed JWT JSON: ?[\u0019???\u0014?M??");
        Assert.assertEquals(errorDto.getPath(),"/v1/contacts");
    }

    @Test
    public void addNewContactUndocumentedError403Test() throws IOException {

        int i = new Random().nextInt(1000) + 1000;

        ContactDto contactDto = ContactDto.builder()
                .name("Oliver")
                .lastName("Kan")
                .email("kan" +i + "@gm.co")
                .phone("1234512345" +i)
                .address("Berlin")
                .description("goalkeeper")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                //.addHeader("Authorization",token)
                .post(body).build();

        Response response = client.newCall(request).execute();// alt + Enter

        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),403);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
    }
}

