package app.com.example.teddy.labb2_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Teddy on 2016-11-29.
 */

public class NineMensMorrisGameLayout extends View {
    private Paint blackPaintBrush;
    private Paint blackPaintBrushFill;
    private Paint whitePaintBrushFill;
    private int screenWidth;
    private int screenHeight;
    private float[][] dotsXAndY;
    private float[][] player1Checkers;
    private float[][] player2Checkers;
    Rect outerRectangle ;
    Rect innerRect ;
    Rect middleRect ;
    //lines
    private int outerLeft ;
    private int outerTop ;
    private int outerRight ;
    private  int outerBottom ;
    private int middleLeft;
    private  int middleTop ;
    private  int middleRight ;
    private int middleBottom;
    private  int innerLeft ;
    private int innerTop ;
    private int innerRight ;
    private int innerBottom ;
    //
    float verticalStartAndStopX ;
    float horizontalStartAndStopY ;
    //
    int screenConstant;
    //
    boolean mainPhaseW;
    boolean mainPhaseB;


    public NineMensMorrisGameLayout(Context context) {
        super(context);
        setBackgroundResource(R.drawable.ninemenboard);
        player1Checkers = new float[9][2];
        player2Checkers = new float[9][2];
        dotsXAndY = new float[26][2];
        for(float[] checker : player1Checkers)
        {
            checker[1] = -1;
            checker[0] = -1;
        }
        for(float[] checker : player2Checkers)
        {
            checker[1] = -1;
            checker[0] = -1;
        }
        initScreenSize();
    }


    private void initScreenSize() {
        this.screenHeight = getHeight();
        this.screenWidth = getWidth();
    }

    public void initCheckersDefaultPos()
    {

        int highestSize = Math.max((int)getHeightSize(), (int)getWidthSize());
        int screenConstant = Math.min((int)getHeightSize(), (int)getWidthSize());
        int lenght = player1Checkers.length-1;
        if(screenWidth < screenHeight)
        {
            if(!mainPhaseB)
            {
                player1Checkers[lenght][0] = (float)(screenConstant*0.2);
                player1Checkers[lenght][1] = (float) (highestSize*0.65);
            }
            if (!mainPhaseW)
            {
                player2Checkers[lenght][0] = (float)(screenConstant*0.8);
                player2Checkers[lenght][1] = (float) (highestSize*0.65);
            }
        }
        else {
            if(!mainPhaseB)
            {
                player1Checkers[lenght][0] = (float)(screenWidth*0.65);
                player1Checkers[lenght][1] = (float) (screenHeight*0.8);
            }

            if(!mainPhaseW)
            {
                player2Checkers[lenght][0] = (float)(highestSize*0.65);
                player2Checkers[lenght][1] = (float) (screenConstant*0.2);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        screenHeight = getHeight();
        screenWidth = getWidth();

        initCheckersDefaultPos();

        //Log.v("[height", "" + getHeight());
        //Log.v("[width", "" + getWidth());
        screenConstant = Math.min(screenHeight, screenWidth);
        initPaint(screenConstant);
        initLines();
        initBoardDots();
        drawNineBoard(screenConstant, canvas);
    }



    private void initPaint(int tmp)
    {
        blackPaintBrush = new Paint();
        blackPaintBrush.setColor(Color.BLACK);
        blackPaintBrush.setStyle(Paint.Style.STROKE);
        blackPaintBrush.setStrokeWidth(new Double(tmp*0.01).intValue());

        blackPaintBrushFill =  new Paint();
        blackPaintBrushFill.setColor(Color.BLACK);
        blackPaintBrushFill.setStyle(Paint.Style.FILL);
        blackPaintBrushFill.setStrokeWidth(new Double(tmp*0.02).intValue());

        whitePaintBrushFill =  new Paint();
        whitePaintBrushFill.setColor(Color.WHITE);
        whitePaintBrushFill.setStyle(Paint.Style.FILL);
    }

    private void drawNineBoard(int screenConstant, Canvas canvas)
    {
        canvas.drawRect(outerRectangle, blackPaintBrush);
        canvas.drawRect(middleRect, blackPaintBrush);
        canvas.drawRect(innerRect, blackPaintBrush);
        //vertical line:
        canvas.drawLine(verticalStartAndStopX, outerTop, verticalStartAndStopX, outerBottom, blackPaintBrush);
        canvas.drawLine(outerLeft, horizontalStartAndStopY, outerRight, horizontalStartAndStopY, blackPaintBrush);
        //draw circles on board:
        //24 circles:
        for(int i = 0; i< dotsXAndY.length-2; i++)
        {
            canvas.drawCircle(dotsXAndY[i][0], dotsXAndY[i][1],(float) (screenConstant*0.01), blackPaintBrush);
        }


        for(int i=0;i<player1Checkers.length;i++)
        {

            if(player1Checkers[i][0] > 0)
            {
                //Log.v("drawing","=" +player1Checkers[i][0] + "index =" + i);
                canvas.drawCircle( player1Checkers[i][0] , player1Checkers[i][1], (float)(screenConstant*0.05),blackPaintBrushFill);
            }
            if(player2Checkers[i][0]>0)
            {
                //Log.v("drawing","WHIT=" +player2Checkers[i][0] + "index =" + i);
                canvas.drawCircle( player2Checkers[i][0] ,player2Checkers[i][1], (float)(screenConstant*0.05),whitePaintBrushFill);
            }
        }
    }

    private void initLines()
    {
        //OuterRect:
        outerRectangle = new Rect();
        //lef, top, right, bottom
        outerLeft = new Double((screenConstant * 0.1)).intValue();
        outerTop = new Double((screenConstant * 0.1)).intValue();
        outerRight = screenConstant - (new Double((screenConstant * 0.1)).intValue());
        outerBottom = screenConstant - (new Double((screenConstant * 0.1)).intValue());

        outerRectangle.set(outerLeft, outerTop, outerRight, outerBottom);
         verticalStartAndStopX = (float) ((outerRight + outerLeft) / 2);
         horizontalStartAndStopY = (float) ((outerBottom + outerTop) / 2);
        //middle rect:
        double middlePercentage = 0.22;
        middleRect = new Rect();
        middleLeft = new Double((screenConstant * middlePercentage)).intValue();
        middleTop = new Double((screenConstant * middlePercentage)).intValue();
        middleRight = screenConstant - (new Double((screenConstant * middlePercentage)).intValue());
        middleBottom = screenConstant - (new Double((screenConstant * middlePercentage)).intValue());
        middleRect.set(middleLeft, middleTop, middleRight, middleBottom);


        //inner Rect:
        double innerPercentage = 0.34;
        innerRect = new Rect();
        innerLeft = new Double((screenConstant * innerPercentage)).intValue();
        innerTop = new Double((screenConstant * innerPercentage)).intValue();
        innerRight = screenConstant - (new Double((screenConstant * innerPercentage)).intValue());
        innerBottom = screenConstant - (new Double((screenConstant * innerPercentage)).intValue());
        innerRect.set(innerLeft, innerTop, innerRight, innerBottom);
        //Log.v("outer:" , "outerB="+outerBottom + "middleB=" + middleBottom);
    }

    private void initBoardDots()
    {
        //cx, cy, radius, paint;
        float middleX = verticalStartAndStopX;
        float middleY = horizontalStartAndStopY;

                float[][] cyAndcyTemp = { {outerLeft,outerTop},{middleX,outerTop}, {outerRight,outerTop},
                {outerRight,middleY}, { outerBottom,outerRight},
                {middleX,outerBottom},{outerLeft,outerBottom},
                {outerLeft,middleY},

                {middleLeft,middleTop},{middleX,middleTop}, {middleRight,middleTop},
                {middleRight,middleY}, { middleBottom,middleRight},
                {middleX,middleBottom},{middleLeft,middleBottom},
                {middleLeft,middleY},

                {innerLeft,innerTop},{middleX,innerTop}, {innerRight,innerTop},
                {innerRight,middleY}, {innerBottom,innerRight},
                {middleX,innerBottom},{innerLeft,innerBottom},
                {innerLeft,middleY},{0,0} ,{0,0}
        };
        dotsXAndY = cyAndcyTemp;
        initDefaultCheckerPos();
    }

    public int getAvailablePos(float posX, float posY) {

        for (int i=0;i<dotsXAndY.length-1;i++)
        {
            if(posX <= (dotsXAndY[i][0] +30) && posX >= (dotsXAndY[i][0] - 30 )&&
                posY>= dotsXAndY[i][1]-30 && posY <= dotsXAndY[i][1]+30)
            {
                return i;
            }
        }
        return -1;
    }

    public float getWidthSize()
    {
        return getWidth();
    }
    public float getHeightSize()
    {
        return getHeight();
    }

    public void setMainPhaseWhitePlayer()
    {
        this.mainPhaseW = true;
    }
    public void setMainPhaseBlackPlayer()
    {
        this.mainPhaseB = true;
    }


    public void initDefaultCheckerPos()
    {
        int width= getWidth();
        int height = getHeight();
        int highestSize = Math.max(width, height);
        int screenConstant = Math.min(height, width);
       // Log.v("12413Wid","="+width +" 231He="+height);

        if(width < height)
        {
            dotsXAndY[dotsXAndY.length-1][0] = (float)(screenConstant*0.2);
            dotsXAndY[dotsXAndY.length-1][1] = (float) (highestSize*0.65);

            dotsXAndY[dotsXAndY.length-2][0] = (float)(screenConstant*0.8);
            dotsXAndY[dotsXAndY.length-2][1] = (float) (highestSize*0.65);
        }
        else {
            dotsXAndY[dotsXAndY.length-1][0] = (float)(width*0.65);
            dotsXAndY[dotsXAndY.length-1][1] = (float) (height*0.8);

            dotsXAndY[dotsXAndY.length-2][0] = (float)(highestSize*0.65);
            dotsXAndY[dotsXAndY.length-2][1] = (float) (screenConstant*0.2);
        }
    }

    public void drawCheckers(int[] whiteCheckers , int[] blackCheckers) {

        //här är rätt
        initLines();
        initBoardDots();
        initDefaultCheckerPos();
        for(float[] checker : player1Checkers)
        {
            checker[1] = -1;
            checker[0] = -1;
        }
        for(float[] checker : player2Checkers)
        {
            checker[1] = -1;
            checker[0] = -1;
        }
        for(int i=0;i<whiteCheckers.length;i++)
        {

            if(whiteCheckers[i] >= 0)
            {
                player2Checkers[i][0] = dotsXAndY[whiteCheckers[i]][0];
                player2Checkers[i][1] = dotsXAndY[whiteCheckers[i]][1];
                //Log.v("inDrawChecks", "wX="+ player2Checkers[i][0] + "wY=" +player2cheskYPos);
            }
            if(blackCheckers[i] >=0)
            {
                //Log.v("i=",""+i+" colorBLACK =" + blackCheckers[i]);
                player1Checkers[i][0] = dotsXAndY[blackCheckers[i]][0];
                player1Checkers[i][1] = dotsXAndY[blackCheckers[i]][1];
            }
        }
        invalidate();
    }

    public void updateScreen(int[] whiteCheckers, int[] blackCheckers) {

        Log.v("!!!!HEIGHT= ", " ="+getHeight()+" WIDHT="+getWidth());
        Log.v("!!!!h= ", " ="+screenHeight+" w=" +screenWidth);
        drawCheckers(whiteCheckers, blackCheckers);
    }
}
