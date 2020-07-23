package org.billing.admin.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.billing.admin.processor.AdminProcessor;
import org.billing.admin.processor.TimeAgo;
import org.billing.api.processor.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
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

			String lastTrx = loadTrxHistoryTable(sessionID, member, Utils.getCurrentMonthFirstDate(),
					Utils.getCurrentMonthLastDate(), 0, 5);
			String lastBilling = loadBillingTable(sessionID, member, Utils.getCurrentMonthFirstDate(),
					Utils.getCurrentMonthLastDate(), 0, 5);
			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "index");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			model.addAttribute("lastTrx", lastTrx);
			model.addAttribute("lastBilling", lastBilling);
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

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
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

	@RequestMapping(value = "/memberDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView memberDetail(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "memberID") String memberID, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject memberDetail = adminProcessor.loadMemberByID(sessionID, memberID);
			String username = memberDetail.getJSONObject("payload").getString("username");
			String name = memberDetail.getJSONObject("payload").getString("name");
			String email = memberDetail.getJSONObject("payload").getString("email");
			String address = memberDetail.getJSONObject("payload").getString("address");
			String idcard = memberDetail.getJSONObject("payload").getString("idCard");
			String msisdn = memberDetail.getJSONObject("payload").getString("msisdn");

			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);

			model.addAttribute("memberUsername", username);
			model.addAttribute("memberName", name);
			model.addAttribute("memberEmail", email);
			model.addAttribute("memberAddress", address);
			model.addAttribute("memberIDCard", idcard);
			model.addAttribute("memberMsisdn", msisdn);

			return new ModelAndView("memberDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/createMember", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView createMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			return new ModelAndView("createMember");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitMember", method = { RequestMethod.POST })
	public ModelAndView submitMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "msisdn", required = true) String msisdn,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "idcard", required = false) String idcard, HttpServletResponse response,
			Model model) {
		try {

			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject res = adminProcessor.createMember(name, msisdn, email, address, idcard, sessionID);
			if (res.getString("status").equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("message", "OK");
				model.addAttribute("status", "PROCESSED");
			} else {
				model.addAttribute("message", "FAILED");
				model.addAttribute("status", res.getString("description"));
			}
			return new ModelAndView("redirect:/submitMemberResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitMemberResult", method = { RequestMethod.GET })
	public ModelAndView submitMemberResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") String status, @RequestParam(value = "message") String message,
			HttpServletResponse response, Model model) throws MalformedURLException, IOException {
		try {

			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			if (status.equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("title", "Member Created Successfuly");
				model.addAttribute("message", "New Member has been succesfully created");
				model.addAttribute("notification", "success");
			} else {
				model.addAttribute("title", "Create Member Failed");
				model.addAttribute("message", status);
				model.addAttribute("notification", "error");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			return new ModelAndView("member");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitBulkMember", method = { RequestMethod.POST })
	public ModelAndView submitBulkMember(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam("memberBulk") MultipartFile file, HttpSession session, Model model) {
		try {

			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			Integer memberID = memberInfo.getJSONObject("payload").getInt("id");
			String path = "/Volumes/Data/upload";
			String filename = memberID + "_" + System.currentTimeMillis() + ".csv";
			byte barr[] = file.getBytes();
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();
			model.addAttribute("message", "OK");
			model.addAttribute("status", "PROCESSED");
			return new ModelAndView("redirect:/submitMemberResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/download/{file_name}", method = RequestMethod.GET, produces = "text/csv")
	public void downloadTemplate(@PathVariable("file_name") String fileName, HttpServletResponse response) {
		try {
			response.setContentType("text/csv;charset=utf-8");
			File initialFile = new File("src/main/resources/" + fileName + ".csv");
			InputStream is = new FileInputStream(initialFile);
			org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("IOError writing file to output stream");
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
				JSONObject payload = adminProcessor.searchMemberByBilling(sessionID, search, billingID);
				if (payload.getString("status").equalsIgnoreCase("PROCESSED")) {
					List<Object> jsonListData = new LinkedList<Object>();
					jsonListData.add(payload.getJSONObject("payload").getInt("id"));
					jsonListData.add(payload.getJSONObject("payload").getString("username"));
					jsonListData.add(payload.getJSONObject("payload").getString("name"));
					jsonListData.add(payload.getJSONObject("payload").getString("description"));
					jsonList.add(jsonListData);
					jsonMap.put("recordsTotal", 1);
					jsonMap.put("recordsFiltered", 1);
				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}
			}
		} else {
			JSONObject data = adminProcessor.loadMemberByBilling(sessionID, billingID, String.valueOf(start),
					String.valueOf(length));
			JSONObject payload = data.getJSONObject("payload");
			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				List<Object> jsonListData = new LinkedList<Object>();
				jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("id"));
				jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("username"));
				jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("name"));
				jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("description"));
				jsonList.add(jsonListData);
			}
			jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
			jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
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

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
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

	@RequestMapping(value = "/billingDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView billingDetail(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "billingID", required = true) String billingID,
			@RequestParam(value = "filter") String filter, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			JSONObject dataStat = adminProcessor.loadInvoiceStat(sessionID, String.valueOf(billingID),
					Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate());
			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject billing = adminProcessor.loadBillingByID(sessionID, billingID);
			String billingName = billing.getJSONObject("payload").getString("name");
			String description = billing.getJSONObject("payload").getString("description");
			Integer cycle = billing.getJSONObject("payload").getInt("billingCycle");
			boolean outstanding = billing.getJSONObject("payload").getBoolean("outstanding");

			String bDate = billing.getJSONObject("payload").getString("createdDate");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = format.parse(bDate);
			String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);

			model.addAttribute("billingID", billingID);
			model.addAttribute("filter", filter);
			model.addAttribute("billingName", billingName);
			model.addAttribute("description", description);
			model.addAttribute("billingCycle", cycle);
			model.addAttribute("outstanding", outstanding);
			model.addAttribute("createdDate", finalDate);

			Calendar mCalendar = Calendar.getInstance();
			String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
			int year = mCalendar.get(Calendar.YEAR);

			model.addAttribute("period", month + " " + year);
			model.addAttribute("totalMember", dataStat.getJSONObject("payload").getInt("totalMember"));
			model.addAttribute("totalPaid", dataStat.getJSONObject("payload").getInt("totalPaidMember"));
			model.addAttribute("totalUnpaid", dataStat.getJSONObject("payload").getInt("totalUnpaidMember"));
			return new ModelAndView("billingDetail");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/createBilling", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView createBilling(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			return new ModelAndView("createBilling");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitBilling", method = { RequestMethod.POST })
	public ModelAndView submitBilling(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "description", required = true) String description,
			@RequestParam(value = "cycle", required = true) String cycle,
			@RequestParam(value = "outstanding", required = false) String outstanding, Model model) {
		try {

			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String outs = outstanding != null ? "true" : "false";
			JSONObject res = adminProcessor.createBilling(name, description, cycle, outs, sessionID);
			if (res.getString("status").equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("message", "OK");
				model.addAttribute("status", "PROCESSED");
			} else {
				model.addAttribute("message", "FAILED");
				model.addAttribute("status", res.getString("description"));
			}
			return new ModelAndView("redirect:/submitBillingResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitBillingResult", method = { RequestMethod.GET })
	public ModelAndView submitBillingResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") String status, @RequestParam(value = "message") String message,
			HttpServletResponse response, Model model) throws MalformedURLException, IOException {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			if (status.equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("title", "Billing Created Successfuly");
				model.addAttribute("message", "New Billing has been succesfully created");
				model.addAttribute("notification", "success");
			} else {
				model.addAttribute("title", "Create Billing Failed");
				model.addAttribute("message", status);
				model.addAttribute("notification", "error");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "member");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);

			return new ModelAndView("billing");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
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

			JSONObject dataStat = adminProcessor.loadInvoiceStat(sessionID,
					String.valueOf(payload.getJSONArray("body").getJSONObject(i).getInt("id")),
					Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate());

			jsonData.put("id", payload.getJSONArray("body").getJSONObject(i).get("id"));
			jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).get("name"));
			jsonData.put("description", payload.getJSONArray("body").getJSONObject(i).get("description"));
			jsonData.put("billingCycle", payload.getJSONArray("body").getJSONObject(i).get("billingCycle") + " "
					+ Utils.getCurrentMonthAndYear());
			boolean outstanding = payload.getJSONArray("body").getJSONObject(i).getBoolean("outstanding");
			String ot = outstanding == true ? "<span class='right badge badge-success'>True</span>"
					: "<span class='right badge badge-danger'>False</span>";
			jsonData.put("outstanding", ot);

			if (dataStat.has("payload")) {
				double stat = dataStat.getJSONObject("payload").getDouble("paidPercentage");
				if (stat >= 0 && stat <= 30.0) {
					jsonData.put("stat",
							"<div class='progress progress-xs progress-striped active'><div class='progress-bar bg-danger' style='width: "
									+ stat + "%'></div></div><span class='right badge badge-danger'>" + stat
									+ "%</span></div>");
				} else if (stat >= 31 && stat <= 50.0) {
					jsonData.put("stat",
							"<div class='progress progress-xs progress-striped active'><div class='progress-bar bg-warning' style='width: "
									+ stat + "%'></div></div><span class='right badge badge-warning'>" + stat
									+ "%</span></div>");
				} else {
					jsonData.put("stat",
							"<div class='progress progress-xs progress-striped active'><div class='progress-bar bg-success' style='width: "
									+ stat + "%'></div></div><span class='right badge badge-success'>" + stat
									+ "%</span></div>");
				}
				jsonList.add(jsonData);
			} else {
				jsonData.put("stat",
						"<div class='progress progress-xs progress-striped active'><div class='progress-bar bg-danger' style='width: "
								+ 0 + "%'></div><span class='right badge badge-danger'>0%</span></div>");
			}
		}
		jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
		jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
		jsonMap.put("data", jsonList);

		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@ResponseBody
	@RequestMapping(value = "/billingStatusData", method = RequestMethod.GET)
	public String billingPaidData(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "billingID") String billingID, @RequestParam(value = "filter") String filter,
			@RequestParam(value = "search[value]") String search)
			throws IOException, URISyntaxException, ParseException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		if (!search.isEmpty()) {
			if (search.length() <= 10) {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);
			} else {
				JSONObject data = adminProcessor.loadPublishedInvoiceByMember(sessionID, billingID, search);
				if (data.has("payload")) {
					Map<String, Object> jsonData = new HashMap<String, Object>();
					jsonData.put("username",
							"<a href='memberDetail?memberID="
									+ data.getJSONObject("payload").getJSONObject("body").getJSONObject("member")
											.getInt("id")
									+ "'>" + data.getJSONObject("payload").getJSONObject("body").getJSONObject("member")
											.getString("username")
									+ "</a>");
					jsonData.put("name", data.getJSONObject("payload").getJSONObject("body").getJSONObject("member")
							.getString("name"));
					jsonData.put("amount", Utils.formatAmount(
							(int) data.getJSONObject("payload").getJSONObject("body").getDouble("amount")));
					jsonData.put("invoice", "<a href='invoiceDetail?invoiceID="
							+ data.getJSONObject("payload").getJSONObject("body").getInt("invoiceID") + "'>"
							+ data.getJSONObject("payload").getJSONObject("body").getString("invoiceNumber") + "</a>");
					if (data.getJSONObject("payload").getJSONObject("body").getString("status")
							.equalsIgnoreCase("PAID")) {
						jsonData.put("status", "<span class='right badge badge-success'>PAID</span>");
					} else {
						jsonData.put("status", "<span class='right badge badge-danger'>UNPAID</span>");
					}
					jsonList.add(jsonData);
					jsonMap.put("recordsTotal", 1);
					jsonMap.put("recordsFiltered", 1);
				}
			}
		} else {
			if (filter.equalsIgnoreCase("0")) {
				JSONObject data = adminProcessor.loadPublishedInvoice(sessionID, billingID, start, length,
						Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate());

				if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
					JSONObject payload = data.getJSONObject("payload");
					for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
						Map<String, Object> jsonData = new HashMap<String, Object>();
						jsonData.put("username",
								"<a href='memberDetail?memberID="
										+ payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getInt("id")
										+ "'>" + payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getString("username")
										+ "</a>");
						jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
								.getString("name"));
						jsonData.put("amount", Utils
								.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount")));
						jsonData.put("invoice", "<a href='invoiceDetail?invoiceID="
								+ payload.getJSONArray("body").getJSONObject(i).getInt("invoiceID") + "'>"
								+ payload.getJSONArray("body").getJSONObject(i).getString("invoiceNumber") + "</a>");
						if (payload.getJSONArray("body").getJSONObject(i).getString("status")
								.equalsIgnoreCase("PAID")) {
							jsonData.put("status", "<span class='right badge badge-success'>PAID</span>");
						} else {
							jsonData.put("status", "<span class='right badge badge-danger'>UNPAID</span>");
						}
						jsonList.add(jsonData);
					}
					jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
					jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));

				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}
			} else if (filter.equalsIgnoreCase("1")) {
				JSONObject data = adminProcessor.loadPublishedInvoiceStatus(sessionID, billingID, start, length,
						Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate(), "PAID");
				if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
					JSONObject payload = data.getJSONObject("payload");
					for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
						Map<String, Object> jsonData = new HashMap<String, Object>();
						jsonData.put("username",
								"<a href='memberDetail?memberID="
										+ payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getInt("id")
										+ "'>" + payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getString("username")
										+ "</a>");
						jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
								.getString("name"));
						jsonData.put("amount", Utils
								.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount")));
						jsonData.put("invoice", "<a href='invoiceDetail?invoiceID="
								+ payload.getJSONArray("body").getJSONObject(i).getInt("invoiceID") + "'>"
								+ payload.getJSONArray("body").getJSONObject(i).getString("invoiceNumber") + "</a>");
						if (payload.getJSONArray("body").getJSONObject(i).getString("status")
								.equalsIgnoreCase("PAID")) {
							jsonData.put("status", "<span class='right badge badge-success'>PAID</span>");
						} else {
							jsonData.put("status", "<span class='right badge badge-danger'>UNPAID</span>");
						}
						jsonList.add(jsonData);
					}
					jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
					jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));

				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}
			} else {
				JSONObject data = adminProcessor.loadPublishedInvoiceStatus(sessionID, billingID, start, length,
						Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate(), "UNPAID");
				if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
					JSONObject payload = data.getJSONObject("payload");
					for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
						Map<String, Object> jsonData = new HashMap<String, Object>();
						jsonData.put("username",
								"<a href='memberDetail?memberID="
										+ payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getInt("id")
										+ "'>" + payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
												.getString("username")
										+ "</a>");
						jsonData.put("name", payload.getJSONArray("body").getJSONObject(i).getJSONObject("member")
								.getString("name"));
						jsonData.put("amount", Utils
								.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount")));
						jsonData.put("invoice", "<a href='invoiceDetail?invoiceID="
								+ payload.getJSONArray("body").getJSONObject(i).getInt("invoiceID") + "'>"
								+ payload.getJSONArray("body").getJSONObject(i).getString("invoiceNumber") + "</a>");
						if (payload.getJSONArray("body").getJSONObject(i).getString("status")
								.equalsIgnoreCase("PAID")) {
							jsonData.put("status", "<span class='right badge badge-success'>PAID</span>");
						} else {
							jsonData.put("status", "<span class='right badge badge-danger'>UNPAID</span>");
						}
						jsonList.add(jsonData);
					}
					jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
					jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));

				} else {
					jsonMap.put("recordsTotal", 0);
					jsonMap.put("recordsFiltered", 0);
				}
			}

		}
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

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "invoice");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
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

	@RequestMapping(value = "/invoiceDetail", method = RequestMethod.GET)
	public ModelAndView invoiceDetail(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "invoiceID") String invoiceID, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject invoice = adminProcessor.loadInvoiceByID(sessionID, invoiceID);
			String memberName = invoice.getJSONObject("payload").getJSONObject("member").getString("name");
			String memberEmail = invoice.getJSONObject("payload").getJSONObject("member").getString("email");
			String memberAddress = invoice.getJSONObject("payload").getJSONObject("member").getString("address");
			String memberMsisdn = invoice.getJSONObject("payload").getJSONObject("member").getString("msisdn");
			String invoiceNo = invoice.getJSONObject("payload").getJSONObject("publishInvoice")
					.getString("invoiceNumber");
			Integer billingID = invoice.getJSONObject("payload").getJSONObject("billing").getInt("id");
			JSONObject billing = adminProcessor.loadBillingByID(sessionID, String.valueOf(billingID));
			String billingName = billing.getJSONObject("payload").getString("name");
			String description = billing.getJSONObject("payload").getString("description");
			Integer cycle = billing.getJSONObject("payload").getInt("billingCycle");
			boolean outstanding = billing.getJSONObject("payload").getBoolean("outstanding");

			Map<String, String> menus = adminProcessor.getMenu(sessionID, "invoice");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("memberName", memberName);
			model.addAttribute("memberEmail", memberEmail);
			model.addAttribute("memberAddress", memberAddress);
			model.addAttribute("memberMsisdn", memberMsisdn);
			model.addAttribute("invoiceNo", invoiceNo);
			model.addAttribute("invoiceID", invoiceID);
			model.addAttribute("fromDate", Utils.getCurrentMonthFirstDate());
			model.addAttribute("endDate", Utils.getCurrentMonthLastDate());
			model.addAttribute("billingName", billingName);
			model.addAttribute("description", description);
			model.addAttribute("billingCycle", cycle);
			model.addAttribute("outstanding", outstanding);

			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);

			return new ModelAndView("invoiceDetail");
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
	@RequestMapping(value = "/invoiceHistoryData", method = RequestMethod.GET)
	public String invoiceHistoryData(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "invoiceID") String invoiceID, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "endDate") String endDate) throws IOException, URISyntaxException, ParseException {

		JSONObject data = adminProcessor.loadPublishedInvoiceHistory(sessionID, invoiceID, start, length, fromDate,
				endDate);
		List<Object> jsonList = new LinkedList<Object>();
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
			JSONObject payload = data.getJSONObject("payload");
			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				Map<String, Object> jsonData = new HashMap<String, Object>();

				String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = format.parse(bDate);
				String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

				if (payload.getJSONArray("body").getJSONObject(i).has("paymentDate")) {
					String pDate = payload.getJSONArray("body").getJSONObject(i).getString("paymentDate");
					SimpleDateFormat pformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					format.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date datePayment = pformat.parse(pDate);
					String finalPDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datePayment);
					jsonData.put("paymentDate", finalPDate);
				} else {
					jsonData.put("paymentDate", "<span class='right badge badge-warning'>N/A</span>");
				}

				jsonData.put("date", finalDate);
				jsonData.put("amount",
						Utils.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount")));

				if (payload.getJSONArray("body").getJSONObject(i).getString("status").equalsIgnoreCase("PAID")) {
					jsonData.put("status", "<span class='right badge badge-success'>PAID</span>");
				} else {
					jsonData.put("status", "<span class='right badge badge-danger'>UNPAID</span>");
				}
				jsonList.add(jsonData);
			}
			if (jsonList.size() == 0) {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);
			} else {
				jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
				jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
			}
		} else {
			jsonMap.put("recordsTotal", 0);
			jsonMap.put("recordsFiltered", 0);
		}

		jsonMap.put("data", jsonList);
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@RequestMapping(value = "/viewInvoice", method = { RequestMethod.POST, RequestMethod.GET })
	public ModelAndView viewInvoice(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "invoiceID") String invoiceID, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			JSONObject invoice = adminProcessor.loadInvoiceByID(sessionID, invoiceID);
			String billerName = memberInfo.getJSONObject("payload").getString("name");
			String billerEmail = memberInfo.getJSONObject("payload").getString("email");
			String billerAddress = memberInfo.getJSONObject("payload").getString("address");

			String billerMsisdn = memberInfo.getJSONObject("payload").getString("msisdn");

			if (billerMsisdn.substring(0, 2).equalsIgnoreCase("62")) {
				billerMsisdn = "(+62) " + billerMsisdn.substring(2);
			} else if (billerMsisdn.substring(0, 1).equalsIgnoreCase("0")) {
				billerMsisdn = "(+62) " + billerMsisdn.substring(1);
			}

			String memberName = invoice.getJSONObject("payload").getJSONObject("member").getString("name");
			String memberEmail = invoice.getJSONObject("payload").getJSONObject("member").getString("email");
			String memberAddress = invoice.getJSONObject("payload").getJSONObject("member").getString("address");

			String memberMsisdn = invoice.getJSONObject("payload").getJSONObject("member").getString("msisdn");

			if (memberMsisdn.substring(0, 2).equalsIgnoreCase("62")) {
				memberMsisdn = "(+62) " + memberMsisdn.substring(2);
			} else if (memberMsisdn.substring(0, 1).equalsIgnoreCase("0")) {
				memberMsisdn = "(+62) " + memberMsisdn.substring(1);
			}

			String invoiceNo = invoice.getJSONObject("payload").getJSONObject("publishInvoice")
					.getString("invoiceNumber");
			String paymentCode = invoice.getJSONObject("payload").getJSONObject("publishInvoice")
					.getString("paymentCode");
			Double amount = invoice.getJSONObject("payload").getJSONObject("publishInvoice").getDouble("amount");

			String status = invoice.getJSONObject("payload").getJSONObject("publishInvoice").getString("status");
			Integer paymentDue = invoice.getJSONObject("payload").getJSONObject("billing").getInt("billingCycle");
			Integer billingID = invoice.getJSONObject("payload").getJSONObject("billing").getInt("id");

			String desc = invoice.getJSONObject("payload").getString("description");
			JSONObject jsonDesc = new JSONObject(desc);
			JSONArray items = jsonDesc.getJSONArray("items");

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < items.length(); i++) {
				sb.append(
						"<tr><td>" + (i + 1) + ".</td><td>" + items.getJSONObject(i).getString("item") + "</td><td>Rp. "
								+ Utils.formatAmount(Integer.parseInt(items.getJSONObject(i).getString("amount")))
								+ ",-</td></tr>");
			}

			Map<String, String> menus = adminProcessor.getMenu(sessionID, "invoice");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", billerName);
			model.addAttribute("email", billerEmail);
			model.addAttribute("address", billerAddress);
			model.addAttribute("msisdn", billerMsisdn);
			model.addAttribute("memberName", memberName);
			model.addAttribute("memberEmail", memberEmail);
			model.addAttribute("memberAddress", memberAddress);
			model.addAttribute("memberMsisdn", memberMsisdn);
			model.addAttribute("billingID", billingID);
			model.addAttribute("invoiceID", invoiceID);
			model.addAttribute("invoiceNo", invoiceNo);
			model.addAttribute("paymentCode", paymentCode);
			model.addAttribute("paymentDue", paymentDue + "/" + Utils.getCurrentMonth() + "/" + Utils.getCurrentYear());
			model.addAttribute("table", sb.toString());
			model.addAttribute("amount", Utils.formatAmount(amount.intValue()));

			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);

			return new ModelAndView("viewInvoice");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/invoicePrint", method = RequestMethod.GET)
	public ModelAndView invoicePrint(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "invoiceID") String invoiceID, HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
		return new ModelAndView("invoicePrint");
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
					jsonData.put("amount",
							Utils.formatAmount((int) payload.getJSONObject("payload").getDouble("amount")));

					String pDate = payload.getJSONObject("payload").getJSONObject("publishInvoice")
							.getString("createdDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					format.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date date = format.parse(pDate);
					// date.
					String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

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
				jsonData.put("amount",
						Utils.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount")));

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

			String item1 = "";
			String amount1 = "";
			String item2 = "";
			String amount2 = "";
			String item3 = "";
			String amount3 = "";
			String item4 = "";
			String amount4 = "";
			JSONObject billingInfo = adminProcessor.loadBillingByID(sessionID, billingID);
			String billingName = billingInfo.getJSONObject("payload").getString("name");
			JSONObject defaultItem = adminProcessor.loadBillingItem(sessionID, billingID);
			Integer sum = 0;
			List<Integer> amounts = new LinkedList<Integer>();
			if (defaultItem != null && defaultItem.has("items")) {
				JSONArray items = defaultItem.getJSONArray("items");
				for (int i = 0; i < items.length(); i++) {
					if (i == 0) {
						item1 = items.getJSONObject(i).getString("item");
						amount1 = items.getJSONObject(i).getString("amount");
						amounts.add(Integer.parseInt(amount1));
					} else if (i == 1) {
						item2 = items.getJSONObject(i).getString("item");
						amount2 = items.getJSONObject(i).getString("amount");
						amounts.add(Integer.parseInt(amount2));
					} else if (i == 2) {
						item3 = items.getJSONObject(i).getString("item");
						amount3 = items.getJSONObject(i).getString("amount");
						amounts.add(Integer.parseInt(amount3));
					} else {
						item4 = items.getJSONObject(i).getString("item");
						amount4 = items.getJSONObject(i).getString("amount");
						amounts.add(Integer.parseInt(amount4));
					}
				}
				sum = amounts.stream().reduce(0, (a, b) -> a + b);
			}

			model.addAttribute("item1", item1);
			model.addAttribute("amount1", amount1);
			model.addAttribute("item2", item2);
			model.addAttribute("amount2", amount2);
			model.addAttribute("item3", item3);
			model.addAttribute("amount3", amount3);
			model.addAttribute("item4", item4);
			model.addAttribute("amount4", amount4);
			model.addAttribute("totalAmount", String.format("%,d", sum).replace(',', '.'));

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "billing");
			model.addAttribute("billingID", billingID);
			model.addAttribute("billingName", billingName);
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
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
			@RequestParam(value = "saveItem", required = false) String save, HttpServletResponse response,
			Model model) {
		try {

			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}
			List<Integer> amounts = new LinkedList<Integer>();
			List<Map<String, String>> litem = new LinkedList<Map<String, String>>();

			for (int i = 0; i < item.length; i++) {
				if (item[i] != null && !item[i].isEmpty()) {
					if (amount[i] != null && !amount[i].isEmpty()) {
						Map<String, String> mi = new HashMap<String, String>();
						mi.put("item", item[i]);
						mi.put("amount", amount[i]);
						litem.add(mi);
						amounts.add(Integer.parseInt(amount[i]));
					}
				}
			}

			JSONObject jdata = new JSONObject();
			jdata.put("items", litem);
			Integer sum = amounts.stream().reduce(0, (a, b) -> a + b);
			JSONObject jo = new JSONObject();
			jo.put("billingID", billingID);
			jo.put("amount", new BigDecimal(sum));
			jo.put("members", memberID);
			jo.put("description", jdata);
			adminProcessor.createInvoice(sessionID, jo.toString());

			if (save.equalsIgnoreCase("on") && save != null) {
				adminProcessor.updateBillingItem(sessionID, jdata.toString(), billingID);
			}

			model.addAttribute("message", "OK");
			model.addAttribute("status", "PROCESSED");
			return new ModelAndView("redirect:/submitInvoiceResult");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/submitInvoiceResult", method = { RequestMethod.GET })
	public ModelAndView submitInvoiceResult(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "status") String status, @RequestParam(value = "message") String message,
			HttpServletResponse response, Model model) throws MalformedURLException, IOException {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			if (status.equalsIgnoreCase("PROCESSED")) {
				model.addAttribute("title", "Invoice submission completed");
				model.addAttribute("message", "New invoice has been succesfully submited");
				model.addAttribute("notification", "success");
			} else {
				model.addAttribute("title", "Invoice submission failed");
				model.addAttribute("message", "New invoice submission failed");
				model.addAttribute("notification", "danger");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "invoice");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			return new ModelAndView("invoice");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@RequestMapping(value = "/message", method = { RequestMethod.GET })
	public ModelAndView message(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			HttpServletResponse response, Model model) throws MalformedURLException, IOException {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "message");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
			return new ModelAndView("message");
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("status", "FAILED");
			model.addAttribute("message",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
	}

	@ResponseBody
	@RequestMapping(value = "/messageData", method = RequestMethod.GET)
	public String messageData(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "search[value]") String search)
			throws IOException, URISyntaxException, ParseException {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		List<Object> jsonList = new LinkedList<Object>();

		JSONObject data = adminProcessor.loadMessage(String.valueOf(start), String.valueOf(length), sessionID);
		JSONObject payload = data.getJSONObject("payload");

		for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
			List<Object> jsonListData = new LinkedList<Object>();
			jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("id"));
			jsonListData
					.add(payload.getJSONArray("body").getJSONObject(i).getJSONObject("fromMember").getString("name"));

			String body = payload.getJSONArray("body").getJSONObject(i).getString("body");

			if (body.length() > 100) {
				body = body.substring(0, 100) + "...";
			}
			jsonListData.add(payload.getJSONArray("body").getJSONObject(i).get("subject"));
			jsonListData.add(body);
			String bDate = payload.getJSONArray("body").getJSONObject(i).getString("createdDate");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = format.parse(bDate);
			String timeAgo = TimeAgo.toRelative(date, new Date(), 1);
			jsonListData.add(timeAgo);
			jsonList.add(jsonListData);
		}
		jsonMap.put("data", jsonList);
		jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
		jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(jsonMap);
		return jsonStr;
	}

	@RequestMapping(value = "/transactionHistory", method = RequestMethod.GET)
	public ModelAndView transactionHistory(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID, String invoiceID,
			HttpServletResponse response, Model model) {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return new ModelAndView("redirect:/login");
			}

			String balance = adminProcessor.loadBalanceInquiry(sessionID, "2").getJSONObject("payload")
					.getString("formattedBalance");
			model.addAttribute("balance", balance);
			JSONObject messages = adminProcessor.loadMessage("0", "5", sessionID);
			Integer unread = messages.getJSONObject("payload").getInt("unreadMessage");
			JSONObject memberInfo = adminProcessor.memberInfo(sessionID);
			String memberName = memberInfo.getJSONObject("payload").getString("name");
			Map<String, String> menus = adminProcessor.getMenu(sessionID, "transactionHistory");
			String menu = menus.get("menu");
			String welcomeMenu = menus.get("welcomeMenu");
			model.addAttribute("name", memberName);
			model.addAttribute("menu", menu);
			model.addAttribute("welcomeMenu", welcomeMenu);
			model.addAttribute("unread", unread);
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return new ModelAndView("page_exception");
		}
		return new ModelAndView("transactionHistory");
	}

	@ResponseBody
	@RequestMapping(value = "/transactionHistoryData", method = RequestMethod.GET)
	public String trxHistoryData(
			@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String sessionID,
			@RequestParam(value = "start") Integer start, @RequestParam(value = "length") Integer length,
			@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate)
			throws IOException, URISyntaxException, ParseException {
		try {
			IMap<String, String> memberMap = instance.getMap("Member");
			String member = memberMap.get(sessionID);
			if (member == null) {
				return null;
			}

			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<Object> jsonList = new LinkedList<Object>();
			JSONObject data = adminProcessor.loadTransactionHistory(sessionID, startDate, endDate, start, length);

			if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
				JSONObject payload = data.getJSONObject("payload");
				for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
					Map<String, Object> jsonData = new HashMap<String, Object>();
					String bDate = payload.getJSONArray("body").getJSONObject(i).getString("transactionDate");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
					format.setTimeZone(TimeZone.getTimeZone("GMT"));
					Date date = format.parse(bDate);
					String finalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
					jsonData.put("date", finalDate);

					String fromMember = payload.getJSONArray("body").getJSONObject(i).getJSONObject("fromMember")
							.getString("name");
					if (fromMember.equalsIgnoreCase(memberMap.get(sessionID))) {
						jsonData.put("member", payload.getJSONArray("body").getJSONObject(i).getJSONObject("toMember")
								.getString("name"));
					} else {
						jsonData.put("member", fromMember);
					}

					jsonData.put("transaction", payload.getJSONArray("body").getJSONObject(i)
							.getJSONObject("transferType").getString("name"));

					jsonData.put("amount", payload.getJSONArray("body").getJSONObject(i).getDouble("amount"));
					jsonData.put("description", payload.getJSONArray("body").getJSONObject(i).getString("description"));
					jsonData.put("traceNo", payload.getJSONArray("body").getJSONObject(i).getString("traceNo"));
					jsonData.put("transactionNumber",
							payload.getJSONArray("body").getJSONObject(i).getString("transactionNumber"));
					jsonList.add(jsonData);
				}

				jsonMap.put("recordsTotal", payload.getInt("totalRecord"));
				jsonMap.put("recordsFiltered", payload.getInt("totalRecord"));
			} else {
				jsonMap.put("recordsTotal", 0);
				jsonMap.put("recordsFiltered", 0);
			}
			jsonMap.put("data", jsonList);
			ObjectMapper Obj = new ObjectMapper();
			String jsonStr = Obj.writeValueAsString(jsonMap);
			return jsonStr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String loadTrxHistoryTable(String sessionID, String billerUsername, String startDate, String endDate,
			int start, int length) throws ParseException, MalformedURLException, IOException, URISyntaxException {
		JSONObject data = adminProcessor.loadTransactionHistory(sessionID, startDate, endDate, start, length);
		StringBuffer sb = new StringBuffer();
		if (data.getString("status").equalsIgnoreCase("PROCESSED")) {
			JSONObject payload = data.getJSONObject("payload");
			for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
				Map<String, Object> jsonData = new HashMap<String, Object>();
				sb.append("<tr>");
				String bDate = payload.getJSONArray("body").getJSONObject(i).getString("transactionDate");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
				format.setTimeZone(TimeZone.getTimeZone("GMT"));
				Date date = format.parse(bDate);
				String finalDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
				jsonData.put("date", finalDate);

				sb.append("<td>" + finalDate + "</td>");
				String fromMember = payload.getJSONArray("body").getJSONObject(i).getJSONObject("fromMember")
						.getString("name");
				if (fromMember.equalsIgnoreCase(billerUsername)) {
					sb.append("<td>"
							+ payload.getJSONArray("body").getJSONObject(i).getJSONObject("toMember").getString("name")
							+ "</td>");
				} else {
					sb.append("<td>" + fromMember + "</td>");
				}
				sb.append("<td>"
						+ payload.getJSONArray("body").getJSONObject(i).getJSONObject("transferType").getString("name")
						+ "</td>");
				sb.append("<td>"
						+ Utils.formatAmount((int) payload.getJSONArray("body").getJSONObject(i).getDouble("amount"))
						+ "</td>");
				sb.append("</tr>");
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	public String loadBillingTable(String sessionID, String billerUsername, String startDate, String endDate, int start,
			int length) throws ParseException, MalformedURLException, IOException, URISyntaxException {
		JSONObject data = adminProcessor.loadBilling(sessionID, String.valueOf(start), String.valueOf(length));
		JSONObject payload = data.getJSONObject("payload");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < payload.getJSONArray("body").length(); i++) {
			sb.append("<tr>");
			JSONObject dataStat = adminProcessor.loadInvoiceStat(sessionID,
					String.valueOf(payload.getJSONArray("body").getJSONObject(i).getInt("id")),
					Utils.getCurrentMonthFirstDate(), Utils.getCurrentMonthLastDate());
			sb.append("<td>" + payload.getJSONArray("body").getJSONObject(i).get("name") + "</td>");
			sb.append("<td>" + payload.getJSONArray("body").getJSONObject(i).get("description") + "</td>");

			if (dataStat.has("payload")) {
				double stat = dataStat.getJSONObject("payload").getDouble("paidPercentage");
				if (stat >= 0 && stat <= 30.0) {
					sb.append(
							"<td><div class='progress progress-xs progress-striped active'><div class='progress-bar bg-danger' style='width:"
									+ +stat + "%'></div></div></td>");
					sb.append("<td><span class='right badge badge-danger'>" + stat + "%</span></td>");
				} else if (stat >= 31 && stat <= 50.0) {
					sb.append(
							"<td><div class='progress progress-xs progress-striped active'><div class='progress-bar bg-warning' style='width:"
									+ +stat + "%'></div></div></td>");
					sb.append("<td><span class='right badge badge-warning'>" + stat + "%</span></td>");
				} else {
					sb.append(
							"<td><div class='progress progress-xs progress-striped active'><div class='progress-bar bg-success' style='width:"
									+ +stat + "%'></div></div></td>");
					sb.append("<td><span class='right badge badge-success'>" + stat + "%</span></td>");
				}
				sb.append("</tr>");
			} else {
				sb.append(
						"<td><div class='progress progress-xs progress-striped active'><div class='progress-bar bg-danger' style='width:"
								+ 0 + "%'></div></div></td>");
				sb.append("<td><span class='right badge badge-danger'>0%</span></td>");
				sb.append("</tr>");
			}
		}
		return sb.toString();
	}
}
