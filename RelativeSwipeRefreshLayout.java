package jijiehao.minhua.com.sidesliplistviewdemo;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * @author moo
 * @date 2017/12/1.
 * @describe
 */

public class RelativeSwipeRefreshLayout extends SwipeRefreshLayout {
    /**
     * 是否在水平滑动
     */
    private boolean mIsVpDragger;

    public RelativeSwipeRefreshLayout(Context context) {
        super(context);
    }

    public RelativeSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private float startX;
    private float startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                mIsVpDragger = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsVpDragger) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (distanceX > ViewConfiguration.get(getContext()).getScaledTouchSlop() && distanceX > distanceY) {
                    mIsVpDragger = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                mIsVpDragger = false;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
