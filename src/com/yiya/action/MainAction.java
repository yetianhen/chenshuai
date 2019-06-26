package com.yiya.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.yiya.annotation.Auth;
import com.yiya.bean.SysMenu;
import com.yiya.bean.SysMenuBtn;
import com.yiya.bean.SysUser;
import com.yiya.bean.TreeNode;
import com.yiya.bean.BaseBean.DELETED;
import com.yiya.bean.BaseBean.STATE;
import com.yiya.model.SiteMainModel;
import com.yiya.service.SysMenuBtnService;
import com.yiya.service.SysMenuService;
import com.yiya.service.SysUserService;
import com.yiya.utils.DateUtil;
import com.yiya.utils.HtmlUtil;
import com.yiya.utils.MethodUtil;
import com.yiya.utils.SessionUtils;
import com.yiya.utils.TreeUtil;
import com.yiya.utils.URLUtils;
import com.yiya.utils.Constant.SuperAdmin;

@Controller
@RequestMapping("")
public class MainAction extends BaseAction {

	
	private final static Logger log= Logger.getLogger(MainAction.class);
	// Servrice start
	@Autowired(required=false) 
	private SysMenuService<SysMenu> sysMenuService; 
	
	@Autowired(required=false) 
	private SysUserService<SysUser> sysUserService; 
	
	@Autowired(required=false) 
	private SysMenuBtnService<SysMenuBtn> sysMenuBtnService;
	
	/**
	 * 鐧诲綍椤甸潰
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("login")
	public ModelAndView  login(HttpServletRequest request,HttpServletResponse response) throws Exception{
		log.info("chenshuai");
		Map<String,Object>  context = getRootMap();
		
		return forword("login", context);
	}
	
	/**
	 * 注册控制器
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("regester")
	public ModelAndView  regester(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object>  context = getRootMap();
		
		return forword("regester", context);
	}
	
	/**
	 * 注册控制器
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("email")
	public String  email(String email,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
        String mes="true";
        log.info("email:"+email);
        int count=sysUserService.getUserCountByEmail(email);
        log.info("count:"+count);
        //判断用户名是否正确，正确则登录，错误则提示重新输入  
        if(count>0){  
            response.setContentType("text/plain;charset=UTF-8"); 
            log.info("msg:"+mes);
            response.getWriter().write(mes);  
        }else{  
            mes="false";  
            //设置字符集  
            response.setContentType("text/plain;charset=UTF-8");  
            response.getWriter().write(mes);  
        }  
        return null; 
	}
	
	
	/**
	 * 鐧诲綍
	 * @param email 閭鐧诲綍璐﹀彿
	 * @param pwd 瀵嗙爜
	 * @param verifyCode 楠岃瘉鐮�
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/toLogin")
	public void  toLogin(String email,String pwd,String verifyCode,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String vcode  = SessionUtils.getValidateCode(request);
		SessionUtils.removeValidateCode(request);//娓呴櫎楠岃瘉鐮侊紝纭繚楠岃瘉鐮佸彧鑳界敤涓�娆�
		if(StringUtils.isBlank(verifyCode)){
			sendFailureMessage(response, "楠岃瘉鐮佷笉鑳戒负绌�.");
			return;
		}
		//鍒ゆ柇楠岃瘉鐮佹槸鍚︽纭�
		if(!verifyCode.toLowerCase().equals(vcode)){
			sendFailureMessage(response, "楠岃瘉鐮佽緭鍏ラ敊璇�.");
			return;
		}
		if(StringUtils.isBlank(email)){
			sendFailureMessage(response, "璐﹀彿涓嶈兘涓虹┖.");
			return;
		}
		if(StringUtils.isBlank(pwd)){
			sendFailureMessage(response, "瀵嗙爜涓嶈兘涓虹┖.");
			return;
		}
		String msg = "鐢ㄦ埛鐧诲綍鏃ュ織:";
		SysUser user = sysUserService.queryLogin(email, MethodUtil.MD5(pwd));
		if(user == null){
			//璁板綍閿欒鐧诲綍鏃ュ織
			log.debug(msg+"["+email+"]"+"璐﹀彿鎴栬�呭瘑鐮佽緭鍏ラ敊璇�.");
			sendFailureMessage(response, "璐﹀彿鎴栬�呭瘑鐮佽緭鍏ラ敊璇�.");
			return;
		}
		if(STATE.DISABLE.key == user.getState()){
			sendFailureMessage(response, "璐﹀彿宸茶绂佺敤.");
			return;
		}
		//鐧诲綍娆℃暟鍔�1 淇敼鐧诲綍鏃堕棿
		int loginCount = 0;
		if(user.getLoginCount() != null){
			loginCount = user.getLoginCount();
		}
		user.setLoginCount(loginCount+1);
		user.setLoginTime(DateUtil.getDateByString(""));
		sysUserService.update(user);
		//璁剧疆User鍒癝ession
		SessionUtils.setUser(request,user);
		//璁板綍鎴愬姛鐧诲綍鏃ュ織
		log.debug(msg+"["+email+"]"+"鐧诲綍鎴愬姛");
		sendSuccessMessage(response, "鐧诲綍鎴愬姛.");
	}
	
	/**
	 * 鐧诲綍
	 * @param email 閭鐧诲綍璐﹀彿
	 * @param pwd 瀵嗙爜
	 * @param verifyCode 楠岃瘉鐮�
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/toRegester")
	public void  toRegester(String email,String pwd,String verifyCode,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String vcode  = SessionUtils.getValidateCode(request);
		SessionUtils.removeValidateCode(request);//娓呴櫎楠岃瘉鐮侊紝纭繚楠岃瘉鐮佸彧鑳界敤涓�娆�
		if(StringUtils.isBlank(verifyCode)){
			sendFailureMessage(response, "楠岃瘉鐮佷笉鑳戒负绌�.");
			return;
		}
		//鍒ゆ柇楠岃瘉鐮佹槸鍚︽纭�
		if(!verifyCode.toLowerCase().equals(vcode)){
			sendFailureMessage(response, "楠岃瘉鐮佽緭鍏ラ敊璇�.");
			return;
		}
		if(StringUtils.isBlank(email)){
			sendFailureMessage(response, "璐﹀彿涓嶈兘涓虹┖.");
			return;
		}
		if(StringUtils.isBlank(pwd)){
			sendFailureMessage(response, "瀵嗙爜涓嶈兘涓虹┖.");
			return;
		}
		String msg = "鐢ㄦ埛鐧诲綍鏃ュ織:";
		log.info("test");
		sysUserService.insertUser(email, MethodUtil.MD5(pwd));
		sendSuccessMessage(response, "注册成功.");
	}
	
	/**
	 * 閫�鍑虹櫥褰�
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyLogin=false,verifyURL=false)
	@RequestMapping("/logout")
	public void  logout(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SessionUtils.removeUser(request);
		response.sendRedirect(URLUtils.get("msUrl")+"/login.shtml");
	}
	
	/**
	 * 鑾峰彇Action涓嬬殑鎸夐挳
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/getActionBtn")
	public void  getActionBtn(String url,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> actionTypes = new ArrayList<String>();
		//鍒ゆ柇鏄惁瓒呯骇绠＄悊鍛�
		if(SessionUtils.isAdmin(request)){
			result.put("allType", true);
		}else{
			String menuUrl = URLUtils.getReqUri(url);
			menuUrl = StringUtils.remove(menuUrl,request.getContextPath());
			//鑾峰彇鏉冮檺鎸夐挳
			actionTypes = SessionUtils.getMemuBtnListVal(request, StringUtils.trim(menuUrl));
			result.put("allType", false);
			result.put("types", actionTypes);
		}
		result.put(SUCCESS, true);
		HtmlUtil.writerJson(response, result);
	}
	 
	
	/**
	 * 淇敼瀵嗙爜
	 * @param url
	 * @param classifyId
	 * @return
	 * @throws Exception 
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/modifyPwd")
	public void modifyPwd(String oldPwd,String newPwd,HttpServletRequest request,HttpServletResponse response) throws Exception{
		SysUser user = SessionUtils.getUser(request);
		if(user == null){
			sendFailureMessage(response, "瀵逛笉璧�,鐧诲綍瓒呮椂.");
			return;
		}
		SysUser bean  = sysUserService.queryById(user.getId());
		if(bean.getId() == null || DELETED.YES.key == bean.getDeleted()){
			sendFailureMessage(response, "瀵逛笉璧�,鐢ㄦ埛涓嶅瓨鍦�.");
			return;
		}
		if(StringUtils.isBlank(newPwd)){
			sendFailureMessage(response, "瀵嗙爜涓嶈兘涓虹┖.");
			return;
		}
		//涓嶆槸瓒呯骇绠＄悊鍛橈紝鍖归厤鏃у瘑鐮�
		if(!MethodUtil.ecompareMD5(oldPwd,bean.getPwd())){
			sendFailureMessage(response, "鏃у瘑鐮佽緭鍏ヤ笉鍖归厤.");
			return;
		}
		bean.setPwd(MethodUtil.MD5(newPwd));
		sysUserService.update(bean);
		sendSuccessMessage(response, "Save success.");
	}
	
	/**
	 * ilook 棣栭〉
	 * @param url
	 * @param classifyId
	 * @return
	 */
	@Auth(verifyURL=false)
	@RequestMapping("/main") 
	public ModelAndView  main(SiteMainModel model,HttpServletRequest request){
		Map<String,Object>  context = getRootMap();
		SysUser user = SessionUtils.getUser(request);
		List<SysMenu> rootMenus = null;
		List<SysMenu> childMenus = null;
		List<SysMenuBtn> childBtns = null;
		//瓒呯骇绠＄悊鍛�
		if(user != null && SuperAdmin.YES.key ==  user.getSuperAdmin()){
			rootMenus = sysMenuService.getRootMenu(null);// 鏌ヨ鎵�鏈夋牴鑺傜偣
			childMenus = sysMenuService.getChildMenu();//鏌ヨ鎵�鏈夊瓙鑺傜偣
		}else{
			rootMenus = sysMenuService.getRootMenuByUser(user.getId() );//鏍硅妭鐐�
			childMenus = sysMenuService.getChildMenuByUser(user.getId());//瀛愯妭鐐�
			childBtns = sysMenuBtnService.getMenuBtnByUser(user.getId());//鎸夐挳鎿嶄綔
			buildData(childMenus,childBtns,request); //鏋勫缓蹇呰鐨勬暟鎹�
		}
		context.put("user", user);
		context.put("menuList", treeMenu(rootMenus,childMenus));
		return forword("main/main",context); 
	}
	
	/**
	 * 鏋勫缓鏍戝舰鏁版嵁
	 * @return
	 */
	private List<TreeNode> treeMenu(List<SysMenu> rootMenus,List<SysMenu> childMenus){
		TreeUtil util = new TreeUtil(rootMenus,childMenus);
		return util.getTreeNode();
	}
	
	
	/**
	 * 鏋勫缓鏍戝舰鏁版嵁
	 * @return
	 */
	private void buildData(List<SysMenu> childMenus,List<SysMenuBtn> childBtns,HttpServletRequest request){
		//鑳藉璁块棶鐨剈rl鍒楄〃
		List<String> accessUrls  = new ArrayList<String>();
		//鑿滃崟瀵瑰簲鐨勬寜閽�
		Map<String,List> menuBtnMap = new HashMap<String,List>(); 
		for(SysMenu menu: childMenus){
			//鍒ゆ柇URL鏄惁涓虹┖
			if(StringUtils.isNotBlank(menu.getUrl())){
				List<String> btnTypes = new ArrayList<String>();
				for(SysMenuBtn btn  : childBtns){
					if(menu.getId().equals(btn.getMenuid())){
						btnTypes.add(btn.getBtnType());
						URLUtils.getBtnAccessUrls(menu.getUrl(), btn.getActionUrls(),accessUrls);
					}
				}
				menuBtnMap.put(menu.getUrl(), btnTypes);
				URLUtils.getBtnAccessUrls(menu.getUrl(), menu.getActions(),accessUrls);
				accessUrls.add(menu.getUrl());
			}
		}
		SessionUtils.setAccessUrl(request, accessUrls);//璁剧疆鍙闂殑URL
		SessionUtils.setMemuBtnMap(request, menuBtnMap); //璁剧疆鍙敤鐨勬寜閽�
	}
}
