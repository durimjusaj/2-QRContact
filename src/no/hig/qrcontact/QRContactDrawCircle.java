package no.hig.qrcontact;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class QRContactDrawCircle extends View {

	public float x = 100;
	public float y = 100;
	private float r = 5;

	public float xMax = 100;
	public float yMax = 100;

	private float xVelocity = 0;
	private float yVelocity = 0;
	Paint paint;

	private Canvas canvas;

	public QRContactDrawCircle(Context con, float r) {
		super(con);
		this.r = r;
		canvas = new Canvas();

		paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.RED);

	}

	public void setCords(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setX(float x) {

		xVelocity += (x * 0.666f);
		this.x -= (xVelocity / 2) * 0.666f;

		if (this.x > xMax+3) {
			this.x = xMax+3;
		} else if (this.x < 10) {
			this.x = 10;
		}

	}

	public void setY(float y) {

		yVelocity += (y * 0.666f);
		this.y += (yVelocity / 2) * 0.666f;

		if (this.y > yMax - 193) {
			this.y = yMax - 193;
		} else if (this.y < 10) {
			this.y = 10;
		}

	}

	public Canvas getCanvas() {
		canvas.rotate(0, x, y);
		return canvas;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawCircle(x, y, r, paint);
		invalidate();
	}

}
