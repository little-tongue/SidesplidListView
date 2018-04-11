package jijiehao.minhua.com.sidesliplistview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


/**
 * @author moodstrikerdd
 * @date 2018/4/10
 * @label
 */

public class RelativeSwipeRefreshLayout extends SwipeRefreshLayout {


    public RelativeSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public RelativeSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }


    /**
     * 是否让子view处理touch事件
     */
    private boolean letChildDealTouchEvent;
    private float startX;
    private float startY;
    private int mTouchSlop;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置
                startY = ev.getY();
                startX = ev.getX();
                // 初始化标记
                letChildDealTouchEvent = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果子view正在拖拽中，那么不拦截它的事件，直接return false；
                if (letChildDealTouchEvent) {
                    return false;
                }

                // 获取当前手指位置
                float endY = ev.getY();
                float endX = ev.getX();
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);
                // 如果X轴位移大于Y轴位移，那么将事件交给子View处理
                if (distanceX > mTouchSlop && distanceX > distanceY) {
                    letChildDealTouchEvent = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 初始化标记
                letChildDealTouchEvent = false;
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
