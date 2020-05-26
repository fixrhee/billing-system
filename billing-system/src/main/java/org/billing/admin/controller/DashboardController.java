package org.billing.admin.controller;

import javax.servlet.http.HttpServletResponse;

import org.billing.admin.processor.AdminProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
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

			model.addAttribute("menu", "dashboard");
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
}
