package com.beyondid.scimConnector.jfgcp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public interface SCIMController {

	@GetMapping(value = "/Users")
	public @ResponseBody Map getUsers(@RequestParam Map<String, String> pagination, HttpServletResponse httpResponse) throws IOException;

	@PostMapping(value = "/Users")
    public @ResponseBody Map createUser(@RequestBody Map<String, Object> scimuser, HttpServletResponse response)  throws Exception;

    @PutMapping(value="/Users/{id}")
    public @ResponseBody Map singleUserPut(@RequestBody Map<String, Object> payload, @PathVariable String id) throws Exception;

    /*@RequestMapping(value="/Groups",method = RequestMethod.GET)
    public @ResponseBody Map getGroups(@RequestParam Map<String, String> pagination, HttpServletResponse httpResponse) throws IOException;*/

}
