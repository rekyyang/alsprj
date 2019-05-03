package zju.edu.als.dao;

import org.apache.ibatis.annotations.Param;
import zju.edu.als.domain.user.User;

import java.util.List;

/**
 * Created by zzq on 2016/12/19.
 */
public interface UserDao {
    public boolean insertUser(@Param("user") User user);
    public boolean deleteUser(@Param("user")User user);
    public List<User> selectAllUser();
    public List<User> selectDynamic(@Param("userName")String userName,
                             @Param("password")String password,
                             @Param("role")String role);
    public boolean updateUser(@Param("userName")String userName,
                          @Param("password")String password,
                          @Param("role")String role);
}
