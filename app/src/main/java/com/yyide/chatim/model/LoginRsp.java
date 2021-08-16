package com.yyide.chatim.model;

import android.text.TextUtils;

public class LoginRsp {


    /**
     * msg : 登录成功
     * code : 200
     * token : yide-eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIyOTciLCJzdWIiOiIxMzY1OTg5NjU5NiIsImlhdCI6MTYxNDkyMzk5MiwiaXNzIjoieWlkZSIsImF1dGhvcml0aWVzIjoiW3tcImF1dGhvcml0eVwiOlwiUk9MRV_otoXnuqfnrqHnkIblkZhcIn1dIiwicGFzc3dvcmQiOiJcIiQyYSQxMCRZZ0JhblR2WHdGVnFzTktpLzBWM01PNW9TbnBXS3N2cktCVi55TEpIR1djeEVnWnI5VkgxMlwiIiwic2Nob29sSWQiOiIxNTgiLCJzY2hvb2xOYW1lIjoi5Y-R55qE5Y-R55SfIiwic3RhdHVzIjoiTk9STUFMIiwiZGF0YVBlckluZCI6IlkiLCJkZXBJZHMiOiJbXSIsImRhdGFQZXJEZXBJZHMiOiJudWxsIiwidGVhY2hlcklkIjoibnVsbCIsImV4cCI6MTYxNDk1Mzk5Mn0.L7w0x6Ify0dOliIQ2AkNXWkfFRtaq2zA4Q8O7Qkkk2sGdCGakd10_fAzia-marEtk97oCOt-yYK33eS34sysNg
     */

    public String msg;
    public String message;
    public int code;
    public String data;

    /**
     * 处理token为空时避免请求报错导致闪退
     *
     * @return
     */
    public String getToken() {
        return TextUtils.isEmpty(data) ? "android_token==null" : data;
    }

}
