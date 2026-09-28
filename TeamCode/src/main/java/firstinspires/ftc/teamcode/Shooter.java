package firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Shooter {
    private final Robot robot;
    private double targetPower;
    private double commandedPower;
    private final ElapsedTime overCurrentTimer = new ElapsedTime();
    private boolean overCurrentActive;

    public Shooter(Robot robot) {
        this.robot = robot;
        overCurrentTimer.reset();
    }

    public void setPower(double power) {
        targetPower = Range.clip(power, -Config.OUTTAKE_MAX_POWER, Config.OUTTAKE_MAX_POWER);
    }

    public void update() {
        if (isOverCurrent()) {
            if (!overCurrentActive) {
                overCurrentActive = true;
                overCurrentTimer.reset();
            }
        } else {
            overCurrentActive = false;
            overCurrentTimer.reset();
        }

        if (overCurrentActive && overCurrentTimer.milliseconds() >= Config.OUTTAKE_STALL_TIME_MS) {
            targetPower = 0;
        }

        commandedPower += Range.clip(targetPower - commandedPower, -Config.OUTTAKE_SLEW_PER_LOOP, Config.OUTTAKE_SLEW_PER_LOOP);
        robot.outtakeLeft.setPower(commandedPower);
        robot.outtakeRight.setPower(commandedPower);
    }

    public double getCurrentAmps() {
        return Math.max(robot.outtakeLeft.getCurrent(CurrentUnit.AMPS), robot.outtakeRight.getCurrent(CurrentUnit.AMPS));
    }

    public boolean isOverCurrent() {
        return getCurrentAmps() >= Config.OUTTAKE_CURRENT_LIMIT_AMPS;
    }

    public double getCommandedPower() {
        return commandedPower;
    }

    public double getTargetPower() {
        return targetPower;
    }

    public void stop() {
        setPower(0);
        robot.outtakeLeft.setPower(0);
        robot.outtakeRight.setPower(0);
    }
}
