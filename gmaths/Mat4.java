//This was provided, I use if for all of my matrix manipulations, I've also added a Vec4 * Mat4 multiplier which returns the xyz coordinates

package gmaths;

public class Mat4 {   // row column formulation

  private float[][] values;
  
  public Mat4() {
    this(0);
  }
  
  public Mat4(float f) {
    values = new float[4][4];
    makeZero();
    for (int i=0; i<4; ++i) {
      values[i][i] = f;
    }
  }
  
  public Mat4(Mat4 m) {
    this.values = new float[4][4];
    for (int i=0; i<4; ++i) {
      for (int j=0; j<4; ++j) {
        this.values[i][j] = m.values[i][j];
      }
    }
  }
  
  public void set(int r, int c, float f) {
    values[r][c] = f;
  }
  public float get(int r, int c) { return values[r][c];}

  private void makeZero() {
    for (int i=0; i<4; ++i) {
      for (int j=0; j<4; ++j) {
        values[i][j] = 0;
      }
    }
  }
  
  public void transpose() {
    for (int i=0; i<4; ++i) {
      for (int j=i; j<4; ++j) {
        float t = values[i][j];
        values[i][j] = values[j][i];
        values[j][i] = t;
      }
    }
  }
    
  public static Mat4 transpose(Mat4 m) {
    Mat4 a = new Mat4(m);
    for (int i=0; i<4; ++i) {
      for (int j=i; j<4; ++j) {
        float t = a.values[i][j];
        a.values[i][j] = a.values[j][i];
        a.values[j][i] = t;
      }
    }
    return a;
  }

  public static Mat4 multiply(Mat4 a, Mat4 b) {
    Mat4 result = new Mat4();
    for (int i=0; i<4; ++i) {
      for (int j=0; j<4; ++j) {
        for (int k=0; k<4; ++k) {
          result.values[i][j] += a.values[i][k]*b.values[k][j];
        }
      }
    }
    return result;
  }

  public static Vec3 multiply(Vec4 a, Mat4 b) {
    Vec3 result = new Vec3();
    result.x += (a.x * b.values[0][0]) + (a.y * b.values[0][1]) + (a.z * b.values[0][2]);
    result.y += (a.x * b.values[1][0]) + (a.y * b.values[1][1]) + (a.z * b.values[1][2]);
    result.z += (a.x * b.values[2][0]) + (a.y * b.values[2][1]) + (a.z * b.values[2][2]);

    return result;
  }

  public float[] toFloatArrayForGLSL() {  // col by row
    float[] f = new float[16];
    for (int j=0; j<4; ++j) {
      for (int i=0; i<4; ++i) {
        f[j*4+i] = values[i][j];
      }
    }
    return f;
  }
  
  public String asFloatArrayForGLSL() {  // col by row
    String s = "{";
    for (int j=0; j<4; ++j) {
      for (int i=0; i<4; ++i) {
        s += String.format("%.2f",values[i][j]);
        if (!(j==3 && i==3)) s+=",";
      }
    }
    return s;
  }
  
  public String toString() {
    String s = "{";
    for (int i=0; i<4; ++i) {
      s += (i==0) ? "{" : " {";
      for (int j=0; j<4; ++j) {
        s += String.format("%.2f",values[i][j]);  
        if (j<3) s += ", ";
      }
      s += (i==3) ? "}" : "},\n";
    } 
    s += "}";
    return s;
  }
  
} // end of Mat4 class