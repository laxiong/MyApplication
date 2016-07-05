


package com.laxiong.View;
/**
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file kt in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.laxiong.Adapter.CalenderBean;
import com.carfriend.mistCF.BuildConfig;
import com.carfriend.mistCF.R;
public class PinnedSectionListView extends ListView {

	 //-- inner classes

		/** List adapter to be implemented for being used with PinnedSectionListView adapter. */
		public static interface PinnedSectionListAdapter extends ListAdapter {
			/** This method shall return 'true' if views of given type has to be pinned. */
			boolean isItemViewTypePinned(int viewType);
		}
		public static interface IViewFragUpdate{
			void updateTitle(int year,int month);
		}
		private IViewFragUpdate iviewlistener;
		public void setIViewListener(IViewFragUpdate iviewlistener){
			this.iviewlistener=iviewlistener;
		}
		/** Wrapper class for pinned section view and its position in the list. */
		static class PinnedSection {
			public View view;
			public int position;
			public long id;
		}

		//-- class fields

	    // fields used for handling touch events
	    private final Rect mTouchRect = new Rect();
	    private final PointF mTouchPoint = new PointF();
	    private int mTouchSlop;
	    private View mTouchTarget;
	    private MotionEvent mDownEvent;

	    // fields used for drawing shadow under a pinned section
	    private GradientDrawable mShadowDrawable;
	    private int mSectionsDistanceY;
	    private int mShadowHeight;

	    /** Delegating listener, can be null. */
	    OnScrollListener mDelegateOnScrollListener;

	    /** Shadow for being recycled, can be null. */
	    PinnedSection mRecycleSection;

	    /** shadow instance with a pinned view, can be null. */
	    PinnedSection mPinnedSection;

	    /** Pinned view Y-translation. We use it to stick pinned view to the next section. */
	    int mTranslateY;

		/** Scroll listener which does the magic */
		private final OnScrollListener mOnScrollListener = new OnScrollListener() {

			@Override 
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mDelegateOnScrollListener != null) { // delegate
					mDelegateOnScrollListener.onScrollStateChanged(view, scrollState);
				}
				// TODO 刷新
				if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&getLastVisiblePosition()==(getCount()-1)){
//					Log.e("onScrollStateChanged","此时需要显示footView");
					isLoading=true;
					footView.setPadding(0, 0, 0, 0);
					setSelection(getCount());//显示ListView最后一条
					if(listener!=null){
						listener.onLoadingMore();
					}
				}
			}

			@Override
	        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	            if (mDelegateOnScrollListener != null) { // delegate
	                mDelegateOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
	            }

	            // get expected adapter or fail fast
	            ListAdapter adapter = getAdapter();
	            if (adapter == null || visibleItemCount == 0) return; // nothing to do

	            final boolean isFirstVisibleItemSection =
	                    isItemViewTypePinned(adapter, adapter.getItemViewType(firstVisibleItem));

	            if (isFirstVisibleItemSection) {
	                View sectionView = getChildAt(0);
	                if (sectionView.getTop() == getPaddingTop()) { // view sticks to the top, no need for pinned shadow
	                    destroyPinnedShadow();
	                } else { // section doesn't stick to the top, make sure we have a pinned shadow
	                    ensureShadowForPosition(firstVisibleItem, firstVisibleItem, visibleItemCount);
	                }

	            } else { // section is not at the first visible position
	                int sectionPosition = findCurrentSectionPosition(firstVisibleItem);
	                if (sectionPosition > -1) { // we have section position
	                    ensureShadowForPosition(sectionPosition, firstVisibleItem, visibleItemCount);
	                } else { // there is no section for the first visible item, destroy shadow
	                    destroyPinnedShadow();
	                }
	            }
			};

		};

		/** Default change observer. */
	    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
	        @Override public void onChanged() {
	            recreatePinnedShadow();
	        };
	        @Override public void onInvalidated() {
	            recreatePinnedShadow();
	        }
	    };

		//-- constructors

	    public PinnedSectionListView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        initView();
	    }

	    public PinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        initView();
	    }

	    private void initView() {
	        setOnScrollListener(mOnScrollListener);
	        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	        initShadow(true);
	        init(); // TODO 刷新
	    }

	    //-- public API methods

	    public void setShadowVisible(boolean visible) {
	        initShadow(visible);
	        if (mPinnedSection != null) {
	            View v = mPinnedSection.view;
	            invalidate(v.getLeft(), v.getTop(), v.getRight(), v.getBottom() + mShadowHeight);
	        }
	    }

	    //-- pinned section drawing methods

	    public void initShadow(boolean visible) {
	        if (visible) {
	            if (mShadowDrawable == null) {
	                mShadowDrawable = new GradientDrawable(Orientation.TOP_BOTTOM,
	                        new int[] { Color.parseColor("#ffa0a0a0"), Color.parseColor("#50a0a0a0"), Color.parseColor("#00a0a0a0")});
	                mShadowHeight = (int) (8 * getResources().getDisplayMetrics().density);
	            }
	        } else {
	            if (mShadowDrawable != null) {
	                mShadowDrawable = null;
	                mShadowHeight = 0;
	            }
	        }
	    }

		/** Create shadow wrapper with a pinned view for a view at given position */
		void createPinnedShadow(int position) {
			// try to recycle shadow
			PinnedSection pinnedShadow = mRecycleSection;
			mRecycleSection = null;

			// create new shadow, if needed
			if (pinnedShadow == null) pinnedShadow = new PinnedSection();
			// request new view using recycled view, if such
			View pinnedView = getAdapter().getView(position, pinnedShadow.view, PinnedSectionListView.this);

			// read layout parameters
			LayoutParams layoutParams = (LayoutParams) pinnedView.getLayoutParams();
			if (layoutParams == null) {
			    layoutParams = (LayoutParams) generateDefaultLayoutParams();
			    pinnedView.setLayoutParams(layoutParams);
			}

			int heightMode = MeasureSpec.getMode(layoutParams.height);
			int heightSize = MeasureSpec.getSize(layoutParams.height);

			if (heightMode == MeasureSpec.UNSPECIFIED) heightMode = MeasureSpec.EXACTLY;

			int maxHeight = getHeight() - getListPaddingTop() - getListPaddingBottom();
			if (heightSize > maxHeight) heightSize = maxHeight;

			// measure & layout
			int ws = MeasureSpec.makeMeasureSpec(getWidth() - getListPaddingLeft() - getListPaddingRight(), MeasureSpec.EXACTLY);
			int hs = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
			pinnedView.measure(ws, hs);
			pinnedView.layout(0, 0, pinnedView.getMeasuredWidth(), pinnedView.getMeasuredHeight());
			mTranslateY = 0;

			// initialize pinned shadow
			pinnedShadow.view = pinnedView;
			pinnedShadow.position = position;
			pinnedShadow.id = getAdapter().getItemId(position);

			// store pinned shadow
			mPinnedSection = pinnedShadow;
			CalenderBean bean= (CalenderBean) getAdapter().getItem(position);
			iviewlistener.updateTitle(bean.getCurrentDate().getYear(),bean.getCurrentDate().getMonth());
		}

		/** Destroy shadow wrapper for currently pinned view */
		void destroyPinnedShadow() {
		    if (mPinnedSection != null) {
		        // keep shadow for being recycled later
		        mRecycleSection = mPinnedSection;
		        mPinnedSection = null;
		    }
		}

		/** Makes sure we have an actual pinned shadow for given position. */
	    void ensureShadowForPosition(int sectionPosition, int firstVisibleItem, int visibleItemCount) {
	        if (visibleItemCount < 2) { // no need for creating shadow at all, we have a single visible item
	            destroyPinnedShadow();
	            return;
	        }

	        if (mPinnedSection != null
	                && mPinnedSection.position != sectionPosition) { // invalidate shadow, if required
	            destroyPinnedShadow();
	        }

	        if (mPinnedSection == null) { // create shadow, if empty
	            createPinnedShadow(sectionPosition);
	        }

	        // align shadow according to next section position, if needed
	        int nextPosition = sectionPosition + 1;
	        if (nextPosition < getCount()) {
	            int nextSectionPosition = findFirstVisibleSectionPosition(nextPosition,
	                    visibleItemCount - (nextPosition - firstVisibleItem));
	            if (nextSectionPosition > -1) {
	                View nextSectionView = getChildAt(nextSectionPosition - firstVisibleItem);
	                final int bottom = mPinnedSection.view.getBottom() + getPaddingTop();
	                mSectionsDistanceY = nextSectionView.getTop() - bottom;
	                if (mSectionsDistanceY < 0) {
	                    // next section overlaps pinned shadow, move it up
	                    mTranslateY = mSectionsDistanceY;
	                } else {
	                    // next section does not overlap with pinned, stick to top
	                    mTranslateY = 0;
	                }
	            } else {
	                // no other sections are visible, stick to top
	                mTranslateY = 0;
	                mSectionsDistanceY = Integer.MAX_VALUE;
	            }
	        }

	    }

		int findFirstVisibleSectionPosition(int firstVisibleItem, int visibleItemCount) {
			ListAdapter adapter = getAdapter();

	        int adapterDataCount = adapter.getCount();
	        if (getLastVisiblePosition() >= adapterDataCount) return -1; // dataset has changed, no candidate

	        if (firstVisibleItem+visibleItemCount >= adapterDataCount){//added to prevent index Outofbound (in case)
	            visibleItemCount = adapterDataCount-firstVisibleItem;
	        }

			for (int childIndex = 0; childIndex < visibleItemCount; childIndex++) {
				int position = firstVisibleItem + childIndex;
				int viewType = adapter.getItemViewType(position);
				if (isItemViewTypePinned(adapter, viewType)) return position;
			}
			return -1;
		}

		int findCurrentSectionPosition(int fromPosition) {
			ListAdapter adapter = getAdapter();

			if (fromPosition >= adapter.getCount()) return -1; // dataset has changed, no candidate
			
			if (adapter instanceof SectionIndexer) {
				// try fast way by asking section indexer
				SectionIndexer indexer = (SectionIndexer) adapter;
				int sectionPosition = indexer.getSectionForPosition(fromPosition);
				int itemPosition = indexer.getPositionForSection(sectionPosition);
				int typeView = adapter.getItemViewType(itemPosition);
				if (isItemViewTypePinned(adapter, typeView)) {
					return itemPosition;
				} // else, no luck
			}

			// try slow way by looking through to the next section item above
			for (int position=fromPosition; position>=0; position--) {
				int viewType = adapter.getItemViewType(position);
				if (isItemViewTypePinned(adapter, viewType)) return position;
			}
			return -1; // no candidate found
		}

		void recreatePinnedShadow() {
		    destroyPinnedShadow();
	        ListAdapter adapter = getAdapter();
	        if (adapter != null && adapter.getCount() > 0) {
	            int firstVisiblePosition = getFirstVisiblePosition();
	            int sectionPosition = findCurrentSectionPosition(firstVisiblePosition);
	            if (sectionPosition == -1) return; // no views to pin, exit
	            ensureShadowForPosition(sectionPosition,
	                    firstVisiblePosition, getLastVisiblePosition() - firstVisiblePosition);
	        }
		}

		@Override
		public void setOnScrollListener(OnScrollListener listener) {
			if (listener == mOnScrollListener) {
				super.setOnScrollListener(listener);
			} else {
				mDelegateOnScrollListener = listener;
			}
		}

		@Override
		public void onRestoreInstanceState(Parcelable state) {
			super.onRestoreInstanceState(state);
			post(new Runnable() {
				@Override public void run() { // restore pinned view after configuration change
				    recreatePinnedShadow();
				}
			});
		}

		@Override
		public void setAdapter(ListAdapter adapter) {

		    // assert adapter in debug mode
			if (BuildConfig.DEBUG && adapter != null) {
				if (!(adapter instanceof PinnedSectionListAdapter))
					throw new IllegalArgumentException("Does your adapter implement PinnedSectionListAdapter?");
				if (adapter.getViewTypeCount() < 2)
					throw new IllegalArgumentException("Does your adapter handle at least two types" +
							" of views in getViewTypeCount() method: items and sections?");
			}

			// unregister observer at old adapter and register on new one
			ListAdapter oldAdapter = getAdapter();
			if (oldAdapter != null) oldAdapter.unregisterDataSetObserver(mDataSetObserver);
			if (adapter != null) adapter.registerDataSetObserver(mDataSetObserver);

			// destroy pinned shadow, if new adapter is not same as old one
			if (oldAdapter != adapter) destroyPinnedShadow();

			super.setAdapter(adapter);
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
		    super.onLayout(changed, l, t, r, b);
	        if (mPinnedSection != null) {
	            int parentWidth = r - l - getPaddingLeft() - getPaddingRight();
	            int shadowWidth = mPinnedSection.view.getWidth();
	            if (parentWidth != shadowWidth) {
	                recreatePinnedShadow();
	            }
	        }
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			super.dispatchDraw(canvas);

			if (mPinnedSection != null) {

				// prepare variables
				int pLeft = getListPaddingLeft();
				int pTop = getListPaddingTop();
				View view = mPinnedSection.view;

				// draw child
				canvas.save();

				int clipHeight = view.getHeight() +
				        (mShadowDrawable == null ? 0 : Math.min(mShadowHeight, mSectionsDistanceY));
				canvas.clipRect(pLeft, pTop, pLeft + view.getWidth(), pTop + clipHeight);

				canvas.translate(pLeft, pTop + mTranslateY);
				drawChild(canvas, mPinnedSection.view, getDrawingTime());

				if (mShadowDrawable != null && mSectionsDistanceY > 0) {
				    mShadowDrawable.setBounds(mPinnedSection.view.getLeft(),
				            mPinnedSection.view.getBottom(),
				            mPinnedSection.view.getRight(),
				            mPinnedSection.view.getBottom() + mShadowHeight);
				    mShadowDrawable.draw(canvas);
				}

				canvas.restore();
			}
		}

		//-- touch handling methods

	    @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {

	        final float x = ev.getX();
	        final float y = ev.getY();
	        final int action = ev.getAction();

	        if (action == MotionEvent.ACTION_DOWN
	                && mTouchTarget == null
	                && mPinnedSection != null
	                && isPinnedViewTouched(mPinnedSection.view, x, y)) { // create touch target

	            // user touched pinned view
	            mTouchTarget = mPinnedSection.view;
	            mTouchPoint.x = x;
	            mTouchPoint.y = y;

	            // copy down event for eventually be used later
	            mDownEvent = MotionEvent.obtain(ev);
	        }

	        if (mTouchTarget != null) {
	            if (isPinnedViewTouched(mTouchTarget, x, y)) { // forward event to pinned view
	                mTouchTarget.dispatchTouchEvent(ev);
	            }

	            if (action == MotionEvent.ACTION_UP) { // perform onClick on pinned view
	                super.dispatchTouchEvent(ev);
	                performPinnedItemClick();
	                clearTouchTarget();

	            } else if (action == MotionEvent.ACTION_CANCEL) { // cancel
	                clearTouchTarget();

	            } else if (action == MotionEvent.ACTION_MOVE) {
	                if (Math.abs(y - mTouchPoint.y) > mTouchSlop) {

	                    // cancel sequence on touch target
	                    MotionEvent event = MotionEvent.obtain(ev);
	                    event.setAction(MotionEvent.ACTION_CANCEL);
	                    mTouchTarget.dispatchTouchEvent(event);
	                    event.recycle();

	                    // provide correct sequence to super class for further handling
	                    super.dispatchTouchEvent(mDownEvent);
	                    super.dispatchTouchEvent(ev);
	                    clearTouchTarget();

	                }
	            }

	            return true;
	        }

	        // call super if this was not our pinned view
	        return super.dispatchTouchEvent(ev);
	    }

	    private boolean isPinnedViewTouched(View view, float x, float y) {
	        view.getHitRect(mTouchRect);

	        // by taping top or bottom padding, the list performs on click on a border item.
	        // we don't add top padding here to keep behavior consistent.
	        mTouchRect.top += mTranslateY;

	        mTouchRect.bottom += mTranslateY + getPaddingTop();
	        mTouchRect.left += getPaddingLeft();
	        mTouchRect.right -= getPaddingRight();
	        return mTouchRect.contains((int)x, (int)y);
	    }

	    private void clearTouchTarget() {
	        mTouchTarget = null;
	        if (mDownEvent != null) {
	            mDownEvent.recycle();
	            mDownEvent = null;
	        }
	    }

	    private boolean performPinnedItemClick() {
	        if (mPinnedSection == null) return false;

	        OnItemClickListener listener = getOnItemClickListener();
	        if (listener != null && getAdapter().isEnabled(mPinnedSection.position)) {
	            View view =  mPinnedSection.view;
	            playSoundEffect(SoundEffectConstants.CLICK);
	            if (view != null) {
	                view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_CLICKED);
	            }
	            listener.onItemClick(this, view, mPinnedSection.position, mPinnedSection.id);
	            return true;
	        }
	        return false;
	    }

	    public static boolean isItemViewTypePinned(ListAdapter adapter, int viewType) {
	        if (adapter instanceof HeaderViewListAdapter) {
	            adapter = ((HeaderViewListAdapter)adapter).getWrappedAdapter();
	        }
	        return ((PinnedSectionListAdapter) adapter).isItemViewTypePinned(viewType);
	    }
	
	    // TODO 刷新
	    private View headView;
		private int height;
		private int downY;
	    
	    private final int PULL_REFRESH=0;//下拉刷新
		private final int REALEASE_REFRESH=1;//松开刷新
		private final int REFRESHING=2;//正在刷新
		private int currentState=PULL_REFRESH;//当前状态
		//初始化head布局的变量
		private ImageView iv_arrow;
		private ProgressBar bar_rotate;
		private TextView tv_time,tv_state;
		
		//定义旋转动画
		private RotateAnimation up,down;
		
		private boolean isLoading=false;//当前是否在加载
	
		private void init() {
			initHeadView();
			initRotateAnimation();
			initFootView();
		}
	
		/**
		 * 初始化旋转动画
		 */
		private void initRotateAnimation() {
			//向上旋转
			up=new RotateAnimation(0, -180,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f, 
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			//动画持续的时间
			up.setDuration(300);
			up.setFillAfter(true);
			//向下旋转
			down=new RotateAnimation(-180, -360,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f, 
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			//动画持续的时间
			down.setDuration(300);
			down.setFillAfter(true);
		}
		
		private void initHeadView() {
			headView = View.inflate(getContext(), R.layout.head,null);
			
			iv_arrow = (ImageView) headView.findViewById(R.id.iv_arrow);
			bar_rotate =(ProgressBar) headView.findViewById(R.id.bar_rotate);
			tv_time = (TextView) headView.findViewById(R.id.tv_time);
			tv_state = (TextView) headView.findViewById(R.id.tv_state);
			
			headView.measure(0, 0);
			height = headView.getMeasuredHeight();
			headView.setPadding(0, -height,0,0);
			addHeaderView(headView);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY=(int) ev.getY();
				break;

			case MotionEvent.ACTION_MOVE:
				int deltaY=(int) (ev.getY()-downY);
				int paddingTop=-height+deltaY;
				//当课件条目是0的时候才可以下拉刷新
				if(paddingTop>-height&&getFirstVisiblePosition()==0){
					headView.setPadding(0,paddingTop,0,0);
					if(paddingTop>=0&&currentState==PULL_REFRESH){
						//从下拉刷新进入松开刷新状态
						currentState=REALEASE_REFRESH;
						refreshHeadView();
					}else if(paddingTop<=0&&currentState==REALEASE_REFRESH){
						//进入下拉刷新状态
						currentState=PULL_REFRESH;
						refreshHeadView();
					}
					
					return true;//拦截TouchMove，不然ListView处理该事件，会造成ListView无法滑动
				}
				break;
			case MotionEvent.ACTION_UP:
				if(currentState==PULL_REFRESH){
					headView.setPadding(0, -height,0,0);
				}else if(currentState==REALEASE_REFRESH){
					//headView完全显示
					headView.setPadding(0,0,0,0);
					currentState=REFRESHING;
					refreshHeadView();
					
					if(listener!=null){
						listener.onPullRefresh();
					}
				}
				break;
			}
			return super.onTouchEvent(ev);
		}
	
		/**
		 * 根据currentState来更新headView
		 */
		private void refreshHeadView(){
			switch (currentState) {
			case PULL_REFRESH:
				tv_state.setText("下拉刷新");
				iv_arrow.startAnimation(down);
				break;
			case REALEASE_REFRESH:
				tv_state.setText("松开刷新");
				iv_arrow.startAnimation(up);
				break;
			case REFRESHING:
				//停止动画
				iv_arrow.clearAnimation();
				//隐藏箭头
				iv_arrow.setVisibility(View.INVISIBLE);
				//显示bar_rotate
				bar_rotate.setVisibility(View.VISIBLE);
				tv_state.setText("正在刷新,请稍后");
				break;
			}
		}
		
		/**
		 * 完成刷新操作，重置状态
		 */
		public void completeRefresh(){
			if(isLoading){
				//重置footView
				footView.setPadding(0,0,0,-footViewHeight);
				isLoading=false;
			}else{
				//重置headView
				headView.setPadding(0, -height,0,0);
				currentState=PULL_REFRESH;
				bar_rotate.setVisibility(View.INVISIBLE);
				iv_arrow.setVisibility(View.VISIBLE);
				tv_state.setText("下拉刷新");
				tv_time.setText("最后刷新："+getCurrentTime());
			}
		}
		
		/**
		 * 获取当前时间
		 * @return
		 */
		@SuppressLint("SimpleDateFormat") 
		private String getCurrentTime(){
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.format(new Date());
		}
		
		private OnRefreshListener listener;
		private View footView;
		private int footViewHeight;

		public void setOnRefreshListener(OnRefreshListener listener) {
			this.listener = listener;
		}

		public interface OnRefreshListener{
			void onPullRefresh();
			void onLoadingMore();
		}
		/**
		 * 初始化FootView
		 */
		private void initFootView() {
			footView = View.inflate(getContext(), R.layout.bottom,null);
			footView.measure(0, 0);
			footViewHeight = footView.getMeasuredHeight();
//			Log.e("footViewHeight","++++"+footViewHeight);
			footView.setPadding(0, 0, 0, -footViewHeight);
			addFooterView(footView);
		}
		

}
