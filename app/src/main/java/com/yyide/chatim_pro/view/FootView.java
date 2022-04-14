package com.yyide.chatim_pro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyide.chatim_pro.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FootView extends RelativeLayout {

    @BindView(R.id.foot_view_progressbar)
    ProgressBar footViewProgressbar;

    @BindView(R.id.tv_load_more)
    TextView tv_load_more;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;//没有更多数据

    public FootView(Context context) {
        this(context, null);
    }

    public FootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.foot_view, this);
        ButterKnife.bind(this, this);
    }

    /**
     * 设置是否加载中，
     * @param loading
     */
    public void setLoading(boolean loading) {
        footViewProgressbar.setVisibility(loading?View.VISIBLE:View.GONE);
        tv_load_more.setVisibility(loading?View.VISIBLE:View.GONE);
        tv_no_data.setVisibility(loading?View.GONE:View.VISIBLE);
    }
}