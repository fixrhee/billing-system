package org.billing.admin.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.billing.admin.processor.AdminProcessor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Controller
public class DashboardController {

	@Autowired
	private AdminProcessor adminProcessor;
	@Autowired
	private HazelcastInstance instance;

	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView dashboard(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "index");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			return new ModelAndView("index");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/member", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView member(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			return new ModelAndView("member");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/memberData", method = RequestMethod.GET)
	public String memberData(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "search[value]") String search)
			throws IOException, URISyntaxException, ParseException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		if (!search.isEmpty()) {
			if (search.length() <= 10) {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);

			} else {
				JSONObject payload = adminProcessor.loadMemberByUsername(sessionID, search);
				if (payload.getString("status").equalsIgnoreCase("PROCESSED")) {
					Map<String, Object> jsonData = new HashMap<String, Object>();
					jsonData.put("id", payload.getJSONObject("payload").getInt("id"));
					jsonData.put("username", payload.getJSONObject("payload").getString("username"));
					jsonData.put("name", payload.getJSONObject("payload").getString("name"));
					jsonData.put("email", payload.getJSONObject("payload").getString("email"));
					String bDate = payload.getJSONObject("payload").getString("createdDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					format.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date date = format.parse(bDate);
					String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					jsonData.put("createdDate", finalDate);
					jsonMap.put("recordsTotal", 1);
					jsonMap.put("recordsFiltered", 1);
					jsonList.add(jsonData);

				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}

			}
		} else {
			JSONObject data = adminProcessor.loadMember(sessionID, String.valueOf(start), String.valueOf(length));
			JSONObject payload = data.getJSONObject("payload");
			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				Map<String, Object> jsonData = new HashMap<String, Object>();
				jsonData.put("id", payload.getJSONArray("body").getJSONObject(i).get("id"));
				jsonData.put("username", payload.getJSONArray("body").getJSONObject(i).get("username"));
				jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).get("name"));
				jsonData.put("email", payload.getJSONArray("body").getJSONObject(i).get("email"));
				String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = format.parse(bDate);
				String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				jsonData.put("createdDate", finalDate);
				jsonList.add(jsonData);
				jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
				jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
			}
		}
		jsonMap.put("data", jsonList);
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@ResponseBody
	@RequestMapping(value = "/memberBillingData", method = RequestMethod.GET)
	public String memberBillingData(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "billingID") String billingID, @RequestParam(value = "search[value]") String search)
			throws IOException, URISyntaxException, ParseException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		if (!search.isEmpty()) {
			if (search.length() <= 10) {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);
			} else {
				JSONObject payload = adminProcessor.loadMemberByUsername(sessionID, search);
				if (payload.getString("status").equalsIgnoreCase("PROCESSED")) {
					Map<String, Object> jsonData = new HashMap<String, Object>();
					jsonData.put("id", payload.getJSONObject("payload").getInt("id"));
					jsonData.put("username", payload.getJSONObject("payload").getString("username"));
					jsonData.put("name", payload.getJSONObject("payload").getString("name"));
					jsonData.put("email", payload.getJSONObject("payload").getString("email"));
					String bDate = payload.getJSONObject("payload").getString("createdDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					format.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date date = format.parse(bDate);
					String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					jsonData.put("createdDate", finalDate);
					jsonMap.put("recordsTotal", 1);
					jsonMap.put("recordsFiltered", 1);
					jsonList.add(jsonData);

				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}

			}
		} else {
			JSONObject data = adminProcessor.loadMemberByBilling(sessionID, billingID, String.valueOf(start),
					String.valueOf(length));
			JSONObject payload = data.getJSONObject("payload");
			System.out.println(payload.getJSONArray("body").length());
			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				Map<String, Object> jsonData = new HashMap<String, Object>();
				jsonData.put("id", payload.getJSONArray("body").getJSONObject(i).get("id"));
				jsonData.put("username", payload.getJSONArray("body").getJSONObject(i).get("username"));
				jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).get("name"));
				jsonData.put("email", payload.getJSONArray("body").getJSONObject(i).get("email"));
				String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = format.parse(bDate);
				String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				jsonData.put("createdDate", finalDate);
				jsonList.add(jsonData);
				jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
				jsonMap.put("recordsFiltered", payload.getInt("filteredRecord"));
			}
		}
		jsonMap.put("data", jsonList);
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@RequestMapping(value = "/billing", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView billing(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			return new ModelAndView("billing");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/billingData", method = RequestMethod.GET)
	public String billingData(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length)
			throws IOException, URISyntaxException, ParseException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		JSONObject data = adminProcessor.loadBilling(sessionID, String.valueOf(start), String.valueOf(length));
		JSONObject payload = data.getJSONObject("payload");

		for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
			Map<String, Object> jsonData = new HashMap<String, Object>();
			jsonData.put("id", payload.getJSONArray("body").getJSONObject(i).get("id"));
			jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).get("name"));
			jsonData.put("description", payload.getJSONArray("body").getJSONObject(i).get("description"));
			jsonData.put("billingCycle", payload.getJSONArray("body").getJSONObject(i).get("billingCycle"));
			boolean outstanding = payload.getJSONArray("body").getJSONObject(i).getBoolean("outstanding");
			String ot = outstanding == true ? "<span class='right badge badge-success'>True</span>"
					: "<span class='right badge badge-danger'>False</span>";
			jsonData.put("outstanding", ot);
			String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = format.parse(bDate);
			String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
			jsonData.put("createdDate", finalDate);
			jsonList.add(jsonData);
		}

		jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
		jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
		jsonMap.put("data", jsonList);

		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@RequestMapping(value = "/invoice", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView invoice(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "invoice");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			return new ModelAndView("invoice");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/invoiceData", method = RequestMethod.GET)
	public String invoiceData(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "search[value]") String search)
			throws IOException, URISyntaxException, ParseException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		if (!search.isEmpty()) {
			if (search.length() <= 12) {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);
			} else {
				JSONObject payload = adminProcessor.loadInvoiceByNumber(sessionID, search);
				if (payload.getString("status").equalsIgnoreCase("PROCESSED")) {
					Map<String, Object> jsonData = new HashMap<String, Object>();
					jsonData.put("id", payload.getJSONObject("payload").getInt("id"));
					jsonData.put("name", payload.getJSONObject("payload").getJSONObject("member").getString("name"));
					jsonData.put("billing",
							payload.getJSONObject("payload").getJSONObject("billing").getString("name"));
					jsonData.put("invoiceNo", payload.getJSONObject("payload").getString("invoiceNumber"));
					jsonData.put("amount", payload.getJSONObject("payload").getDouble("amount"));

					boolean active = payload.getJSONObject("payload").getBoolean("active");
					if (active == false) {
						jsonData.put("status", "<span class='right badge badge-warning'>INACTIVE</span>");
					} else {
						String badgeStatus = "<span class='right badge badge-info'>UNPUBLISHED</span>";
						if (payload.getJSONObject("payload").has("publishInvoice")) {
							String status = payload.getJSONObject("payload").getJSONObject("publishInvoice")
									.getString("status");
							if (status.equalsIgnoreCase("PAID")) {
								badgeStatus = "<span class='right badge badge-success'>PAID</span>";
							} else {
								badgeStatus = "<span class='right badge badge-danger'>UNPAID</span>";
							}
						}
						jsonData.put("status", badgeStatus);
					}
					jsonList.add(jsonData);
					jsonMap.put("recordsTotal", 1);
					jsonMap.put("recordsFiltered", 1);
				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}
			}
		} else {
			JSONObject data = adminProcessor.loadInvoice(sessionID, String.valueOf(start), String.valueOf(length));
			JSONObject payload = data.getJSONObject("payload");

			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				Map<String, Object> jsonData = new HashMap<String, Object>();
				jsonData.put("id", payload.getJSONArray("body").getJSONObject(i).get("id"));
				jsonData.put("name",
						payload.getJSONArray("body").getJSONObject(i).getJSONObject("member").getString("name"));
				jsonData.put("billing",
						payload.getJSONArray("body").getJSONObject(i).getJSONObject("billing").getString("name"));
				jsonData.put("invoiceNo", payload.getJSONArray("body").getJSONObject(i).get("invoiceNumber"));
				jsonData.put("amount", payload.getJSONArray("body").getJSONObject(i).get("amount"));

				boolean active = payload.getJSONArray("body").getJSONObject(i).getBoolean("active");
				if (active == false) {
					jsonData.put("status", "<span class='right badge badge-warning'>INACTIVE</span>");
				} else {
					String badgeStatus = "<span class='right badge badge-info'>UNPUBLISHED</span>";
					if (payload.getJSONArray("body").getJSONObject(i).has("publishInvoice")) {
						String status = payload.getJSONArray("body").getJSONObject(i).getJSONObject("publishInvoice")
								.getString("status");
						if (status.equalsIgnoreCase("PAID")) {
							badgeStatus = "<span class='right badge badge-success'>PAID</span>";
						} else {
							badgeStatus = "<span class='right badge badge-danger'>UNPAID</span>";
						}
					}
					jsonData.put("status", badgeStatus);
				}

				String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = format.parse(bDate);
				String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				jsonData.put("createdDate", finalDate);
				jsonList.add(jsonData);
				jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
				jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
			}
		}

		jsonMap.put("data", jsonList);
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@RequestMapping(value = "/createInvoice", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView createInvoice(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "billingID") String billingID, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			model.addAttribute("billingID", billingID);
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			return new ModelAndView("createInvoice");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitInvoice", method = { RequestMethod.POST })
	public ModelAndView submitInvoice(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "billing") String billingID, @RequestParam(value = "item") String[] item,
			@RequestParam(value = "amount") String[] amount, @RequestParam(value = "member") String[] memberID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			System.out.println("[Billing : " + billingID + "]");
			StringJoiner description = new StringJoiner(";");
			for (int i = 0; i < item.length; i++) {
				description.add(item[i] + "," + amount[i]);
			}
			System.out.println("[Description : " + description + "]");
			adminProcessor.createInvoice(sessionID, billingID, "", memberID, "150000");
			return new ModelAndView("invoice");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

}
