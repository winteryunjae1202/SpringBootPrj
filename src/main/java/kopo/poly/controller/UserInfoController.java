package kopo.poly.controller;

import kopo.poly.service.IUserInfoService;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@Controller
@RequiredArgsConstructor
public class UserInfoController {
    private final IUserInfoService userInfoService;

    @GetMapping(value = "/user/userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm");

        return "/user/userRegForm";
    }

    @PostMapping(value = "/user/insertUserInfo")
    public String insertUserInfo(HttpServletRequest request, ModelMap modelMap) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo start!");
        int res;
        String msg = "";
        String url = "";

        UserInfoDTO pDTO = null;

        try {
            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            log.info("user_id : " + user_id);
            log.info("user_name : " + user_name);
            log.info("password : " + password);
            log.info("email : " + email);
            log.info("addr1 : " + addr1);
            log.info("addr2 : " + addr2);

            pDTO = new UserInfoDTO();

            pDTO.setUser_id(user_id);
            pDTO.setUser_name(user_name);

            pDTO.setPassword(EncryptUtil.encHashSHA256(password));  // Encrypt~ => 비밀번호 암호화  encHASH~ => 원상복구 불가능
            // 비밀번호 잊어버렸을 때 기존으로 복구가 아닌 비밀번호 재설정하는것이 이것 때문
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));  // encAES~ => 원상복구 가능
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            // 회원가입
            res = userInfoService.insertUserInfo(pDTO);
            log.info("회원가입 결과(res) : " + res);
            if (res == 1) {
                msg = "회원가입되었습니다.";
                url = "/user/login";
            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
                url = "/user/userRegForm";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }
        } catch (Exception e) {
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return "/redirect";
    }


    @PostMapping(value = "/user/loginProc")
    public String loginProc(HttpServletRequest request, ModelMap model, HttpSession session) {
        log.info(this.getClass().getName() + ".loginProc Start!");

        String msg = "";
        String url = "";
        UserInfoDTO pDTO = null;

        try {
            String user_id = CmmUtil.nvl(request.getParameter("user_id"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            log.info("user_id : " + user_id);
            log.info("password : " + password);

            pDTO = new UserInfoDTO();
            pDTO.setUser_id(user_id);

            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            UserInfoDTO rDTO = userInfoService.getLogin(pDTO);

            if (CmmUtil.nvl(rDTO.getUser_id()).length() > 0) {
                session.setAttribute("SS_USER_ID", user_id);
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUser_name()));

                msg = "로그인이 성공했습니다. \n" + rDTO.getUser_name() + "님 환영합니다.";
                url = "/notice/noticeList";
            } else {
                msg = "로그인이 실패했습니다. 재입력 해주세요.";
                url = "/user/login";
            }
        } catch (Exception e) {
            msg = "시스템 문제로 로그인이 실패했습니다.";
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            log.info(this.getClass().getName() + ".loginProc End!");
        }

        return "/redirect";
    }

    @GetMapping(value = "/user/login")
    public String login() {
        log.info(this.getClass().getName() + ".user.login Start!");
        log.info(this.getClass().getName() + ".user.login End!");

        return "/user/login";
    }
}