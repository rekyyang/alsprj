package zju.edu.als.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import zju.edu.als.dao.UserDao;
import zju.edu.als.domain.result.Result;
import zju.edu.als.domain.user.User;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zzq on 2016/12/19.
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserDao userDao;

    @RequestMapping("/login")
    @ResponseBody
    public Result in(@RequestParam(value = "userName", required = true) String userName,
                     @RequestParam(value = "password", required = true) String password, HttpServletResponse response) {
        log.info("username {} ; password  {}",userName,password);
        try {
            List<User> userList = userDao.selectDynamic(userName, password, null);
            if (userList == null || userList.size() == 0) {
                return Result.fail("用户名或者密码错误");
            }
            Cookie roleCookie = new Cookie("role", userList.get(0).getRole());
            roleCookie.setMaxAge(10 * 60);
            roleCookie.setPath("/");
            response.addCookie(roleCookie);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e);
        }

    }

    @RequestMapping("/update")
    @ResponseBody
    public Result update(@RequestParam(value = "userName") String userName,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "role") String role) {
        try {
            userDao.updateUser(userName, password, role);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @RequestMapping("/search")
    @ResponseBody
    public Result search(@RequestParam(value = "userName", required = true) String userName) {
        try {
            List<User> userList = userDao.selectDynamic(userName, null, null);
            return Result.ok(userList);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Result out(HttpServletResponse response) {
        Cookie roleCookie = new Cookie("role", "");
        roleCookie.setMaxAge(0);
        roleCookie.setPath("/");
        response.addCookie(roleCookie);
        return Result.ok();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public Result insert(@RequestParam(value = "userName") String userName,
                         @RequestParam(value = "password") String password,
                         @RequestParam(value = "role") String role) {
        try {
            List<User> userList = userDao.selectDynamic(userName, null, null);
            if (userList != null && userList.size() > 0) {
                return Result.fail("用户已经存在");
            } else {
                User user = new User();
                user.setUserName(userName);
                user.setPassword(password);
                user.setRole(role);
                userDao.insertUser(user);
                return Result.ok();
            }
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(@RequestParam(value = "userName") String userName,
                         @RequestParam(value = "password", required = false) String password,
                         @RequestParam(value = "role", required = false) String role) {
        try {
            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            user.setRole(role);
            userDao.deleteUser(user);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @RequestMapping("/all")
    @ResponseBody
    public Result all() {
        try {
            List<User> userList = userDao.selectAllUser();
            return Result.ok(userList);
        } catch (Exception e) {
            return Result.fail(e);
        }
    }
}
