package kopo.poly.service.impl;


import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService implements IMailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public int doSendMail(MailDTO pDTO) {
        log.info(this.getClass().getName() + ".doSendMail start!");

        int res = 1;        // res = 1 ==> 발송 성공 / res = 0 ==> 발송 실패

        if(pDTO == null){
            pDTO = new MailDTO();
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail());      // 받는사람
        String title = CmmUtil.nvl(pDTO.getTitle());        // 메일제목
        String contents = CmmUtil.nvl(pDTO.getContents());  // 보낼 내용

        log.info("toMail : " + toMail);
        log.info("title : " + title);
        log.info("contents : " + contents);

        MimeMessage message = mailSender.createMimeMessage();       // 메일 발송 메시지 구조

        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");      // 메일 발송 메시지 구조를 쉽게 생성하게 도와주는 객체

        try {
            messageHelper.setTo(toMail);        // 받는사람
            messageHelper.setFrom(fromMail);    // 보내는사람
            messageHelper.setSubject(title);    // 메일제목
            messageHelper.setText(contents);    // 메일 내용

            mailSender.send(message);

        }   catch (Exception e) {
            res = 0;        // 메일 발송 실패의 경우
            log.info("[ERROR] " + this.getClass().getName() + ".doSendMail : " + e);
        }

        log.info(this.getClass().getName() + ".doSendMail end!");
        return res;
    }
}
