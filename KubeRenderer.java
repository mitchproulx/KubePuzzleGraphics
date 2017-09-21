package com.kube.kubepuzzletimer.business;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.kube.kubepuzzletimer.objects.Kube;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class KubeRenderer implements GLSurfaceView.Renderer {

    private Kube [] kubes = new Kube[27];

    private double time;
    private float frameTime = 0;
    private int currFrame = 0;
    private int numKubePieces = 27;
    private int kubeIndex = 0;
    private boolean RANDOMIZE = false, ENABLE_ROTATE = true, ROTATING = true;
    private float speedX = 4f, speedY = 2f, speedZ = 7f;

    private int moveAgain = 0;
    private int width;
    private int height;

    public volatile float mAngle;
    public float mPreviousX = 0, mPreviousY = 0, currX = 0, currY = 0;

    public KubeRenderer() {
        kubes = new Kube[numKubePieces];
        for (int i = 0; i < numKubePieces; i++) {
            kubes[i] = new Kube();
        }
    }

    // Call back when the surface is first created or re-created.
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);    // black
        gl.glClearDepthf(1.0f);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DITHER);
        gl.glEnable(GL10.GL_LINE_SMOOTH);
    }

    // Call back after onSurfaceCreated() or whenever the window's size changes.
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.height = height;
        this.width = width;

        if (height == 0) {
            height = 1;   // To prevent divide by zero
        }
        float aspect = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        time = System.nanoTime();
        frameTime += time;
        if (frameTime >= 0.06f) {        // if 1/60th of a second passed (for 60fps)
            currFrame++;
            if (currFrame == Integer.MAX_VALUE) {
                currFrame = 0;
            }

            if (currFrame > 60 && currFrame < 80) {
                RANDOMIZE = true;
            }else{
                RANDOMIZE = false;
            }

            if (!ROTATING && ENABLE_ROTATE) {
                moveAgain++;
            }
            if(moveAgain >= 150) {
                ROTATING = true;
                moveAgain = 0;
            }
        }

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // DRAW THE KUBE
        gl.glPushMatrix();
        moveIntoView(gl);
        gl.glPushMatrix();
        rotate(gl);
        drawKube(gl);
        gl.glPopMatrix();
        gl.glPopMatrix();

    }
    public void stopRotation(){
        ROTATING = false;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public void setAngle(float angle) {
        this.mAngle = angle;
    }
    public float getAngle() {
        return mAngle;
    }
    public void moveIntoView(GL10 gl){
        gl.glTranslatef(0,0,-3f);
        gl.glRotatef(45, 1, 0, 0);
        gl.glRotatef(25, 0, 1, 0);
    }
    public void rotate(GL10 gl){
        if (ROTATING && ENABLE_ROTATE) {

            if (speedZ > 4f) {
                speedZ -= 0.05f;
            }
            if (speedY > 1f) {
                speedY -= 0.05f;
            }
            if (speedX > 2f) {
                speedX -= 0.05f;
            }
            gl.glRotatef(speedZ * currFrame, 0, 0, 1);
            gl.glRotatef(speedY * currFrame, 0, 1, 0);
            gl.glRotatef(speedX * currFrame, 1, 0, 0);
        }
        else {
            float moveX = Math.abs(currX - mPreviousX);
            float moveY = Math.abs(currY - mPreviousY);

            if (moveX != 0 && moveX > moveY) {
                gl.glRotatef(-0.15f * mAngle, 0, 1, 1);
            }
            else if (moveY != 0 && moveY > moveX) {
                gl.glRotatef(-0.1f * mAngle, 1, 0, 1);
            }
        }
    }
    public void drawKube(GL10 gl){
        gl.glPushMatrix();
        // BACK layer
        drawLayer(gl);
        // MIDDLE layer
        gl.glTranslatef(0f, 0f, 0.2f);
        drawLayer(gl);
        // FRONT layer
        gl.glTranslatef(0f, 0f, 0.2f);
        drawLayer(gl);
        gl.glPopMatrix();
    }
    public void drawKubePiece(GL10 gl) {
        // cube piece at origin
        gl.glPushMatrix();
        if (kubeIndex >= 27) {
            kubeIndex = 0;
            if (RANDOMIZE) {
                kubes[kubeIndex].randomizeColors();
            }
            kubes[kubeIndex].draw(gl);
            kubeIndex++;
        }
        else{
            if (RANDOMIZE) {
                kubes[kubeIndex].randomizeColors();
            }
            kubes[kubeIndex].draw(gl);
            kubeIndex++;
        }
        gl.glPopMatrix();
    }

    public void drawLayer(GL10 gl) {
        gl.glPushMatrix();

        // CENTER
        drawKubePiece(gl);
        // CENTER-RIGHT
        gl.glTranslatef(0.2f, 0.0f, 0.0f);
        drawKubePiece(gl);
        // CENTER-LEFT
        gl.glTranslatef(-0.4f, 0.0f, 0.0f);
        drawKubePiece(gl);
        // CENTER-TOP
        gl.glTranslatef(0.2f, 0.2f, 0.0f);
        drawKubePiece(gl);
        // CENTER-BOTTOM
        gl.glTranslatef(0.0f, -0.4f, 0.0f);
        drawKubePiece(gl);
        // BOTTOM-LEFT
        gl.glTranslatef(-0.2f, 0.0f, 0.0f);
        drawKubePiece(gl);
        // BOTTOM-RIGHT
        gl.glTranslatef(0.4f, 0.0f, 0.0f);
        drawKubePiece(gl);
        // TOP-RIGHT
        gl.glTranslatef(0.0f, 0.4f, 0.0f);
        drawKubePiece(gl);
        // TOP-LEFT
        gl.glTranslatef(-0.4f, 0.0f, 0.0f);
        drawKubePiece(gl);
        gl.glPopMatrix();
    }
}