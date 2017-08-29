package rgups.railvideo.core;

import org.opencv.core.Mat;
import rgups.railvideo.service.MatSupervisor;

/**
 * Created by Dmitry on 20.08.2017.
 */
public class RvMat extends Mat {

    public RvMat(Mat mat) {
        super(mat.getNativeObjAddr());
        registerInDefaultSupervisor();
    }

    public RvMat() {
        super();
        registerInDefaultSupervisor();
    }

    public RvMat(int rows, int cols, int type) {
        super(rows, cols, type);
        registerInDefaultSupervisor();
    }

    public Mat clone() {
        RvMat retVal = new RvMat(super.clone());
        return retVal;
    }

    public Mat cloneAsMat() {
        return super.clone();
    }

    public void registerInDefaultSupervisor() {
        MatSupervisor.registerMatDefault(this);
    }
}
