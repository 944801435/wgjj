package com.uav.base.util.spacial;

import java.util.ArrayList;

import com.uav.base.util.spacial.BufferLineEntity;
import com.uav.base.util.spacial.Point;

public class BufferLine
{
    private BufferLineEntity genLineBufferSingleLine(Point beginPoint, Point endPoint, double bufferWidth)
    {
        BufferLineEntity bufferlineEntity = new BufferLineEntity(beginPoint, endPoint);

        double l = bufferWidth / 2; // 点到直线距离

        double x1 = beginPoint.x; // A点X坐标
        double y1 = beginPoint.y; // A点Y坐标
        double x2 = endPoint.x; // B点X坐标
        double y2 = endPoint.y; // B点Y坐标

        double k = 0.000; // 点A，B所在直线斜率
        double n = 0.000; // 点A，C所在直线 y=-x/k+n的常量n
        double a = 0.000; // 距离一元二次方程的三个系数中的a
        double b = 0.000; // 距离一元二次方程的三个系数中的b
        double c = 0.000; // 距离一元二次方程的三个系数中的c

        if ( x1 - x2 == 0 && y1 - y2 == 0 )
        {
            return bufferlineEntity;
        }

        if ( x1 - x2 != 0.0 && y1 - y2 == 0.0 )
        {
            bufferlineEntity.ptFS.x = (x1);
            bufferlineEntity.ptFS.y = (y1 + l);
            bufferlineEntity.ptSS.x = (x2);
            bufferlineEntity.ptSS.y = (y2 + l);
            bufferlineEntity.ptFE.x = (x1);
            bufferlineEntity.ptFE.y = (y1 - l);
            bufferlineEntity.ptSE.x = (x2);
            bufferlineEntity.ptSE.y = (y2 - l);
        }
        else if ( x1 - x2 == 0.0 && y1 - y2 != 0.0 )
        {
            bufferlineEntity.ptFS.x = (x1 + l);
            bufferlineEntity.ptFS.y = (y1);
            bufferlineEntity.ptSS.x = (x2 + l);
            bufferlineEntity.ptSS.y = (y2);
            bufferlineEntity.ptFE.x = (x1 - l);
            bufferlineEntity.ptFE.y = (y1);
            bufferlineEntity.ptSE.x = (x2 - l);
            bufferlineEntity.ptSE.y = (y2);
        }
        else if ( x1 - x2 != 0.0 && y1 - y2 != 0.0 )
        {
            k = (y1 - y2) * 1.0 / (x1 - x2);
            n = y1 + x1 / k;
            a = 1 + 1 / Math.pow(k, 2);
            b = 2 * y1 / k - 2 * x1 - 2 * n / k;
            c = Math.pow(y1, 2) + Math.pow(x1, 2) + Math.pow(n, 2) - Math.pow(l, 2) - 2 * n * y1;
            double x = 0.0;
            double y = 0.0;
            x = (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
            y = -x / k + n;
            bufferlineEntity.ptFS.x = x;
            bufferlineEntity.ptFS.y = y; // A端第一个点设置完成

            x = (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
            y = -x / k + n;
            bufferlineEntity.ptFE.x = x;
            bufferlineEntity.ptFE.y = y; // A端第二个点设置完成

            // 下面开始进行B端两个点的坐标运算

            // k值不曾发生变化，故此处不进行任何设置
            n = y2 + x2 / k;
            // a值不曾发生变化，故此处不进行任何设置
            b = 2 * y2 / k - 2 * x2 - 2 * n / k;
            c = Math.pow(y2, 2) + Math.pow(x2, 2) + Math.pow(n, 2) - Math.pow(l, 2) - 2 * n * y2;

            x = (-b + Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
            y = -x / k + n;

            bufferlineEntity.ptSS.x = x;
            bufferlineEntity.ptSS.y = y; // B端第一个点设置完成

            x = (-b - Math.sqrt(Math.pow(b, 2) - 4 * a * c)) / (2 * a);
            y = -x / k + n;
            bufferlineEntity.ptSE.x = (x);
            bufferlineEntity.ptSE.y = (y); // B端第二个点设置完成
        }

        /********************************************************************************/
        if ( beginPoint.x > endPoint.x )
        {
            if ( beginPoint.y > endPoint.y )
            {
                double tgResult = (bufferlineEntity.ptFE.y - beginPoint.y) / (bufferlineEntity.ptFE.x - beginPoint.x);
                double alpha = Math.atan(tgResult) * 180 / Math.PI;
                double beta = alpha + 180;

                bufferlineEntity.angleFS = alpha;
                bufferlineEntity.angleFE = beta;
                bufferlineEntity.angleSS = alpha + 180.0;
                bufferlineEntity.angleSE = beta + 180;
            }
            else if ( beginPoint.y < endPoint.y )
            {
                double tgResult = (bufferlineEntity.ptFS.y - beginPoint.y) / (bufferlineEntity.ptFS.x - beginPoint.x);
                double alpha = Math.atan(tgResult) * 180 / Math.PI;
                double beta = alpha - 180;

                bufferlineEntity.angleFS = alpha;
                bufferlineEntity.angleFE = beta;
                bufferlineEntity.angleSS = alpha + 180.0;
                bufferlineEntity.angleSE = beta + 180;
            }
            else if ( beginPoint.y == endPoint.y )
            {
                bufferlineEntity.angleFS = -90;
                bufferlineEntity.angleFE = 90;
                bufferlineEntity.angleSS = 90;
                bufferlineEntity.angleSE = 270;
            }
        }
        else if ( beginPoint.x < endPoint.x )
        {
            if ( beginPoint.y > endPoint.y )
            {
                double tgResult = (bufferlineEntity.ptFS.y - beginPoint.y) / (bufferlineEntity.ptFS.x - beginPoint.x);
                double alpha = Math.atan(tgResult) * 180.0 / Math.PI;
                double beta = alpha + 180;

                bufferlineEntity.angleFS = alpha;
                bufferlineEntity.angleFE = beta;
                bufferlineEntity.angleSS = alpha + 180.0;
                bufferlineEntity.angleSE = beta + 180;

            }
            else if ( beginPoint.y < endPoint.y )
            {
                double tgResult = (bufferlineEntity.ptFE.y - beginPoint.y) / (bufferlineEntity.ptFE.x - beginPoint.x);
                double alpha = Math.atan(tgResult) * 180.0 / Math.PI + 180;
                double beta = alpha + 180;

                bufferlineEntity.angleFS = alpha;
                bufferlineEntity.angleFE = beta;
                bufferlineEntity.angleSS = alpha + 180.0;
                bufferlineEntity.angleSE = beta + 180;
            }
            else if ( beginPoint.y == endPoint.y )
            {
                bufferlineEntity.angleFS = 90;
                bufferlineEntity.angleFE = 270;
                bufferlineEntity.angleSS = -90;
                bufferlineEntity.angleSE = 90;
            }
        }
        else
        {
            if ( beginPoint.y > endPoint.y )
            {
                bufferlineEntity.angleFS = 0;
                bufferlineEntity.angleFE = 180;
                bufferlineEntity.angleSS = 180;
                bufferlineEntity.angleSE = 360;

            }
            else if ( beginPoint.y < endPoint.y )
            {
                bufferlineEntity.angleFS = 180;
                bufferlineEntity.angleFE = 360;
                bufferlineEntity.angleSS = 0;
                bufferlineEntity.angleSE = 180;
            }
        }

        return bufferlineEntity;
    }

    /**
     * 
     * @param centralLine
     * @param bufferWidth
     * @return
     */
    public ArrayList<BufferLineEntity> genLineBuffer(ArrayList<Point> centralLine, double bufferWidth)
    {
        ArrayList<BufferLineEntity> arrRet = new ArrayList<BufferLineEntity>();

        for (int i = 0; i < centralLine.size() - 1; i++)
        {
            arrRet
                .add(genLineBufferSingleLine((Point) centralLine.get(i), (Point) centralLine.get(i + 1), bufferWidth));
        }

        return arrRet;
    }

    /**
	 * 
	 */
    public BufferLine()
    {
    }
}
