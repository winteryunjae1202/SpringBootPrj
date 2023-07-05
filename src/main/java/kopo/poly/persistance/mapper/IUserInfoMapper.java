package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IUserInfoMapper {
    int insertUserInfo(UserInfoDTO pDTO) throws Exception; // 결과를 성공 실패 여부에 따라 0 1 2로 받으므로 int형 선언

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;
}
