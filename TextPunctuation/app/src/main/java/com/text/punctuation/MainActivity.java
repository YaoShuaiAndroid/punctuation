package com.text.punctuation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.text.punctuation.model.ShowRoomListModel;
import com.text.punctuation.model.ShowRoomModel;
import com.text.punctuation.util.BitmapUtils;
import com.text.punctuation.view.ImageDotLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    //模拟数据
    List<ShowRoomListModel> showRoomListModels;
    //图片总地址
    private List<Bitmap> bitmaps = new ArrayList<>();

    private int imageSize=0;

    private ImageDotLayout imageDotLayout;

    String[] imgs=new String[]{
            "http://p1.so.qhimgs1.com/bdr/_240_/t01671d7969d7be0e71.jpg",
            "http://p4.so.qhmsg.com/bdr/_240_/t013ac1e3768d7292e9.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageDotLayout= (ImageDotLayout) findViewById(R.id.gallery_photo);

        initData();
    }

    //创建模拟数据
    public void initData(){
        showRoomListModels=new ArrayList<>();
        for (int i = 1; i <imgs.length+1 ; i++) {
            //楼层
            ShowRoomListModel showRoomListModel=new ShowRoomListModel();
            showRoomListModel.setPlan(imgs[i-1]);
            showRoomListModel.setNo("1");
            List<ShowRoomModel> showRoomModels=new ArrayList<>();
            for (int j = 1; j <5; j++) {
                //标记点
                ShowRoomModel showRoomModel=new ShowRoomModel();
                showRoomModel.setId(i+""+j);
                showRoomModel.setName(i+"楼标记"+j);
                showRoomModel.setX(50*j);
                showRoomModel.setY(50*j);
                showRoomModels.add(showRoomModel);
            }
            showRoomListModel.setShowroomList(showRoomModels);
            showRoomListModels.add(showRoomListModel);
        }
        Log.i("tag",""+showRoomListModels.size());
        setFloor(showRoomListModels);
    }


    public void setFloor(final List<ShowRoomListModel> showRoomListModels) {
        for (int i = 0; i < showRoomListModels.size(); i++) {
            //将图片设为空，为了保证加载图片时图片插入的顺序是对的
            bitmaps.add(null);
            final int finalI = i;
            Glide.with(MainActivity.this)
                    .load(showRoomListModels.get(i).getPlan())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageSize++;
                            //保存图片的原始宽高，防止后面图片拼接是变化了宽高而找不到标记点
                            showRoomListModels.get(finalI).setImageHeight(bitmap.getHeight());
                            showRoomListModels.get(finalI).setImageWidth(bitmap.getWidth());
                            //将图片放大或缩小到屏幕的宽度
                            bitmap= BitmapUtils.compressScale(bitmap,500);
                            //因为图片因为大小有先后所以也需要按顺序放置
                            bitmaps.set(finalI,bitmap);

                            showRoomListModels.get(finalI).setImageFullHeight(bitmap.getHeight());
                            showRoomListModels.get(finalI).setImageFullWidth(bitmap.getWidth());
                            Log.i("tag",""+imageSize);
                            //当为最后一次循环获取图片后拼接图片
                            if (bitmaps != null && bitmaps.size() > 0 && imageSize == showRoomListModels.size()) {
                                Log.i("tag","进入");
                                newBitmap(bitmaps);
                            }
                        }
                    });
        }
    }

    private Bitmap bitmap;
    /**
     * 拼接图片
     *
     * @param bitmaps 图片数组
     * @return 返回拼接后的Bitmap
     */
    private void newBitmap(List<Bitmap> bitmaps) {

        int width = bitmaps.get(0).getWidth();
        int height = bitmaps.get(0).getHeight();
        int currentHeight=0;

        //获取总高度
        for (int i = 0; i < bitmaps.size(); i++) {
            height = height + bitmaps.get(i).getHeight();
        }
        //创建一个空的Bitmap(内存区域),宽度等于第一张图片的宽度，高度等于多张图片高度总和

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        //将bitmap放置到绘制区域,并将要拼接的图片绘制到指定内存区域
        Canvas canvas = new Canvas(bitmap);
        //一张纸拼接图片
        for (int i = 0; i < bitmaps.size(); i++) {
            if (i == 0) {
                currentHeight=bitmaps.get(i).getHeight();
                canvas.drawBitmap(bitmaps.get(i), 0, 0, null);
            } else {
                //设置拼接的位置坐标
                canvas.drawBitmap(bitmaps.get(i), 0, currentHeight, null);
                currentHeight=currentHeight+bitmaps.get(i).getHeight();
            }
        }
        //设置背景图片
        imageDotLayout.setImage(bitmap,2);

        initIcon(height);

        //监听图片是否加载完成
        imageDotLayout.setOnLayoutReadyListener(new ImageDotLayout.OnLayoutReadyListener() {
            @Override
            public void onLayoutReady() {

            }
        });

        imageDotLayout.setOnIconClickListener(new ImageDotLayout.OnIconClickListener() {
            @Override
            public void onIconClick(View v) {
                ImageDotLayout.IconBean bean = (ImageDotLayout.IconBean) v.getTag();

                Toast.makeText(MainActivity.this, "位置=" + bean.name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<ImageDotLayout.IconBean> iconBeanList = new ArrayList<>();
    /**
     * 添加标记点
     */
    private void initIcon(int height) {
        float oneWidth = 0;
        float oneHeigth = 0;
        int imageHeight=0;
        int item=0;
        //因为每张楼层的图片都不同，所以将其放大缩小到屏幕宽度后，从后台返回的标记点位置也要通过运算做出统一处理
        for (int i = 0; i < showRoomListModels.size(); i++) {
            for (int j = 0; j <showRoomListModels.get(i).getShowroomList().size(); j++) {
                ShowRoomModel showRoomModel=showRoomListModels.get(i).getShowroomList().get(j);
                oneWidth = showRoomModel.getX() / (float)showRoomListModels.get(i).getImageWidth();
                oneHeigth = (showRoomModel.getY()*showRoomListModels.get(i).getImageFullHeight()/showRoomListModels.get(i).getImageHeight()+imageHeight)/(float)height;
                String name=showRoomModel.getName();

                ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(item,showRoomModel.getId(),name, oneWidth, oneHeigth, null);
                item++;
                iconBeanList.add(bean);
            }
            //添加楼层标记
            ImageDotLayout.IconBean bean = new ImageDotLayout.IconBean(-1,"-1",(i+1)+"F",0.05f, (float)(imageHeight+30)/(float)height, null);
            iconBeanList.add(bean);

            imageHeight=imageHeight+showRoomListModels.get(i).getImageFullHeight();
        }

        imageDotLayout.addIcons(iconBeanList);
    }
}
