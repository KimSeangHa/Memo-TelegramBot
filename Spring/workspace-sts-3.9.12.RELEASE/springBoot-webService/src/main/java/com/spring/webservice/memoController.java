package com.spring.webservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.spring.webservice.vo.memoVO;

@RestController
public class memoController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	// �޸� ���
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/addMemo", method = RequestMethod.POST)
	@ResponseBody
	public String addMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		System.out.println("## AddMemo ##");
		System.out.println(memoVO.getId());
			
		int rst = sqlSession.insert("memo.addMemo", memoVO);
		String result = "";
		
		if ( rst > 0) {
			result = "0";
		} else {
			result = "1";
		}
		
		return result;
	}
	
	// �޸� ����
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/deleteMemo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		System.out.println("## DeleteMemo ## ");
		System.out.println(memoVO.getId());
			
		int rst = sqlSession.delete("memo.deleteMemo", memoVO);
		String result = "";
		
		if ( rst > 0) {
			result = "0";
		} else {
			result = "1";
		}
		
		return result;
	}
	
	// �޸� ����
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/rewriteMemo", method = RequestMethod.POST)
	@ResponseBody
	public String rewriteMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		System.out.println("## RewriteMemo ## ");
		System.out.println(memoVO.getId());
		System.out.println(memoVO.getModifyDate());
		int rst = sqlSession.update("memo.rewriteMemo", memoVO);
		String result = "";
		
		if ( rst > 0) {
			result = "0";
		} else {
			result = "1";
		}
		
		return result;
	}
	
	// �޸� ����Ʈ ��ȸ
	@CrossOrigin(origins = "http://localhost:8080")
	@RequestMapping(value = "/getMemoList", method = RequestMethod.POST)
	@ResponseBody
	public List<memoVO> getListMemo(Locale locale, Model model) {
		System.out.println("## getListMemo ## ");
		
		List<memoVO> getMemoList = sqlSession.selectList("memo.getMemoList");
		ArrayList<String> errorResult = new ArrayList<String>();
		
		if (getMemoList == null) {
			errorResult.add("ERROR");
		}
		
		return getMemoList;
	}
}