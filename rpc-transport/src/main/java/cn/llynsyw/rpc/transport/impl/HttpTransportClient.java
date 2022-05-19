package cn.llynsyw.rpc.transport.impl;

import cn.llynsyw.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * TODO
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Slf4j
public class HttpTransportClient implements TransportClient {
	private String url;
	HttpURLConnection urlConnection;

	@Override
	public void connect(String url) {
		url = url.replace("'", "");
		this.url = "http://" + url;
	}

	@Override
	public InputStream write(InputStream data) {
		try {
			log.info("client connect");
			urlConnection = (HttpURLConnection) new URL(url).openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod("POST");
			urlConnection.connect();
			IOUtils.copy(data, urlConnection.getOutputStream());

			int resultCode = urlConnection.getResponseCode();
			if (resultCode == HttpURLConnection.HTTP_OK) {
				return urlConnection.getInputStream();
			} else {
				return urlConnection.getErrorStream();
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public void close() {
		if (urlConnection != null) {
			urlConnection.disconnect();
		}
	}
}
