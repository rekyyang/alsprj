package zju.edu.als.constant;

/**
 * Created by zzq on 2016/12/19.
 */
public enum UserRoles {
    admin("admin"),
    doctor("doctor"),
    patient("patient");
    String role;
     UserRoles(String role){
        this.role=role;
    }
}
