package com.yyide.chatim.view;


import com.yyide.chatim.base.BaseView;
import com.yyide.chatim.model.TeacherlistRsp;
import com.yyide.chatim.model.listByAppRsp;

/**
 * 作者：Rance on 2016/10/25 15:19
 * 邮箱：rance935@163.com
 */
public interface NoteByListBookView extends BaseView {

    void TeacherlistRsp(TeacherlistRsp rsp);

    void TeacherlistRspFail(String rsp);

    void studentListRsp(TeacherlistRsp rsp);

    void studentListRspFail(String msg);

}
