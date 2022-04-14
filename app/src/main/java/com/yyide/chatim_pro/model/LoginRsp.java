package com.yyide.chatim_pro.model;

import android.text.TextUtils;

import com.yyide.chatim_pro.kotlin.network.base.BaseResponse;

import java.io.Serializable;

public class LoginRsp extends BaseResponse<LoginRsp> implements Serializable {

    /**
     * msg : 登录成功
     * code : 200
     * token : yide-eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyOTciLCJzdWIiOiIxMzY1OTg5NjU5NiIsImlhdCI6MTYxNDkyMzk5MiwiaXNzIjoieWlkZSIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV_otoXnuqfnrqHnkIblkZhcIn1dIiwicGFzc3dvcmQiOiJcIiQyYSQxMCRZZ0JhblR2WHdGVnFzTktpLzBWM01PNW9TbnBXS3N2cktCVi55TEpIR1djeEVnWnI5VkgxMlwiIiwic2Nob29sSWQiOiIxNTgiLCJzY2hvb2xOYW1lIjoi5Y-R55qE5Y-R55SfIiwic3RhdHVzIjoiTk9STUFMIiwiZGF0YVBlckluZCI6IlkiLCJkZXBJZHMiOiJbXSIsImRhdGFQZXJEZXBJZHMiOiJudWxsIiwidGVhY2hlcklkIjoibnVsbCIsImV4cCI6MTYxNDk1Mzk5Mn0.L7w0x6Ify0dOliIQ2AkNXWkfFRtaq2zA4Q8O7Qkkk2sGdCGakd10_fAzia-marEtk97oCOt-yYK33eS34sysNg
     */
    public String accessToken;
    public String refreshToken;
    public String expiresIn;
    public String tokenHead;

    //是否登录
    public boolean isLogin;

    public String getAccessToken() {
        String s = tokenHead + accessToken;
        return TextUtils.isEmpty(s) ? "token" : s;
    }
}
