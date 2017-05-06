package com.timer.jike.timemanager.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


public class DrawView extends View {
    //定义所需的变量和其他
    //获取时间
    private Time NowTime;
    //时间秒
    private int Stime0;
    private int Stime1;
    //时间分
    private int Mtime0;
    private int Mtime1;
    //时间时
    private int Htime0;
    private int Htime1;
    private int Htime2;
    //时角度
    private int HAngle;
    //分角度
    private int MAngle;
    //秒角度
    private int SAngle;
    //设置字
    @SuppressWarnings("unused")
    private String Text;
    //初始化
    private Handler mainHandler = null;
    private int year;
    private int mouth;
    private int date;
    //初始化
    int x = 0;
int y = 0;
/**事件触发时间**/
Long mActionTime = 0L;
//双击事件判断
//计算点击的次数
private int count = 0;
//第一次的点击
private long firstClick = 0;
//第二次点击
private long lastClick = 0;
//用于判断按下还是移动还是抬起
int v1 = 0;
//用于判断按下的区域是否为最小的圆的区域
int v2 = 0;
//它的值将决定将要执行的动作
int z1 = 1;
//定义画笔的宽度
    int w1 = 10;
    int w2 = 1;

    //子空间的宽和高
    private int circleWidth, circleHeight;

    public DrawView(Context context) {
        super(context);
    }

    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //确定圆心（a,b）
        @SuppressWarnings("unused")
        int a,b,c,d,e,g,h;
        //整数h用来圆的高度。g为圆的半径单位，d为显示的宽，e为显示的高。
        a = b = c = d = e = g = 0;
        double f = 0;
        circleHeight = getHeight();
        circleWidth = getWidth();
        a = circleWidth / 2;
        b = circleHeight / 2;
        h = b;
        //比较是触摸点与中心点的坐标的大小，并求出两点的距离
        //距离用于判断所做的动作
        if(a > x && b > y)
        {
            f =  (int) Math.sqrt ((a - x) * (a - x)  + (h - y) * (h - y));
        }
        else if(a > x && y > b)
        {
            f =  (int) Math.sqrt ((x - a) * (x - a) + (y - h) * (y - h));
        }
        else if(x > a && b > y)
        {
            f =  (int) Math.sqrt ((x - a) * (x - a) + (h - y) * (h - y));
        }
        else if(x > a && y > b)
        {
            f =  (int) Math.sqrt ((x - a) * (x - a) + (y - h) * (y - h));
        }
        //求出哪个是长，哪个是宽（为以后做横屏做准备）
        if(a > b)
        {
            d = b;
            g = (int) (d / 4.5);
            e = a;
            c = g;
        }
        else
        {
            d = a;
            g = (int) (a / 4.5);
            e = b;
            c = g;
        }
        //获取时间
        NowTime = new Time();
        NowTime.setToNow();
        Stime1 = NowTime.second;
        Stime0 = Stime1;
        System.out.println(Stime1);
SAngle = 6 * Stime0;
Mtime0 = Mtime1;
Mtime1 =NowTime.minute;
System.out.println(Mtime1);
MAngle = 6 * Mtime0;
Htime1 = NowTime.hour;
System.out.println(Htime1);
date=NowTime.monthDay;
year=NowTime.year;
mouth=NowTime.month + 1;
// 如果大于12点，就减去12，否则不减去。（用于绘制小时圆）
if(Htime1 > 12)
{
            Htime2 = Htime1 - 12;
}
else
{
            Htime2 = Htime1;
}
Htime0 = Htime2;
HAngle = 30 * Htime0;
//获取时间完成
        //画圆
//定义画笔
        Paint paint = new Paint();
        //是否抗锯齿
        paint.setAntiAlias(true);
        //使用图像抖动处理
        paint.setDither(true);
        //画笔颜色
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        //画笔粗细
        paint.setStrokeWidth(w1);
        //设置画笔为圆形
        paint.setStrokeCap(Paint.Cap.ROUND);

        //加监事件，通过判断f的大小实现事件的跳转
        if(v1 == 1)
        {
            if(f < g)
            {
                v2 = 1;
                //初始化数据
                //双击事件，用于启动手电筒
                //初始化程序
                if(count == 2)
                {
                    count = 0;
                    firstClick = 0;
                    lastClick = 0;
                }
                //条件成立的话，给第二个值赋值
                if(count == 1)
                    {
                        lastClick = System.currentTimeMillis();
                        //调用双击判断函数
                        dblclick();
                        //初始化数据
                        count = 0;
                        firstClick = 0;
                        lastClick = 0;
                    }
                //条件成立的话，给第一个值赋值
                else if(count == 0)
                    {
                        TimerTask task = new TimerTask() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                            count = 0;
                            firstClick = 0;
                            lastClick = 0;
                            }

                        };
                        Timer time = new Timer();
                        time.schedule(task, 500, 500);
                        firstClick = System.currentTimeMillis();
                        count = 1;
                    }
            }
            else
            {
                v2 = 0;
            }
        }
        if(v1 == 2)
        {
            if(v2 == 1)
            {
                if(f < g)
                {
                    SAngle = 0;
                    MAngle = 0;
                    HAngle = 0;
                    w1 = 80;
                    paint.setStrokeWidth(w2);
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(d, h, g + g / 2, paint);
                    canvas.drawCircle(d, h, g * 2 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 3 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 4 + g / 2, paint);
                    z1 = 0;
                }
                if(f > g && f < 2 * g )
                {
                    SAngle = 0;
                    MAngle = 0;
                    HAngle = 360;
                    w1 = 80;
                    paint.setStrokeWidth(w2);
                    canvas.drawCircle(d, h, g + g / 2, paint);
                    canvas.drawCircle(d, h, g * 2 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 3 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 4 + g / 2, paint);
                    z1 = 2;

                }
                if(f > 2 * g && f < 3 * g )
                {
                    SAngle = 0;
                    MAngle = 360;
                    HAngle = 0;
                    w1 = 80;
                    paint.setStrokeWidth(w2);
                    paint.setColor(Color.WHITE);
                    canvas.drawCircle(d, h, g + g / 2, paint);
                    canvas.drawCircle(d, h, g * 2 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 3 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 4 + g / 2, paint);
                    z1 = 3;
                }
                if(f > 3 * g)
                {
                    SAngle = 360;
                    MAngle = 0;
                    HAngle = 0;
                    w1 = 80;
                    paint.setStrokeWidth(w2);
                    canvas.drawCircle(d, h, g + g / 2, paint);
                    canvas.drawCircle(d, h, g * 2 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 3 + g / 2, paint);
                    canvas.drawCircle(d, h, g * 4 + g / 2, paint);
                    z1 = 4;
                }
            }
        }
        if(v1 == 0)
        {
            if(v2 == 1)
            {
                if(f < g)
                {
                    Stime0 = 60;
                    SAngle = 6 * Stime0;
                    Mtime0 = 60;
                    MAngle = 6 * Mtime0;
                    Htime0 = 12;
                    HAngle = 30 * Htime0;
                    w1 = 80;
                    z1 = 1;
                }
                if(f > g && f < 2 * g )
                {
                    phone();
                    //添加电话事件
                }
                if(f > 2 * g && f < 3 * g )
                {
                    ssm();
                    //添加短信事件
                }
                if(f > 3 * g)
                {
                    home();
                    x = d;
                    y = e;
                }
                Stime0 = Stime1;
                SAngle = 6 * Stime0;
                Mtime0 = Mtime1;
                MAngle = 6 * Mtime0;
                Htime0 = Htime2;
                HAngle = 30 * Htime0;
                w1 = 10;
            }
        }
paint.setColor(Color.WHITE);
        paint.setAlpha(100);
paint.setStrokeWidth(w2);
//辅助线
/*
//竖线
canvas.drawLine(d, 0, d, 2 * e, paint);
//横线
canvas.drawLine(0, e, 2 * d, e, paint);
canvas.drawLine(0, e - d, 2 * d, e - d, paint);
//斜线
canvas.drawLine(0, e - d, 2 * d, e + d, paint);
canvas.drawLine(2 * d, e - d, 0, e + d, paint);
*/
        //redtf的四个参数： 起点轴的x，y坐标，宽和高
paint.setStrokeWidth(w1);
        paint.setColor(Color.GREEN);
        paint.setAlpha(255);
        RectF rectf=new RectF( g / 2,e - d + g / 2 , 2 * d - g / 2, ( 2 * d )+( e - d ) - g / 2);
canvas.drawArc(rectf, 270, SAngle, false, paint);
paint.setAlpha(255);
RectF Mrectf=new RectF( g / 2 * 3, e - d + g / 2 * 3 , 2 * d - g / 2 * 3, ( 2 * d ) + ( e - d ) - g / 2 * 3);
canvas.drawArc(Mrectf, 270, MAngle, false, paint);
paint.setAlpha(255);
RectF Hrectf=new RectF( g / 2 * 5, e - d + g / 2 * 5 , 2 * d - g / 2 * 5, ( 2 * d ) + ( e - d ) - g / 2 * 5);
canvas.drawArc(Hrectf, 270,HAngle, false, paint);
paint.setStrokeWidth(w2);
paint.setColor(Color.WHITE);
paint.setTextSize(30);
        paint.setStyle(Paint.Style.FILL);
        //刷新周期
        this.postInvalidateDelayed(10);
if(z1 ==  0)
{
        canvas.drawText("滑动进入：=》 ", d - 90, e + 10, paint);
        canvas.drawText("电话", d + 130, h + 10, paint);
        canvas.drawText("短信", d + 210, h + 10, paint);
        canvas.drawText("桌面", d + 290, h + 10, paint);
        //this.postInvalidateDelayed(1000);
}
if(z1 ==  1)
{
canvas.drawText("点击时间进入解锁菜单", d - (g * 2 - g / 8), h * 2 - (g * 2 - g / 8) , paint);
paint.setColor(Color.WHITE);
//设置为亚像素边缘
paint.setSubpixelText(true);
//设置阴影
paint.setShadowLayer(15 ,5,5,Color.BLACK);

        canvas.drawText(year+":"+mouth+":"+date, d - 70, h - 10, paint);
        canvas.drawText(NowTime.hour+":"+NowTime.minute+":"+NowTime.second, d - 60, h + 30, paint);
        //this.postInvalidateDelayed(1000);
}
if(z1 ==  2)
{
        canvas.drawText("拨打电话", d -60, h + 10, paint);
        //this.postInvalidateDelayed(1000);
}
if(z1 ==  3)
{
        canvas.drawText("发送短信", d - 60, h + 10, paint);
        //this.postInvalidateDelayed(1000);
}
if(z1 ==  4)
{
        canvas.drawText("解锁屏幕", d - 60, h + 10, paint);
        //this.postInvalidateDelayed(1000);
}

paint.setColor(Color.WHITE);
paint.setStrokeWidth(1);
/*
canvas.drawText("当前X坐标："+x, 0, 40, paint);
canvas.drawText("当前Y坐标："+y, 0, 80, paint);
canvas.drawText("当前A坐标："+a, 0, 120, paint);
canvas.drawText("当前B坐标："+b, 0, 160, paint);
canvas.drawText("当前f坐标："+f, 0, 200, paint);
canvas.drawText("当前v坐标："+v1, 0, 240, paint);
canvas.drawText("当前第一次点击时间："+firstClick, 0, 280, paint);
canvas.drawText("当前第二次点击时间："+lastClick, 0, 320, paint);
canvas.drawText("当前g坐标："+c, 0, 360, paint);
*/
super.onDraw(canvas);

        }

    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        x = (int) event.getX();
        y = (int) event.getY();
        switch (action) {
        // 触摸按下的事件
        case MotionEvent.ACTION_DOWN:
        {
        v1 = 1;
        Log.v("test", "ACTION_DOWN");
        }
        break;
        // 触摸移动的事件
        case MotionEvent.ACTION_MOVE:
        {
        v1 = 2;

        Log.v("test", "ACTION_MOVE");
        }
        break;
        // 触摸抬起的事件
        case MotionEvent.ACTION_UP:
        {
            v1 = 0;
        Log.v("test", "ACTION_UP");
        }
        break;
        }   /**得到事件触发时间**/
        //mActionTime = event.getEventTime();
        /** 通知UI线程刷新屏幕 **/
        //postInvalidate();
        //return super.onTouchEvent(event);

        return true;
    }
    /*
     * if 半径 <= 88,无动作；
     * if 半径 <= 168 && 半径 >= 88, 启动电话；
     * if 半径 <= 248 && 半径 >= 168, 启动短信；
     * if 半径 <= 328 && 半径 >= 248, 解锁；
     * 按下以后如果小于88，v2 = 1
    */

    public void dblclick()
    {	//下面的需要代码需要重新构造
        //双击事件成立的话，打开手电
        if(lastClick - firstClick < 500)
        {
            troch();
        }
        //条件不成立的话，初始化数据
        else
            count = 2;
     }

    public void home()
    {
        //resetViewState();
       //结束我们的主Activity界面
//        mainHandler.obtainMessage(MEActivity.MSG_LOCK_SUCESS).sendToTarget();

     }
    public void phone()
    {
        //resetViewState();
       //结束我们的主Activity界面
//        mainHandler.obtainMessage(MEActivity.MY_PHONE).sendToTarget();

     }
    public void ssm()
    {
        //resetViewState();
       //结束我们的主Activity界面
//        mainHandler.obtainMessage(MEActivity.MY_SSM).sendToTarget();

     }
    public void troch()
    {
        //resetViewState();
       //结束我们的主Activity界面
//        mainHandler.obtainMessage(MEActivity.MY_TROCH).sendToTarget();

     }
    public  void setMainHandler(Handler handler){
        //activity所在的Handler对象
        mainHandler = handler;
    }

}