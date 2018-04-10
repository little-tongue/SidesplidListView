package jijiehao.minhua.com.sidesliplistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Scroller;

/**
 * @author moodstrikerdd
 * @date 2018/4/10
 * @label
 */

public class SideslipListView extends ListView {
    /**
     * 屏幕宽度
     */
    private int mScreenWidth;
    /**
     * 按下点的x值
     */
    private int mDownX;
    /**
     * 按下点的y值
     */
    private int mDownY;
    /**
     * 删除按钮的宽度
     */
    private int mDeleteBtnWidth;
    /**
     * 当前处理的item
     */
    private ViewGroup mPointChild;
    /**
     * 当前处理上一个item
     */
    private ViewGroup mLastPointChild;
    /**
     * 当前处理的item的LayoutParams
     */
    private LinearLayout.LayoutParams mLayoutParams;

    private Scroller mScroller;

    public SideslipListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideslipListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScroller = new Scroller(context);
    }

    /**
     * 是否拦截down up时间
     */
    private boolean shouldOnlyTouchDown;
    /**
     * 是否正在左划
     */
    private boolean isLeftScrolling;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("tag", "ACTION_DOWN");
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("tag", "ACTION_MOVE");
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.e("tag", "ACTION_UP");
                return performActionUp(ev);
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 处理action_down事件
     */
    private void performActionDown(MotionEvent ev) {
        if (shouldTurnToNormal()) {
            turnToNormal();
            shouldOnlyTouchDown = true;
            return;
        }

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();

        //有头时 头不能左划
        if (getHeaderViewsCount() == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return;
        }
        //有尾时 尾不能左划
        if (getFooterViewsCount() == 1 && pointToPosition(mDownX, mDownY) == getCount() - 1) {
            return;
        }
        // 获取当前点的item
        mPointChild = (ViewGroup) getChildAt(pointToPosition(mDownX, mDownY)
                - getFirstVisiblePosition());

        if (mPointChild == null) {
            return;
        }
        // 获取删除按钮的宽度（itemView的固定格式，左右两块左边为内容 宽度全屏 右边为菜单）
        mDeleteBtnWidth = mPointChild.getChildAt(1).getMeasuredWidth();
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    /**
     * 处理action_move事件
     */
    private boolean performActionMove(MotionEvent ev) {
        //有头时 头不能左划
        if (getHeaderViewsCount() == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return super.onTouchEvent(ev);
        }
        //有尾时 尾不能左划
        if (getFooterViewsCount() == 1 && pointToPosition(mDownX, mDownY) == getCount() - 1) {
            return super.onTouchEvent(ev);
        }
        if (shouldOnlyTouchDown) {
            return true;
        }
        if (mPointChild == null) {
            return super.onTouchEvent(ev);
        }
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if (Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if (nowX < mDownX) {
                isLeftScrolling = true;
                // 计算要偏移的距离
                int scroll = (nowX - mDownX);
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if (-scroll >= mDeleteBtnWidth) {
                    scroll = -mDeleteBtnWidth;
                }
                mPointChild.scrollTo(-scroll, 0);
            }
        }
        return isLeftScrolling || super.onTouchEvent(ev);
    }

    /**
     * 处理action_up事件
     */
    private boolean performActionUp(MotionEvent ev) {
        isLeftScrolling = false;

        if (shouldOnlyTouchDown) {
            shouldOnlyTouchDown = false;
            return true;
        }

        //有头时 头不能左划
        if (getHeaderViewsCount() == 1 && pointToPosition(mDownX, mDownY) == 0) {
            return super.onTouchEvent(ev);
        }
        //有尾时 头不能左划
        if (getFooterViewsCount() == 1 && pointToPosition(mDownX, mDownY) == getCount() - 1) {
            return super.onTouchEvent(ev);
        }

        if (mPointChild == null) {
            return super.onTouchEvent(ev);
        }

        mLastPointChild = mPointChild;
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        int scrollX = mPointChild.getScrollX();

        if (scrollX >= mDeleteBtnWidth / 2) {
            mScroller.startScroll(scrollX, 0, mDeleteBtnWidth - scrollX, 0);
            invalidate();
        } else {
            turnToNormal();
        }
        return true;
    }

    /**
     * 变为正常状态
     */
    public void turnToNormal() {
        if (mLastPointChild == null) {
            return;
        }
        int scrollX = mLastPointChild.getScrollX();
        Log.e("tag", "scrollX   " + scrollX);
        mScroller.startScroll(scrollX, 0, -scrollX, 0);
        invalidate();
    }

    private boolean shouldTurnToNormal() {
        return mLastPointChild != null && mLastPointChild.getScrollX() != 0;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mLastPointChild.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
