package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserInfoMapper {
    int insertUserInfo(UserInfoDTO pDTO) throws Exception; // 회원가입   / 결과를 성공 실패 여부에 따라 0 1 2로 받으므로 int형 선언

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;    // 로그인

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;     // 회원가입 전 중복 아이디 체크

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;      // 회원가입 전 이메일 중복 체크
}
