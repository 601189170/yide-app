package com.yyide.chatim.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.activity.book.BookPatriarchDetailActivity;
import com.yyide.chatim.activity.book.BookSearchActivity;
import com.yyide.chatim.activity.book.BookStudentDetailActivity;
import com.yyide.chatim.activity.book.BookTeacherDetailActivity;
import com.yyide.chatim.activity.gate.GateDetailInfoActivity;
import com.yyide.chatim.databinding.ItemBookSearchBinding;
import com.yyide.chatim.databinding.ItemNewBookGuardianBinding;
import com.yyide.chatim.databinding.ItemNewBookGuardianSearchBinding;
import com.yyide.chatim.databinding.ItemNewBookStudentBinding;
import com.yyide.chatim.databinding.ItemNewBookStudentSearchBinding;
import com.yyide.chatim.model.BookGuardianItem;
import com.yyide.chatim.model.BookSearchStudent;
import com.yyide.chatim.model.BookStudentItem;
import com.yyide.chatim.model.BookTeacherItem;
import com.yyide.chatim.model.Parent;
import com.yyide.chatim.model.Student;
import com.yyide.chatim.model.Teacher;
import com.yyide.chatim.utils.GlideUtil;

import java.util.ArrayList;

/**
 * @Description: 通讯录搜索adapter
 * @Author: liu tao
 * @CreateDate: 2021/4/12 18:58
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/12 18:58
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ItemBookSearchAdapter extends BaseMultiItemQuickAdapter<Teacher, BaseViewHolder> {
    private int ITEM_TYPE_TEACHER = 0;
    private int ITEM_TYPE_STUDENT = 1;
    private String from;
    public void setFrom(String from) {
        this.from = from;
    }

    public ItemBookSearchAdapter() {
        addItemType(ITEM_TYPE_TEACHER, R.layout.item_book_search);
        addItemType(ITEM_TYPE_STUDENT, R.layout.item_new_book_student_search);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, Teacher teacher) {
        if (holder.getItemViewType() == ITEM_TYPE_STUDENT) {
            Student student = teacher.getStudent();
            ItemNewBookStudentSearchBinding bind = ItemNewBookStudentSearchBinding.bind(holder.itemView);
            bind.tvName.setText(TextUtils.isEmpty(student.getName()) ? "未知姓名" : student.getName() + "（" + student.getTypeName() + "）");
            GlideUtil.loadImageHead(
                    getContext(),
                    student.getFaceInformation(),
                    bind.img
            );
            GuardianAdapter guardianAdapter = new GuardianAdapter();
            bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            bind.recyclerView.setAdapter(guardianAdapter);
            guardianAdapter.setList(student.getParentList());
            guardianAdapter.setOnItemClickListener((adapter, view, position1) -> {
                Parent item1 = guardianAdapter.getItem(position1);
                if (BookSearchActivity.FROM_GATE.equals(from)){
                    //跳转闸机通行数据详情页
                    return;
                }
                BookGuardianItem guardianItem = new BookGuardianItem(
                        item1.getName(),
                        item1.getId(),
                        item1.getPhone(),
                        item1.getUserId(),
                        item1.getRelation(),
                        item1.getWorkUnit(),
                        item1.getFaceInformation(),
                        item1.getSingleParent()
                );
                BookPatriarchDetailActivity.start(getContext(), guardianItem);
            });
            holder.itemView.setOnClickListener(v -> {
//                BookSearchStudent item = student.getList();
                if (BookSearchActivity.FROM_GATE.equals(from)){
                    //跳转闸机通行数据详情页
                    final String userId = teacher.getUserId();
                    GateDetailInfoActivity.Companion.toDetail(getContext(),1,null,userId,null);
                    return;
                }
//                BookStudentItem studentItem = new BookStudentItem(item.getId(),
//                        item.getName(),
//                        item.getPhone(),
//                        item.getClassName(),
//                        item.getUserId(),
//                        item.getPrimaryGuardianPhone(),
//                        item.getDeputyGuardianPhone(),
//                        item.getSex(),
//                        item.getAddress(),
//                        item.getFaceInformation(),
//                        item.isOwnChild(),
//                        null);
//                BookStudentDetailActivity.start(getContext(), studentItem);
                BookTeacherItem studentItem = new BookTeacherItem(
                        teacher.getName(),
                        teacher.getGender(),
                        teacher.getPhone(),
                        teacher.getUserId(),
                        teacher.getEmail(),
                        teacher.getSubjectName(),
                        teacher.getSubjectName(),
                        teacher.getFaceInformation(),
                        teacher.getWhitelist());
                BookTeacherDetailActivity.start(getContext(), studentItem);
            });
        } else {
            ItemBookSearchBinding bind = ItemBookSearchBinding.bind(holder.itemView);
            GlideUtil.loadImageHead(getContext(), teacher.getFaceInformation(), bind.ivHead);
//            if (!SpData.getIdentityInfo().staffIdentity() && teacher.getList() != null) {
            if (teacher.getList()!=null&&teacher.getList().getTeachingSubjects()!=null){
                bind.tvName.setText(teacher.getName() + " (" + teacher.getList().getTeachingSubjects() + ")");
            }else {
                bind.tvName.setText(teacher.getName());
            }
//            } else {
//                bind.tvName.setText(teacher.getName() + " (" + teacher.getTypeName() + ")");
//            }

            holder.itemView.setOnClickListener(v -> {

                BookTeacherItem teacherItem1 = teacher.getList();
                if (BookSearchActivity.FROM_GATE.equals(from)){
                    //跳转闸机通行数据详情页
                    final String userId = teacherItem1.getUserId();
                    GateDetailInfoActivity.Companion.toDetail(getContext(),2,null,userId,null);
                    return;
                }

//                val list= ArrayList();
//                if (teacherItem1!=null){
                    BookTeacherItem teacherItem = new BookTeacherItem(
                            teacher.getName(),
                            teacher.getGender(),
                            teacher.getPhone(),
                            teacher.getUserId(),
                            teacher.getEmail(),
                            teacher.getSubjectName(),
                            teacher.getSubjectName(),
                            teacher.getFaceInformation(),
                            teacher.getWhitelist());
                    BookTeacherDetailActivity.start(getContext(), teacherItem);
//                }

            });
        }
    }

    class GuardianAdapter extends
            BaseQuickAdapter<Parent, BaseViewHolder> {
        public GuardianAdapter() {
            super(R.layout.item_new_book_guardian_search);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, Parent item) {
            ItemNewBookGuardianSearchBinding bind = ItemNewBookGuardianSearchBinding.bind(baseViewHolder.itemView);
            bind.tvName.setText(item.getName());
            bind.tvGuardianName.setText(TextUtils.isEmpty(item.getRelationType())?"":item.getRelationType());
//            if (!TextUtils.isEmpty(item.getRelationType())){
//                bind.tvGuardianName.setText(item.getRelationType());
//            }else {
//                bind.tvGuardianName.setText(item.getRelationType());
//            }

        }
    }
}
