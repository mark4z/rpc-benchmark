import lombok.val;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author mark4z
 * @date 2023/3/13 11:37
 */
public class Http2Test {

	@Test
	public void test() throws IOException {
		OkHttpClient client = new OkHttpClient.Builder()
				.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
				.build();
		HttpUrl url = HttpUrl.parse("https://http2.akamai.com/");
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response response = client.newCall(request).execute();
		System.out.println(response.protocol());
	}
}
