package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

public interface IUserInfoService {
    int insertUserInfo(UserInfoDTO pDTO) throws Exception;  // 회원가입시 회원정보 등록

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;    // 로그인시 id pw 일치하는지 확인
}
