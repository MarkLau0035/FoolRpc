package cn.llynsyw.rpc.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class NetAddressUtil {

	/**
	 * IP 地址校验的正则表达式
	 */
	private static final Pattern IPV4_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	/**
	 * 获取 IP 地址的服务列表
	 */
	private static final String[] IPV4_SERVICES = {
			"http://checkip.amazonaws.com/",
			"https://ipv4.icanhazip.com/",
			"http://bot.whatismyipaddress.com/"
			// and so on ...
	};

	public static String getPubIp() throws ExecutionException, InterruptedException {
		List<Callable<String>> callables = new ArrayList<>();
		for (String ipService : IPV4_SERVICES) {
			callables.add(() -> getIPFromService(ipService));
		}

		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			// 返回第一个成功获取的 IP
			return executorService.invokeAny(callables);
		} finally {
			executorService.shutdown();
		}
	}

	private static String getIPFromService(String url) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			String ip = in.readLine();
			if (IPV4_PATTERN.matcher(ip).matches()) {
				return ip;
			} else {
				throw new IOException("invalid IPv4 address: " + ip);
			}
		}
	}

}