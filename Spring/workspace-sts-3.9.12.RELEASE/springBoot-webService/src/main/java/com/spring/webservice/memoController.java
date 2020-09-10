package com.spring.webservice;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

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

import com.spring.webservice.utill.Telegram;
import com.spring.webservice.utill.TelegramDeleteWebhook;
import com.spring.webservice.utill.TelegramSend;
import com.spring.webservice.vo.botVO;
import com.spring.webservice.vo.loginVO;
import com.spring.webservice.vo.memoVO;

@RestController
public class memoController {
private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SqlSession sqlSession;
	
	// �޸� ���
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/addMemo", method = RequestMethod.POST)
	@ResponseBody
	public String addMemo(Locale locale, Model model, @RequestBody memoVO memoVO, HttpServletRequest request) {
		logger.info("## AddMemo ##");
		logger.info(memoVO.getMember_token());
		
		
		loginVO loginVO = new loginVO();
		loginVO.setMember_token(memoVO.getMember_token());
		int tokenCount = sqlSession.selectOne("login.getTokenCount", loginVO);
		String result = "";
		
		// �α���üũ
		if ( tokenCount > 0 ) {
			loginVO lvo = sqlSession.selectOne("login.getTokenInfo", loginVO);
			
			if (lvo.getMember_token().equals("")) {
				result = "100";
				return result;
			} 
	
			String getToken = lvo.getMember_token();
			if (!loginVO.getMember_token().equals(getToken)) {
				result = "100";
				return result;
			}
			
			memoVO.setMember_num(lvo.getMember_num());
			memoVO.setContent(memoVO.getContent().replace("\n", "%0A"));
			
			int rst = sqlSession.insert("memo.addMemo", memoVO);
			
			// member_num ���� auth_key, chat_id �� ��������
			botVO botVO = new botVO();
			botVO.setMember_num(memoVO.getMember_num());
			botVO bot = sqlSession.selectOne("bot.getBotInfo", botVO);
			
			if ( bot == null ) {
				result = "2";
				return result;
			}
			
			if ( rst > 0) {
				result = "0";
				TelegramSend.SendMessage(memoVO.getContent(), bot.getBot_token(), bot.getChat_id());
			} else {
				result = "1";
			}
		} else {
			result = "100";
		}
		
		return result;
	}
	
	// �޸� ����
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/deleteMemo", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		logger.info("## DeleteMemo ## ");
		logger.info(memoVO.getId());
		logger.info(memoVO.getMember_token());
		
		
		loginVO loginVO = new loginVO();
		loginVO.setMember_token(memoVO.getMember_token());
		int tokenCount = sqlSession.selectOne("login.getTokenCount", loginVO);
		String result = "";
		
		// �α���üũ
		if ( tokenCount > 0 ) {
			loginVO lvo = sqlSession.selectOne("login.getTokenInfo", loginVO);
			
			if (lvo.getMember_token().equals("")) {
				result = "100";
				return result;
			} 
	
			String getToken = lvo.getMember_token();
			if (!loginVO.getMember_token().equals(getToken)) {
				result = "100";
				return result;
			}
			
			memoVO.setMember_num(lvo.getMember_num());
			int rst = sqlSession.delete("memo.deleteMemo", memoVO);
			
			if ( rst > 0) {
				result = "0";
			} else {
				result = "1";
			}
			
		} else {
			result = "100";
		}
		
		return result;
	}
	
	// �޸� ����
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/rewriteMemo", method = RequestMethod.POST)
	@ResponseBody
	public String rewriteMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		logger.info("## RewriteMemo ## ");
		logger.info(memoVO.getId());
		logger.info(memoVO.getModifyDate());
		logger.info(memoVO.getMember_token());
		
		
		loginVO loginVO = new loginVO();
		loginVO.setMember_token(memoVO.getMember_token());
		int tokenCount = sqlSession.selectOne("login.getTokenCount", loginVO);
		String result = "";
		
		// �α���üũ
		if ( tokenCount > 0 ) {
			loginVO lvo = sqlSession.selectOne("login.getTokenInfo", loginVO);
			
			if (lvo.getMember_token().equals("")) {
				result = "100";
				return result;
			} 
	
			String getToken = lvo.getMember_token();
			if (!loginVO.getMember_token().equals(getToken)) {
				result = "100";
				return result;
			}
			
			memoVO.setMember_num(lvo.getMember_num());
			memoVO.setContent(memoVO.getContent().replace("\n", "%0A"));
			int rst = sqlSession.update("memo.rewriteMemo", memoVO);
			
			// member_num ���� auth_key, chat_id �� ��������
			botVO botVO = new botVO();
			botVO.setMember_num(memoVO.getMember_num());
			botVO bot = sqlSession.selectOne("bot.getBotInfo", botVO);
			
			if ( bot == null ) {
				result = "2";
				return result;
			}
			
			if ( rst > 0) {
				result = "0";
				TelegramSend.SendMessage(memoVO.getContent(), bot.getBot_token(), bot.getChat_id());
			} else {
				result = "1";
			}
		} else {
			result = "100";
		}
		return result;
	}
	
	// �޸� ����Ʈ ��ȸ
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getMemoList", method = RequestMethod.POST)
	@ResponseBody
	public List<memoVO> getListMemo(Locale locale, Model model, @RequestBody memoVO memoVO) {
		logger.info("## getListMemo ## ");
		logger.info(memoVO.getMember_token());
		
		
		loginVO loginVO = new loginVO();
		loginVO.setMember_token(memoVO.getMember_token());
		
		if (memoVO.getMember_token().equals("")) {
			return null;
		}
		
		int tokenCount = sqlSession.selectOne("login.getTokenCount", loginVO);
		
		// �α���üũ
		if ( tokenCount > 0 ) {
			loginVO lvo = sqlSession.selectOne("login.getTokenInfo", loginVO);
			
			if (lvo.getMember_token().equals("")) {
				return null;
			} 
	
			String getToken = lvo.getMember_token();
			if (!loginVO.getMember_token().equals(getToken)) {
				return null;
			}
	
			memoVO.setMember_num(lvo.getMember_num());
			List<memoVO> getMemoList = sqlSession.selectList("memo.getMemoList", memoVO);
			
			if (getMemoList == null) {
				return null;
			}
			
			return getMemoList;
			
		} else {
			return null;
		}
	}
	
	// �ڷ��׷� �� ����
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/Makebot", method = RequestMethod.POST)
	@ResponseBody
	public String Makebot(Locale locale, Model model, @RequestBody memoVO memoVO) {
		logger.info("## Make Bot ## ");
		logger.info(memoVO.getMember_token());
		
		loginVO loginVO = new loginVO();
		loginVO.setMember_token(memoVO.getMember_token());
		int tokenCount = sqlSession.selectOne("login.getTokenCount", loginVO);
		String result = "";
		
		// �α���üũ
		if ( tokenCount > 0 ) {
			loginVO lvo = sqlSession.selectOne("login.getTokenInfo", loginVO);
			
			if (lvo.getMember_token().equals("")) {
				result = "100";
				return result;
			} 
	
			String getToken = lvo.getMember_token();
			if (!loginVO.getMember_token().equals(getToken)) {
				result = "100";
				return result;
			}
			
			if (lvo.getMember_level() == 3) {
				Telegram.makeBot();
				TelegramDeleteWebhook.deleteWebhook();
				result = "0";
				return result;
			} else {
				result = "1";
				return result;
			} 
		} else {
			result = "100";
		}
		
		return result;
	}
}
