package com.cjy.retrofitframe;


import com.cjy.retrofitlibrary.model.BaseResponseModel;

import java.util.List;

/**
 * <功能详细描述>
 * <p>
 * Data：2018/12/18
 *
 * @author yong
 */
public class LoginModel extends BaseResponseModel<LoginModel> {

    /**
     * obj : {"accountNonExpired":true,"accountNonLocked":true,"authorities":[{"authority":"ROLE_ADMIN"}],"companyId":134,"credentialsNonExpired":true,"enabled":true,"id":225,"isAgreeDisclaimer":1,"loginName":"15713802736","mobile":"15713802736","passCode":0,"status":1,"userType":"10","username":"15713802736"}
     * expire : 21600
     * token : eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE1NjM1MjA4MDUsInN1YiI6IjE1NzEzODAyNzM2IiwiZXhwIjoxNTYzNTQyNDA1LCJqdGkiOiI0NTM4MTFlM2Y4YTk0Y2UwOGNkMWNmYzA3ZGQwOGVmMCIsImUiOjIxNjAwLCJhdWQiOiI3MTMyZjAzZTM5NmY0NjkwODUxNDQyNWI3MTJkMTVmYyJ9.ZAiLOokK7X-9ukeMcJ8it5WdCpIghyyj3gTuulFyhHw
     */

    private ObjBean obj;
    private int userId;
    private int expire;
    private String token;

    public int getUserId() {
        return userId;
    }

    public LoginModel setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class ObjBean {
        /**
         * accountNonExpired : true
         * accountNonLocked : true
         * authorities : [{"authority":"ROLE_ADMIN"}]
         * companyId : 134
         * credentialsNonExpired : true
         * enabled : true
         * id : 225
         * isAgreeDisclaimer : 1
         * loginName : 15713802736
         * mobile : 15713802736
         * passCode : 0
         * status : 1
         * userType : 10
         * username : 15713802736
         */

        private boolean accountNonExpired;
        private boolean accountNonLocked;
        private int companyId;
        private boolean credentialsNonExpired;
        private boolean enabled;
        private int id;
        private int isAgreeDisclaimer;
        private String loginName;
        private String mobile;
        private int passCode;
        private int status;
        private String userType;
        private String username;
        private List<AuthoritiesBean> authorities;

        public boolean isAccountNonExpired() {
            return accountNonExpired;
        }

        public void setAccountNonExpired(boolean accountNonExpired) {
            this.accountNonExpired = accountNonExpired;
        }

        public boolean isAccountNonLocked() {
            return accountNonLocked;
        }

        public void setAccountNonLocked(boolean accountNonLocked) {
            this.accountNonLocked = accountNonLocked;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public boolean isCredentialsNonExpired() {
            return credentialsNonExpired;
        }

        public void setCredentialsNonExpired(boolean credentialsNonExpired) {
            this.credentialsNonExpired = credentialsNonExpired;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsAgreeDisclaimer() {
            return isAgreeDisclaimer;
        }

        public void setIsAgreeDisclaimer(int isAgreeDisclaimer) {
            this.isAgreeDisclaimer = isAgreeDisclaimer;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getPassCode() {
            return passCode;
        }

        public void setPassCode(int passCode) {
            this.passCode = passCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<AuthoritiesBean> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(List<AuthoritiesBean> authorities) {
            this.authorities = authorities;
        }

        public static class AuthoritiesBean {
            /**
             * authority : ROLE_ADMIN
             */

            private String authority;

            public String getAuthority() {
                return authority;
            }

            public void setAuthority(String authority) {
                this.authority = authority;
            }
        }
    }
}
