package com.kube.kubepuzzletimer.objects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class Kube {

    private final int numSides = 6;
    private final int numFaces = 9;
    private final int numVerts = 8;
    private final int numEdges = 12;

    float [] red = new float [] { 0.85f, 0.15f, 0.15f };
    float [] blue = new float [] { 0.2f, 0.3f, 0.75f };
    float [] green = new float [] { 0.15f, 0.75f, 0.2f };
    float [] yellow = new float [] { 0.75f, 0.75f, 0.15f };
    float [] purple = new float [] { 0.6f, 0.1f, 0.6f };
    float [] orange = new float [] { 0.9f, 0.55f, 0.1f };
    float [] azure = new float [] { 0.85f, 0.85f, 0.85f };
    float [] cyan = new float [] { 0.15f, 0.85f, 0.85f };
    float [][] colorArray = new float[][] { red, blue, green, yellow, purple, orange, azure, cyan };

    float [][] sideVertices;
    Square [] sides;
    Vertex [] kubeVertices;
    Edge [] kubeEdges;
    Line [] boarders;

    public Kube() {

        // vertices of the original kube piece
        kubeVertices = new Vertex[numVerts];
        kubeVertices[0] = new Vertex(new float[]{ -0.1f, -0.1f, 0f });
        kubeVertices[1] = new Vertex(new float[]{ 0.1f, -0.1f, 0.0f });
        kubeVertices[2] = new Vertex(new float[]{ 0.1f, 0.1f, 0.0f });
        kubeVertices[3] = new Vertex(new float[]{ -0.1f, 0.1f, 0.0f });
        kubeVertices[4] = new Vertex(new float[]{ -0.1f, -0.1f, -0.2f });
        kubeVertices[5] = new Vertex(new float[]{ 0.1f, -0.1f, -0.2f });
        kubeVertices[6] = new Vertex(new float[]{ 0.1f, 0.1f, -0.2f });
        kubeVertices[7] = new Vertex(new float[]{ -0.1f, 0.1f, -0.2f });

        // edges of the kube piece
        kubeEdges = new Edge[numEdges];
        kubeEdges[0] = new Edge(new Vertex(kubeVertices[0]), new Vertex(kubeVertices[1]));
        kubeEdges[1] = new Edge(new Vertex(kubeVertices[1]), new Vertex(kubeVertices[2]));
        kubeEdges[2] = new Edge(new Vertex(kubeVertices[2]), new Vertex(kubeVertices[3]));
        kubeEdges[3] = new Edge(new Vertex(kubeVertices[0]), new Vertex(kubeVertices[3]));
        kubeEdges[4] = new Edge(new Vertex(kubeVertices[4]), new Vertex(kubeVertices[5]));
        kubeEdges[5] = new Edge(new Vertex(kubeVertices[5]), new Vertex(kubeVertices[6]));
        kubeEdges[6] = new Edge(new Vertex(kubeVertices[6]), new Vertex(kubeVertices[7]));
        kubeEdges[7] = new Edge(new Vertex(kubeVertices[4]), new Vertex(kubeVertices[7]));
        kubeEdges[8] = new Edge(new Vertex(kubeVertices[3]), new Vertex(kubeVertices[7]));
        kubeEdges[9] = new Edge(new Vertex(kubeVertices[0]), new Vertex(kubeVertices[4]));
        kubeEdges[10] = new Edge(new Vertex(kubeVertices[1]), new Vertex(kubeVertices[5]));
        kubeEdges[11] = new Edge(new Vertex(kubeVertices[2]), new Vertex(kubeVertices[6]));

        // outline of the kube piece
        boarders = new Line[numEdges];
        for (int i = 0; i < numEdges; i++) {
            boarders[i] = new Line(kubeEdges[i]);
        }

        // vertices for each side of the kube
        sideVertices = new float[numSides][numFaces];
        sideVertices[0] = new float []{     // Square vertices
                -0.1f, -0.1f,  0.0f,        // 0. left-bottom
                0.1f, -0.1f,  0.0f,         // 1. right-bottom
                -0.1f,  0.1f,  0.0f,        // 2. left-top
                0.1f,  0.1f,  0.0f          // 3. right-top
        };
        sideVertices[1] = new float[]{
                -0.1f, -0.1f, -0.2f,
                -0.1f, -0.1f, 0f,
                -0.1f, 0.1f, -0.2f,
                -0.1f, 0.1f, 0f
                };
        sideVertices[2] = new float[]{
                0.1f, -0.1f, 0f,
                0.1f, -0.1f, -0.2f,
                0.1f, 0.1f, 0f,
                0.1f, 0.1f, -0.2f
        };
        sideVertices[3] = new float[]{
                0.1f, -0.1f, -0.2f,
                -0.1f, -0.1f, -0.2f,
                0.1f, 0.1f, -0.2f,
                -0.1f, 0.1f, -0.2f
        };
        sideVertices[4] = new float[]{
                -0.1f, 0.1f, 0f,
                0.1f, 0.1f, 0f,
                -0.1f, 0.1f, -0.2f,
                0.1f, 0.1f, -0.2f
        };
        sideVertices[5] = new float[]{
                -0.1f, -0.1f, -0.2f,
                0.1f, -0.1f, -0.2f,
                -0.1f, -0.1f, 0f,
                0.1f, -0.1f, 0f
        };

        // set the colors
        sides = new Square[numSides];
        for (int i = 0; i < numSides; i++) {
            float [] newColor = new float [] { colorArray[i][0], colorArray[i][1], colorArray[i][2]};
            sides[i] = new Square(sideVertices[i], newColor);
        }
    }

    public void randomizeColors() {
        Random r = new Random();
        int n = r.nextInt(colorArray.length);

        Boolean colorPicked[] = new Boolean[colorArray.length];
        for (int j = 0; j < colorPicked.length; j++) {
            colorPicked[j] = false;
        }

        for (int i = 0; i < numSides; i++) {
            while (colorPicked[n]) {
                n = r.nextInt(colorArray.length);
            }
            float [] newColor = new float [] { colorArray[n][0], colorArray[n][1], colorArray[n][2]};
            sides[i] = new Square(sideVertices[i], newColor);
            colorPicked[n] = true;
        }
    }

    public void draw(GL10 gl) {
        drawSides(gl);
        drawBoarders(gl);
    }

    public void drawSides(GL10 gl) {
        for(int i = 0; i < sides.length; i++) {
            float [] c = sides[i].color;
            gl.glColor4f(c[0], c[1], c[2], 1f);
            drawSide(gl, i);
        }
    }
    public void drawSide(GL10 gl, int side) {
        if (0 <= side && side < numSides) {
            sides[side].draw(gl);
        }
    }

    public void drawBoarders(GL10 gl) {
        gl.glColor4f(0f, 0f, 0f, 1.0f);
        for (Line line: boarders) {
            line.draw(gl);
        }
    }

    private class Vertex {
        float x, y, z;
        public Vertex(float [] xyz) {
            x = xyz[0];
            y = xyz[1];
            z = xyz[2];
        }
        public Vertex(Vertex v) {
            x = v.x;
            y = v.y;
            z = v.z;
        }
    }
    private class Edge {
        Vertex v1, v2;
        public Edge(Vertex v1, Vertex v2) {
            this.v1 = new Vertex(v1);
            this.v2 = new Vertex(v2);
        }
    }
    private class Line {
        private FloatBuffer vfb;
        private ByteBuffer vbb;
        float line[] = {
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f
        };
        public Line(Edge edge) {
            Vertex v1 = edge.v1;
            Vertex v2 = edge.v2;
            setLine(v1.x, v1.y, v1.z, v2.x, v2.y, v2.z);
            vbb = ByteBuffer.allocateDirect(line.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vfb = vbb.asFloatBuffer();
            vfb.put(line);
            vfb.position(0);
        }
        public void setLine(float v0, float v1, float v2, float v3, float v4, float v5) {
            line[0] = v0;
            line[1] = v1;
            line[2] = v2;
            line[3] = v3;
            line[4] = v4;
            line[5] = v5;
        }
        public void draw(GL10 gl) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vfb);
            gl.glDrawArrays(GL10.GL_LINES, 0, line.length / 3);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }

    // square drawn with two triangles
    private class Square {
        private FloatBuffer vfb;
        private ByteBuffer vbb;
        private float[] vertices;
        private float[] color;
        public Square(float [] v, float [] c) {
            this.color = new float[3];
            this.color[0] = c[0];
            this.color[1] = c[1];
            this.color[2] = c[2];
            vertices = new float[v.length];
            for (int i = 0; i < v.length; i++) {
                vertices[i] = v[i];
            }
            vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vfb = vbb.asFloatBuffer();
            vfb.put(vertices);
            vfb.position(0);
        }
        public void draw(GL10 gl) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vfb);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }
    }
}