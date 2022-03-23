package com.yyide.chatim.activity.book;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.tencent.mmkv.MMKV;
import com.yyide.chatim.R;
import com.yyide.chatim.SpData;
import com.yyide.chatim.adapter.ItemBookSearchAdapter;
import com.yyide.chatim.adapter.ItemBookSearchHistoryAdapter;
import com.yyide.chatim.base.BaseConstant;
import com.yyide.chatim.base.BaseMvpActivity;
import com.yyide.chatim.databinding.EmptyBinding;
import com.yyide.chatim.model.BookSearchRsp;
import com.yyide.chatim.model.Student;
import com.yyide.chatim.model.Teacher;
import com.yyide.chatim.presenter.BookSearchPresenter;
import com.yyide.chatim.view.BookSearchView;
import com.yyide.chatim.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通讯录搜索
 */
public class BookSearchActivity extends BaseMvpActivity<BookSearchPresenter> implements BookSearchView {
    private static final String TAG = "BookSearchActivity";
    @BindView(R.id.edit)
    EditText editText;

    @BindView(R.id.cl_search_content)
    ConstraintLayout cl_search_content;

    @BindView(R.id.cl_search_history)
    ConstraintLayout cl_search_history;

    @BindView(R.id.recyclerview_teacher)
    RecyclerView recyclerview_teacher;
    @BindView(R.id.recyclerview_student11)
    RecyclerView recyclerview_student11;
    @BindView(R.id.recyclerview_search_history)
    RecyclerView recyclerviewHistory;

    @BindView(R.id.btn_delete_search)
    Button btnDeleteSearch;
    @BindView(R.id.studentlayout)
    LinearLayout studentlayout;
    @BindView(R.id.teacherlayout)
    LinearLayout teacherlayout;
    private ItemBookSearchAdapter adapterTeacher;
    private ItemBookSearchAdapter adapterStudent;
    private ItemBookSearchHistoryAdapter itemBookSearchHistoryAdapter;

    @Override
    public int getContentViewID() {
        return R.layout.activity_book_search;
    }

    private List<String> tags = new ArrayList<>();//存储历史

    private static String BOOK_SEARCH_HISTORY = "BOOK_SEARCH_HISTORY";
    /**
     * 从闸机通行统计app搜索跳转
     */
    public static String FROM_GATE = "from_gate";

    public String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getIntent().getStringExtra("from");
        recyclerview_teacher.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_student11.setLayoutManager(new LinearLayoutManager(this));
        adapterTeacher = new ItemBookSearchAdapter();
        adapterStudent = new ItemBookSearchAdapter();
        adapterTeacher.setFrom(from);
        adapterStudent.setFrom(from);
        EmptyBinding emptyBinding = EmptyBinding.inflate(getLayoutInflater());
        emptyBinding.tvDesc.setText("未找到该人员");
        recyclerview_teacher.setAdapter(adapterTeacher);
        recyclerview_student11.setAdapter(adapterStudent);
//        adapterTeacher.setEmptyView(emptyBinding.getRoot());
//        adapterStudent.setEmptyView(emptyBinding.getRoot());



        //初始化标签
        Set<String> search_history = MMKV.defaultMMKV().decodeStringSet(BOOK_SEARCH_HISTORY, new HashSet<String>());
        if (!search_history.isEmpty()) {
            tags.addAll(search_history);
        }
        if (tags.isEmpty()) {
            cl_search_history.setVisibility(View.GONE);
        } else {
            cl_search_history.setVisibility(View.VISIBLE);
        }
        itemBookSearchHistoryAdapter = new ItemBookSearchHistoryAdapter(this, tags);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);//主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);//按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);//交叉轴的起点对齐。

        recyclerviewHistory.setLayoutManager(flexboxLayoutManager);
        recyclerviewHistory.addItemDecoration(new SpacesItemDecoration(SpacesItemDecoration.dip2px(5)));
        recyclerviewHistory.setAdapter(itemBookSearchHistoryAdapter);
        itemBookSearchHistoryAdapter.setOnClickedListener(position -> {
            String tag = tags.get(position);
            editText.setText(tag);
            search(tag);
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            Log.e(TAG, "onEditorAction: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ((InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(BookSearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                String keyWord = editText.getText().toString();
                if (TextUtils.isEmpty(keyWord)) {
                    ToastUtils.showShort("请输入关键词内容");
                    return true;
                }
                saveHistory(keyWord);
                search(keyWord);
                return true;
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (!TextUtils.isEmpty(s1)) {
                    btnDeleteSearch.setVisibility(View.VISIBLE);
                } else {
                    btnDeleteSearch.setVisibility(View.GONE);
                }
            }
        });
    }

    private void search(String keyWord) {
        if (SpData.getIdentityInfo().staffIdentity()) {
            mvpPresenter.bookSearch(keyWord, "2",from);
        }else{
            mvpPresenter.bookSearch(keyWord, "1",from);
        }
//        if (GetUserSchoolRsp.DataBean.TYPE_PARENTS.equals(SpData.getIdentityInfo().status)) {
//            mvpPresenter.bookSearch(keyWord, "1",from);
//        } else {
//            mvpPresenter.bookSearch(keyWord, "2",from);
//        }
    }

    private void saveHistory(String keyWord) {
        Set<String> book_search_history = MMKV.defaultMMKV().decodeStringSet(BOOK_SEARCH_HISTORY, new HashSet<>());
        if (book_search_history.isEmpty()) {
            book_search_history = new HashSet<>();
        }
        book_search_history.add(keyWord);
        MMKV.defaultMMKV().encode(BOOK_SEARCH_HISTORY, book_search_history);
    }

    @Override
    protected BookSearchPresenter createPresenter() {
        return new BookSearchPresenter(this);
    }

    @Override
    public void selectUserListSuccess(BookSearchRsp model) {
//        Log.e(TAG, "selectUserListSuccess: " + model.toString());
        if (model.getCode() == BaseConstant.REQUEST_SUCCES_0) {
            hideHistory();
            List<Teacher> teacherList = model.getData().getElternList();
            List<Teacher> studentlist = new ArrayList();
            recyclerview_teacher.scrollToPosition(0);
            recyclerview_student11.scrollToPosition(0);
            //将学生数据加入
            if (!model.getData().getStudentList().isEmpty()) {
                for (Student student : model.getData().getStudentList()) {
//                    var subjectName: String?,
//                            var userId: String?,
//                            var gender: String?,
//                            var phone: String?,
//                            var id: String?,
//                            var employeeSubjects: String?,
//                            var email: String?,
//                            var departmentName: String?,
//                            var avatar: String?,
//                            var concealPhone: String?,
                    Teacher student1 = new Teacher("", null, student.getName(), student.getType(), student.getTypeName(), student.getTypeName(),student, "","",student.getGender(),student.getPhone(),student.getId(),"",student.getElternAddBookDTOList(),student.getEmail(),student.getDepartmentName(),student.getAvatar(),"",student.getClassName(),1);
                    studentlist.add(student1);
                }
            }
            adapterTeacher.setList(teacherList);
            adapterStudent.setList(studentlist);
            studentlayout.setVisibility(studentlist.size()>0?View.VISIBLE:View.GONE);
            teacherlayout.setVisibility(teacherList.size()>0?View.VISIBLE:View.GONE);
        }
    }

    private void showHistory() {
        cl_search_content.setVisibility(View.GONE);
        Set<String> search_history = MMKV.defaultMMKV().decodeStringSet(BOOK_SEARCH_HISTORY, new HashSet<>());
        if (!search_history.isEmpty()) {
            tags.clear();
            tags.addAll(search_history);
            itemBookSearchHistoryAdapter.notifyDataSetChanged();
        }
        if (tags.isEmpty()) {
            cl_search_history.setVisibility(View.GONE);
        } else {
            cl_search_history.setVisibility(View.VISIBLE);
        }
    }

    private void hideHistory() {
        cl_search_content.setVisibility(View.VISIBLE);
        cl_search_history.setVisibility(View.GONE);
    }

    private void clearHistory() {
        Set<String> search_history = MMKV.defaultMMKV().decodeStringSet(BOOK_SEARCH_HISTORY, new HashSet<>());
        search_history.clear();
        tags.clear();
        MMKV.defaultMMKV().encode(BOOK_SEARCH_HISTORY, search_history);
        cl_search_history.setVisibility(View.GONE);
        itemBookSearchHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectUserListFail(String msg) {
        Log.e(TAG, "selectUserListFail: " + msg);
    }

    @OnClick({R.id.cancel, R.id.btn_delete_search, R.id.btn_delete_search_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.btn_delete_search:
                editText.setText("");
                showHistory();
                break;
            case R.id.btn_delete_search_history:
                clearHistory();
                break;
            default:
                break;
        }
    }

    @Override
    public void showError() {

    }
}