package Tools;

import FAProps.FAMap;
import org.joml.Vector2f;

/**
 * Created by Basti on 17.7.16.
 */
public class SymmetryHandler {

    private MirrorMode mirrorMode = MirrorMode.MIRROR_NONE;
    private Tool usedTool;

    public void setTool(Tool usedTool) {
        this.usedTool = usedTool;
    }

    public MapChangeHandler applyTool(FAMap map, boolean left, boolean middle, boolean right, Vector2f pos) {
        Vector2f middlePos = new Vector2f(map.getMapDetails().getWidth() / 2f, map.getMapDetails().getHeight() / 2f);
        MapChangeHandler handler = new MapChangeHandler(map);
        switch (mirrorMode) {
            case MIRROR_NONE:
                handler = usedTool.applyTool(handler, left, middle, right, pos);
                break;
            case MIRROR_LEFT_RIGHT:
                handler = usedTool.applyTool(handler, left, middle, right, pos);
                handler = usedTool.applyTool(handler, left, middle, right, new Vector2f(map.getMapDetails().getWidth() - pos.x, pos.y));
                break;
            case MIRROR_UP_DOWN:
                handler = usedTool.applyTool(handler, left, middle, right, pos);
                handler = usedTool.applyTool(handler, left, middle, right, new Vector2f(pos.x, map.getMapDetails().getHeight() - pos.y));
                break;
            case MIRROR_POINT_2:
                handler = usedTool.applyTool(handler, left, middle, right, pos);
                handler = usedTool.applyTool(handler, left, middle, right, new Vector2f(map.getMapDetails().getWidth() - pos.x, map.getMapDetails().getHeight() - pos.y));
                break;
            case MIRROR_POINT_4:
                //usedTool.applyTool(handler,left,middle,right,pos);
                //usedTool.applyTool(handler,left,middle,right,pos);
                //usedTool.applyTool(handler,left,middle,right,pos);
                //usedTool.applyTool(handler,left,middle,right,pos);
        }
        return handler;
    }

    public boolean hasSize() {
        return usedTool.hasSize();
    }

    public boolean hasBrush() {
        return usedTool.hasBrush();
    }

    public void setMirrorMode(MirrorMode mirrorMode) {
        this.mirrorMode = mirrorMode;
    }

    public enum MirrorMode {MIRROR_NONE, MIRROR_LEFT_RIGHT, MIRROR_UP_DOWN, MIRROR_POINT_2, MIRROR_POINT_4}
}
