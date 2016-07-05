package editor.model;

import configuration.model.engine.CalvisFormattedInstruction;
import simulatorvisualizer.controller.SystemController;

/**
 * Created by Goodwin Chua on 1/23/2016.
 */
public abstract class AssemblyComponent {

    protected SystemController sysCon;

    public abstract void update(CalvisFormattedInstruction currentInstruction, int lineNumber);

    public abstract void refresh();

    public abstract void build();

    public void setSysCon(SystemController sysCon) {
        this.sysCon = sysCon;
    }

}
