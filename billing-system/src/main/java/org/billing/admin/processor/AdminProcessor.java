package org.billing.admin.processor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.billing.admin.data.Login;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

@Controller
public class AdminProcessor {

	public JSONObject submitLogin(Login login) throws MalformedURLException, IOException {
		String result = "";
		HttpPost post = new HttpPost("http://localhost:8900/host/api/member/login");

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("username", login.getUsername()));
		urlParameters.add(new BasicNameValuePair("password", login.getPassword()));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject memberInfo(String token) throws MalformedURLException, IOException {
		String result = "";
		HttpGet get = new HttpGet("http://localhost:8900/host/api/member/profile");

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public Map<String, String> getMenu(String token, String path) throws IOException {
		Map<String, String> menuMap = new HashMap<String, String>();
		String result = "";
		HttpGet get = new HttpGet("http://localhost:8900/host/api/member/menu");

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		JSONObject menu = new JSONObject(result);
		String welcomeMenu = menu.getJSONObject("payload").getString("welcomeMenu");
		JSONArray ja = menu.getJSONObject("payload").getJSONArray("menu");
		Map<String, Integer> pathMap = new HashMap<String, Integer>();
		for (int i = 0; i < ja.length(); i++) {
			if (ja.getJSONObject(i).getJSONArray("childMenu").length() != 0) {
				for (int j = 0; j < ja.getJSONObject(i).getJSONArray("childMenu").length(); j++) {
					pathMap.put(ja.getJSONObject(i).getJSONArray("childMenu").getJSONObject(j).getString("link"),
							ja.getJSONObject(i).getJSONArray("childMenu").getJSONObject(j).getInt("parentID"));
				}
			}
		}

		Integer parentID = pathMap.get(path) == null ? 0 : pathMap.get(path);

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ja.length(); i++) {
			String name = ja.getJSONObject(i).getString("name");
			String icon = ja.getJSONObject(i).getString("icon");
			int id = ja.getJSONObject(i).getInt("id");
			String badge = "";
			String link = "#";
			if (ja.getJSONObject(i).has("link")) {
				link = ja.getJSONObject(i).getString("link");
			}
			if (ja.getJSONObject(i).has("badge")) {
				badge = ja.getJSONObject(i).getString("badge");
			}
			String linkActive = link.equalsIgnoreCase(path) ? "active" : "";
			if (ja.getJSONObject(i).getJSONArray("childMenu").length() == 0) {
				sb.append("<li class='nav-item'>" + "<a href='" + link + "' class='nav-link " + linkActive + "'>"
						+ "<i class='" + icon + "'></i>" + "<p> " + name + " " + badge + "</p></a></li>");
			} else {
				String plinkActive = id == parentID ? "active" : "";
				sb.append("<li class='nav-item has-treeview'><a href='#'" + "class='nav-link " + plinkActive
						+ "'> <i class='" + icon + "'></i>" + "<p>" + name + "<i class='fas fa-angle-left right'></i>"
						+ "</p></a>" + "<ul class='nav nav-treeview'>");

				for (int j = 0; j < ja.getJSONObject(i).getJSONArray("childMenu").length(); j++) {
					String cname = ja.getJSONObject(i).getJSONArray("childMenu").getJSONObject(j).getString("name");
					String clink = ja.getJSONObject(i).getJSONArray("childMenu").getJSONObject(j).getString("link");
					String clinkActive = clink.equalsIgnoreCase(path) ? "active" : "";
					sb.append("<li class='nav-item'><a href='" + clink + "'" + "class='nav-link " + clinkActive
							+ "'> <i class='far fa-circle nav-icon'></i>" + "<p>" + cname + "</p>" + "</a></li>");
				}
				sb.append("</ul></li>");
			}
		}

		menuMap.put("menu", sb.toString());
		menuMap.put("welcomeMenu", welcomeMenu);
		return menuMap;
	}

	public JSONObject loadMember(String token, String currentPage, String pageSize)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/member");
		builder.setParameter("currentPage", currentPage).setParameter("pageSize", pageSize);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadMemberByUsername(String token, String username)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/member/username/" + username);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadMemberByID(String token, String id)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/member/" + id);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject createMember(String name, String msisdn, String email, String address, String idcard,
			String token) throws MalformedURLException, IOException {
		String result = "";
		HttpPost post = new HttpPost("http://localhost:8900/host/api/member");

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("name", name));
		urlParameters.add(new BasicNameValuePair("msisdn", msisdn));
		urlParameters.add(new BasicNameValuePair("email", email));
		urlParameters.add(new BasicNameValuePair("address", address));
		urlParameters.add(new BasicNameValuePair("idCard", idcard));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject searchMemberByBilling(String token, String username, String billingID)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/member/billing/username/" + username);
		builder.setParameter("billingID", billingID);

		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadMemberByBilling(String token, String billingID, String currentPage, String pageSize)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/member/billing");
		builder.setParameter("currentPage", currentPage).setParameter("pageSize", pageSize)
				.setParameter("billingID", billingID).setParameter("availability", "true");
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadBilling(String token, String currentPage, String pageSize)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/billing");
		builder.setParameter("currentPage", currentPage).setParameter("pageSize", pageSize);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadBillingByID(String token, String billingID)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/billing/" + billingID);
		HttpGet get = new HttpGet(builder.build());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public JSONObject loadBillingItem(String token, String id)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/billing/item");
		builder.setParameter("billingID", id);
		HttpGet get = new HttpGet(builder.build());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public String updateBillingItem(String token, String items, String id)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/billing/item");
		HttpPost post = new HttpPost(builder.build());
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		post.setHeader("Accept", "*/*");
		post.setHeader("Content-type", "application/x-www-form-urlencoded");

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("billingID", id));
		params.add(new BasicNameValuePair("items", items));
		post.setEntity(new UrlEncodedFormEntity(params));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return result;
	}

	public JSONObject loadInvoice(String token, String currentPage, String pageSize)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice");
		builder.setParameter("currentPage", currentPage).setParameter("pageSize", pageSize);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadInvoiceByNumber(String token, String invoiceNo)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/number/" + invoiceNo);
		HttpGet get = new HttpGet(builder.build());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public JSONObject loadInvoiceByID(String token, String id)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/" + id);
		HttpGet get = new HttpGet(builder.build());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public String createInvoice(String token, String json)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/bulk");
		HttpPost post = new HttpPost(builder.build());
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		post.setHeader("Accept", "application/json");
		post.setHeader("Content-type", "application/json");

		StringEntity entity = new StringEntity(json);
		post.setEntity(entity);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return result;
	}

	public JSONObject loadPublishedInvoice(String token, String billingID, int currentPage, int pageSize, String start,
			String end) throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/publish/billing");
		builder.setParameter("currentPage", String.valueOf(currentPage))
				.setParameter("pageSize", String.valueOf(pageSize)).setParameter("startDate", start)
				.setParameter("endDate", end).setParameter("billingID", billingID);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public JSONObject loadPublishedInvoiceStatus(String token, String billingID, int currentPage, int pageSize,
			String start, String end, String status) throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/publish/billing");
		builder.setParameter("currentPage", String.valueOf(currentPage))
				.setParameter("pageSize", String.valueOf(pageSize)).setParameter("startDate", start)
				.setParameter("endDate", end).setParameter("billingID", billingID).setParameter("status", status);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public JSONObject loadPublishedInvoiceByMember(String token, String billingID, String username)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/publish/member");
		builder.setParameter("username", username).setParameter("billingID", billingID);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}
		return new JSONObject(result);
	}

	public JSONObject loadInvoiceStat(String token, String billingID, String start, String end)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/invoice/stat");
		builder.setParameter("startDate", start).setParameter("endDate", end).setParameter("billingID", billingID);
		HttpGet get = new HttpGet(builder.build());
		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject createBilling(String name, String desc, String cycle, String outstanding, String token)
			throws MalformedURLException, IOException {
		String result = "";
		HttpPost post = new HttpPost("http://localhost:8900/host/api/billing");

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("name", name));
		urlParameters.add(new BasicNameValuePair("description", desc));
		urlParameters.add(new BasicNameValuePair("billingCycle", cycle));
		urlParameters.add(new BasicNameValuePair("outstanding", outstanding));

		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		post.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

	public JSONObject loadMessage(String currentPage, String pageSize, String token)
			throws MalformedURLException, IOException, URISyntaxException {
		String result = "";
		URIBuilder builder = new URIBuilder("http://localhost:8900/host/api/message");
		builder.setParameter("currentPage", currentPage).setParameter("pageSize", pageSize);
		HttpGet get = new HttpGet(builder.build());

		get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(get)) {
			result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		return new JSONObject(result);
	}

}
