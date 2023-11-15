package com.sideProject.publicRestRoomInJeju.controller;

import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sideProject.publicRestRoomInJeju.service.ApiService;
import com.sideProject.publicRestRoomInJeju.vo.ToiletInfoVo;

@Controller
public class ApiController {

	@Autowired
	private ApiService apiService;
	
	
	// getToiletList() 값을 반환하는 매핑
	@ResponseBody
	@GetMapping("/toilet")
	public List<ToiletInfoVo> mapData(Model model) throws IOException, ParseException {
		return apiService.getToiletList();
	}
	
	
	// 화면을 그리는 매핑
	@GetMapping("/")
	public String showMap(Model model) {
		
		try {
			
			// 서비스에서 데이터를 가져옴
			List<ToiletInfoVo> resultToiletList = apiService.getToiletList();
			
			// 데이터를 모델에 추가해서 뷰에 전달
			model.addAttribute("toiletList", resultToiletList);
			
		}catch(IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return "sheIndex";
		
	}
	
}
