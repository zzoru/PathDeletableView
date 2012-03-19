package com.zzoru.pathview;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PathDeletableView extends View {


	private boolean isDeleteMode = false;
	private CopyOnWriteArrayList <Path> mListPaths = new CopyOnWriteArrayList <Path>();

	private Bitmap  mBitmap;
	private Canvas  mCanvas;
	private Path    mPath;
	private Paint   mBitmapPaint;
	private Paint       mPaint;

	public PathDeletableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFFF0000);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0xFFAAAAAA);

		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);		
		canvas.drawPath(mPath, mPaint);
	
	}
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}
	private void touch_move(float x, float y) {
		if(!isDeleteMode){
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;
			}
		}
	}
	private void touch_up() {
		if(!isDeleteMode){
			mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			mListPaths.add(new Path(mPath));
			for(Path path : mListPaths){
				mCanvas.drawPath(path, mPaint);
			}

			mPath.reset();
		}
		else{
			
			
			/* タッチした部分に入っているPathの中で一番古いやつ削除  
			for(Path path : mListPaths){
				RectF bounds = new RectF();
				path.computeBounds(bounds, false);
				if(bounds.contains(mX, mY)){
					mListPaths.remove(path);	
					break;
				}
			}
			*/
			

			/* タッチした部分に入っているPathの中で一番新しいやつ削除  */
			int listsize = mListPaths.size();
			for(int i=listsize-1; i>=0; i--){
				RectF bounds = new RectF();
				Path path = mListPaths.get(i);
				path.computeBounds(bounds, false);
				if(bounds.contains(mX, mY)){
					mListPaths.remove(path);	
					break;
				}
			}
			
			/* 描き直し */
			mCanvas.drawColor(0xFFAAAAAA);
			mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);		
			for(Path path : mListPaths){
				mCanvas.drawPath(path, mPaint);
			}
			
			
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}  


	public void setDeleteMode(boolean set){
		isDeleteMode = set;
	}

}
