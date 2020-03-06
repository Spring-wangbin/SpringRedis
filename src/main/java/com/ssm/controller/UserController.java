package com.ssm.controller;

import com.ssm.pojo.User;
import com.ssm.service.IUserService;
import com.ssm.utils.BeanHelper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
//@RequestMapping("/userCURD")
public class UserController {

    @Resource
    private IUserService iUserService;

    /**
     * 查询所有User
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/showUser", method = RequestMethod.GET)
    public String showUsers(Model model){
        System.out.println("**********showUsers********");
        List<User> userList = new ArrayList<User>();
        userList = iUserService.getAllUser();
        model.addAttribute("userList", userList); // 填充数据到model
        System.out.println(userList);
        return "showUser";
    }

    @RequestMapping(value = "/showUser2", method = RequestMethod.GET)
    public String showUsers2(Model model){
        System.out.println("**********showUsers2********");
        List<User> userList = new ArrayList<User>();
        userList = iUserService.getAllUser2();
        model.addAttribute("userList", userList); // 填充数据到model
        System.out.println(userList);
        return "showUser";
    }

    @RequestMapping(value = "/APISTORE_GET", method = RequestMethod.GET)
    @ResponseBody
    public static String APISTORE_GET(String accountNo, String name, String idCard) {

        //appcode查看地址 https://market.console.aliyun.com/imageconsole/
        String appcode = "77acf435622d474bb686e36f11f02125";
        //请求地址
        String strUrl="https://tbank.market.alicloudapi.com/bankCheck";

        String returnStr = null; // 返回结果定义
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        String params = null;
        try {
            params = "accountNo=6214830159118701";
            params += "&name=张渊";
            params += "&idCard=140781198410180133";  //可为空
            url = new URL(strUrl + "?" + params);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET"); // get方式
            httpURLConnection.setUseCaches(false); // 不用缓存
            httpURLConnection.connect();
            System.out.println(httpURLConnection.getResponseCode());
            System.out.println(httpURLConnection.getResponseMessage());
            httpURLConnection.getErrorStream();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
            System.out.println(returnStr);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }

    public static void main(String[] args) {
        APISTORE_GET("","","");
    }

    /**
     * 增加一个用户
     *
     * @param userName
     * @param sex
     * @param age
     * @return
     */
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap addUser(String userName, String sex, int age) {
        System.out.println("******addUser********");
        System.out.println(userName + sex + age);
        User user = new User();
        user.setsex(sex);
        user.setUserName(userName);
        user.setAge(age);
        iUserService.insertUser(user);
        ModelMap model = new ModelMap();
        model.addAttribute("result", "添加成功");
        return model;
    }

    /**
     * 通过userID删除用户
     *
     * @param userID
     */
    @RequestMapping(value = "/delUser/{userID}", method = RequestMethod.GET)
    public ModelAndView delUser(@PathVariable int userID) {
        System.out.println(userID);
        iUserService.deleteUser(userID);
        ModelAndView mv = new ModelAndView();
        List<User> userList = new ArrayList<User>();
        userList = iUserService.getAllUser();
        mv.addObject("userList", userList); // 填充数据到model
        mv.setViewName("showUser");
        return mv;
    }

    /**
     * 查询用户
     *
     * @param model
     * @param keyWords
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String findUsers(Model model, String keyWords) {
        System.out.println(keyWords);
        List<User> userList = new ArrayList<User>();
        userList = iUserService.findUsers(keyWords);
        model.addAttribute("userList", userList); // 填充数据到model
        return "showUser";
    }

    /**
     * 更新用户信息
     * @param userName
     * @param sex
     * @param age
     * @param id
     * @return
     */
    @RequestMapping(value="/editUser",method=RequestMethod.POST)
    public ModelAndView editUser(String userName, String sex, int age, int id) {
        System.out.println(userName + sex + age);
        User user = new User();
        user.setsex(sex);
        user.setUserName(userName);
        user.setAge(age);
        user.setId(id);
        iUserService.editUser(user);
        ModelAndView mv = new ModelAndView();
        List<User> userList = new ArrayList<User>();
        userList = iUserService.getAllUser();
        mv.addObject("userList", userList); // 填充数据到model
        mv.setViewName("redirect:/UserCRUD/showUser");
        return mv;
    }

    @RequestMapping("/refresh")
    @ResponseBody
    public void refresh(HttpServletRequest request){
        XmlWebApplicationContext context =
                (XmlWebApplicationContext) WebApplicationContextUtils
                        .getWebApplicationContext(request.getServletContext());

        /*XmlWebApplicationContext context = (XmlWebApplicationContext) BeanHelper.getApplicationContext();*/

        PooledDataSource dataSource = (PooledDataSource) context.getBean("dataSource");
        dataSource.setPassword("wangbin");
        System.out.println(context);
        context.refresh();
    }
}
