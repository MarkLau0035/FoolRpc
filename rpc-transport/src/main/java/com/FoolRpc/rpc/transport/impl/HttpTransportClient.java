package com.FoolRpc.rpc.transport.impl;

import com.FoolRpc.rpc.common.constants.Constants;
import com.FoolRpc.rpc.transport.TransportClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Http客户端
 *
 * @author luolinyuan
 * @date 2022/3/29
 **/
@Slf4j
public class HttpTransportClient implements TransportClient {
	private String url;
	private HttpURLConnection urlConnection;
	private boolean isConnected;

	@Override
	public void setUrl(String url) {
		url = url.replace("'", "");
		this.url = "http://" + url;
	}

	@Override
	public void connect() {
		try {
			urlConnection = (HttpURLConnection) new URL(url).openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(Constants.NETWORK_CONNECTION_TIMEOUT_MS);
			urlConnection.setReadTimeout(Constants.NETWORK_SESSION_TIME_TIMEOUT_MS);
			urlConnection.connect();
			isConnected = true;
			log.info("client connect to {} successfully",url);
		} catch (Exception e) {
			//throw new IllegalStateException("try to connect to  fail",url);
		}
	}

	@Override
	public InputStream write(InputStream data) {
		try {
			IOUtils.copy(data, urlConnection.getOutputStream());
			/*因为一个HttpURLConnection连接只能发送一次请求*/
			isConnected = false;
			return urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK ? urlConnection.getInputStream() :
					urlConnection.getErrorStream();
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

	@Override
	public boolean isConnected() {
		return isConnected;
	}
}
