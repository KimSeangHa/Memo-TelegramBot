package com.spring.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.webservice.utill.MakeToken;
import com.spring.webservice.vo.loginVO;

@RestController
public class LoginController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	// 회원 가입
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/InsertMember", method = RequestMethod.POST)
	@ResponseBody
	public String InsertMember(Locale locale, Model model, @RequestBody loginVO loginVO, HttpServletRequest request) {
		System.out.println("## InsertMember ##");
		System.out.println(loginVO.getMember_id());
		System.out.println(loginVO.getMember_name());
		
		
		int rst = sqlSession.insert("login.InsertMember", loginVO);
		String result = "";
		
		if ( rst > 0) {
			result = "0";
		} else {
			result = "1";
		}
	
		return result;
	}
	
	// ID 중복체크
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/MemberChk", method = RequestMethod.POST)
	@ResponseBody
	public String MemberChk(Locale locale, Model model, @RequestBody loginVO loginVO, HttpServletRequest request) {
		System.out.println("## MemberChk ##");
		System.out.println(loginVO.getMember_id());
		
		int rst = sqlSession.selectOne("login.MemberChk", loginVO);
		String result = "";
		
		if ( rst == 0) {
			result = "0";
		} else {
			result = "1";
		}
	
		return result;
	}
	
	// 로그인
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/LoginChk", method = RequestMethod.POST)
	@ResponseBody
	public List<String> LoginChk(Locale locale, Model model, @RequestBody loginVO loginVO, HttpServletRequest request) {
		System.out.println("## LoginChk ##");
		System.out.println(loginVO.getMember_id());
		
		HttpSession session = request.getSession();

		// MEMBER 정보 조회
		// 없으면 LIST RESULT = 1로 반환
		int rst = sqlSession.selectOne("login.loginChk", loginVO);
		ArrayList<String> LoginResultInfo = new ArrayList<String>();
		
		// 있으면 AccessKey (TOKEN) 생성
		// 세션 등록
		// AccessKey(TOKEN), RESULT = 0 으로 반환
		String result = "";
		String token = MakeToken.NewToken(100);
		
		if ( rst > 0) {
			result = "0";
			LoginResultInfo.add(result);
			LoginResultInfo.add(token);
			
			loginVO loginInfo = sqlSession.selectOne("login.getLoginInfo", loginVO);
			
			session.setAttribute("MEMBER_NUM", loginInfo.getMember_num());
			session.setAttribute("MemberToken", token);
			
			String aaa = (String) session.getAttribute("MemberToken");
			System.out.println(aaa);
			
		} else {
			result = "1";
			LoginResultInfo.add(result);
		}
	
		return LoginResultInfo;
	}
}
