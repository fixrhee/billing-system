package org.billing.admin.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.billing.admin.data.Login;
import org.billing.admin.processor.AdminProcessor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

@Controller
@RequestMapping("/")
public class LoginController {

	@Autowired
	private AdminProcessor adminProcessor;
	@Autowired
	private HazelcastInstance instance;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model) {
		// model.addAttribute("key", contextLoader.getSiteKey());
		// model.addAttribute("login", new Login());
		return "login";
	}

	@RequestMapping(value = "/submitLogin", method = RequestMethod.POST)
	public String submitLogin(@Valid @ModelAttribute("login") Login login, BindingResult result,
			HttpServletResponse response, Model model) {
		try {
			if (result.hasErrors()) {
				model.addAttribute("httpResponseCode", "500");
				model.addAttribute("status", "Oops !");
				model.addAttribute("description",
						"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
				return "page_exception";
			}

			IMap<String, String> memberMap = instance.getMap("Member");

			JSONObject res = adminProcessor.submitLogin(login);
			if (res.getString("status").equalsIgnoreCase("PROCESSED")) {
				String sessionID = res.getJSONObject("payload").getString("token");
				Cookie cookie = new Cookie("SessionID", sessionID);
				memberMap.put(sessionID, login.getUsername());
				cookie.setMaxAge(3600);
				response.addCookie(cookie);
				return "loginSuccess";
			} else {
				model.addAttribute("status", "Invalid Username/Password");
				return "login";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("httpResponseCode", "500");
			model.addAttribute("status", "Oops !");
			model.addAttribute("description",
					"We are experiencing some trouble here, but don't worry our team are OTW to solve this");
			return "page_exception";
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String login(@CookieValue(value = "SessionID", defaultValue = "defaultCookieValue") String cookieValue,
			HttpServletResponse response, Model model) {
		Cookie cookie = new Cookie("SessionID", "");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		return "login";
	}

}
